package com.claimprocessing.fraudDetectionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ai.onnxruntime.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@RestController
public class FraudController {

    @PostMapping("/fraud")
    public Map<String, Object> getData(@RequestBody Map<String, Object> claimData) {
        try {
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

                // Build input tensor dynamically from claimData map
                float[][] input = {{
                        ((Number) claimData.get("patient_age")).floatValue(),
                        ((Number) claimData.get("claim_amount")).floatValue(),
                        ((Number) claimData.get("ocr_confidence")).floatValue(),
                        ((Number) claimData.get("num_documents")).floatValue(),
                        ((Number) claimData.get("rule_flags_count")).floatValue(),
                        ((Number) claimData.get("days_between_prescription_and_claim")).floatValue(),
                        ((Number) claimData.get("doctor_registered")).floatValue(),
                        ((Number) claimData.get("is_hospital_verified")).floatValue()
                }};

                Map<String, OnnxTensor> inputMap = Map.of(
                        "input", OnnxTensor.createTensor(env, input)
                );

                // Run model
                OrtSession.Result result = session.run(inputMap);

                // TreeEnsembleClassifier returns long[] for class labels
                long[] prediction = (long[]) result.get(0).getValue();
                boolean isFraud = prediction[0] == 1;

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
