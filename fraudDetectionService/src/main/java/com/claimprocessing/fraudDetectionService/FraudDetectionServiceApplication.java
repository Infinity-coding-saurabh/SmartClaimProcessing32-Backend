package com.claimprocessing.fraudDetectionService;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@SpringBootApplication
public class FraudDetectionServiceApplication {

	public static void main(String[] args) throws Exception {

		SpringApplication.run(FraudDetectionServiceApplication.class, args);

	}
}
