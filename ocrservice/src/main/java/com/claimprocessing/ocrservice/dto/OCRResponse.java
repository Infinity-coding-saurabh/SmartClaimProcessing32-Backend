package com.claimprocessing.ocrservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OCRResponse {

    private String status;
    private String extractedText;
}
