package com.claimprocessing.ocrservice.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "ocrSpaceClient", url = "https://api.ocr.space")
public interface OcrSpaceClient {

    @PostMapping(value = "/parse/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String extractText(
            @RequestHeader("apikey") String apiKey,
            @RequestPart("file") MultipartFile file,
            @RequestPart("language") String language,
            @RequestPart("isOverlayRequired") String isOverlayRequired
    );
}
