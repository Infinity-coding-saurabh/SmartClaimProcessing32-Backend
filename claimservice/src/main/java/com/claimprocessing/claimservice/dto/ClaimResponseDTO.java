package com.claimprocessing.claimservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class ClaimResponseDTO {
    @NotNull
    private Long claimId;
    private String firstName;
    private String lastName;
    private String policyNumber;
    private String claimType;
    private LocalDate submissionDate;
    private String diagnosis;
    @NotBlank
    private BigDecimal claimAmount;
    @NotBlank
    private LocalDate createdDate;
    private String status;

    public ClaimResponseDTO(Long claimId, String firstName, String lastName, String policyNumber, String claimType, LocalDate submissionDate, String diagnosis, BigDecimal claimAmount, LocalDate createdDate) {
    }
}
