package com.claimprocessing.ocrservice.service;

import com.claimprocessing.ocrservice.clients.OcrSpaceClient;
import com.claimprocessing.ocrservice.dto.OCRResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OCRSpaceService {


    private final OcrSpaceClient ocrClient;

    @Value("${ocr.space.api-key}")
    private String apiKey;

    public OCRResponse extractText(MultipartFile file) {
        try {
            String rawResponse = ocrClient.extractText(apiKey, file, "eng", "false");

            // parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(rawResponse);
            String extractedText = node.path("ParsedResults").get(0).path("ParsedText").asText();

            return new OCRResponse("success", extractedText);

        } catch (Exception e) {
            e.printStackTrace();
            return new OCRResponse("error", e.getMessage());
        }
    }

}
