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

                // Extract features from claimData map
                float patientAge = ((Number) claimData.get("patient_age")).floatValue();
                float claimAmount = ((Number) claimData.get("claim_amount")).floatValue();
                float ocrConfidence = ((Number) claimData.get("ocr_confidence")).floatValue();
                float numDocuments = ((Number) claimData.get("num_documents")).floatValue();
                float ruleFlagsCount = ((Number) claimData.get("rule_flags_count")).floatValue();
                float daysBetween = ((Number) claimData.get("days_between_prescription_and_claim")).floatValue();
                float doctorRegistered = ((Number) claimData.get("doctor_registered")).floatValue();
                float isHospitalVerified = ((Number) claimData.get("is_hospital_verified")).floatValue();

                float[][] input = {{
                        patientAge,
                        claimAmount,
                        ocrConfidence,
                        numDocuments,
                        ruleFlagsCount,
                        daysBetween,
                        doctorRegistered,
                        isHospitalVerified
                }};

                // Make sure input name matches ONNX model input name
                Map<String, OnnxTensor> inputMap = Map.of(
                        "input", OnnxTensor.createTensor(env, input)
                );

                OrtSession.Result result = session.run(inputMap);
                float[][] output = (float[][]) result.get(0).getValue();

                // Return prediction as JSON
                return Map.of(
                        "fraud_probability", output[0][0],
                        "is_fraud", output[0][0] > 0.5 // example threshold
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
