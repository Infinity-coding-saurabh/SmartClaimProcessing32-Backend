package com.claimprocessing.fraudDetectionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ai.onnxruntime.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@RestController
public class FraudController {

    @GetMapping("/fraud")
    public Map<String, Object> getData() {
        try {
            // Example claim data (set values likely to be classified as fraud)
            float[][] input = {{
                    60f,       // patient_age
                    200000f,   // claim_amount
                    0.2f,      // ocr_confidence
                    1f,        // num_documents
                    5f,        // rule_flags_count
                    0f,        // days_between_prescription_and_claim
                    0f,        // doctor_registered
                    0f         // is_hospital_verified
            }};

            // Load ONNX model from resources
            InputStream modelStream = FraudDetectionServiceApplication.class
                    .getClassLoader()
                    .getResourceAsStream("fraud_model.onnx");

            if (modelStream == null) {
                throw new RuntimeException("⚠️ Model file not found in resources!");
            }

            Path tempModelPath = Files.createTempFile("fraud_model", ".onnx");
            Files.copy(modelStream, tempModelPath, StandardCopyOption.REPLACE_EXISTING);

            try (OrtEnvironment env = OrtEnvironment.getEnvironment();
                 OrtSession session = env.createSession(tempModelPath.toString(), new OrtSession.SessionOptions())) {

                Map<String, OnnxTensor> inputMap = Map.of(
                        "input", OnnxTensor.createTensor(env, input)
                );

                // Run model
                OrtSession.Result result = session.run(inputMap);

                // TreeEnsembleClassifier usually returns long[] for class labels
                long[] prediction = (long[]) result.get(0).getValue();

                boolean isFraud = prediction[0] == 1;

                // Return JSON response
                return Map.of(
                        "prediction", prediction[0],
                        "is_fraud", isFraud
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return Map.of(
                    "error", "Failed to run ONNX model",
                    "details", ex.getMessage()
            );
        }
    }
}
