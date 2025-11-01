package com.claimprocessing.ocrservice.controller;


import com.claimprocessing.ocrservice.dto.OCRResponse;
import com.claimprocessing.ocrservice.service.OCRSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ocr")
@RequiredArgsConstructor
public class OCRController {
    private final OCRSpaceService ocrService;

    @PostMapping(value = "/extract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OCRResponse> extractText(@RequestParam("file") MultipartFile file) {
        OCRResponse response = ocrService.extractText(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OCR Service is up ✅");
    }
}
