package com.claimprocessing.claimservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "insurance_claims")
@Data
@Setter
@Getter
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimId;
    private String firstName;
    private String lastName;
    private String policyNumber;
    private String claimType;
    private LocalDate submissionDate;
    private String diagnosis;
    private BigDecimal claimAmount;
    private LocalDate createdDate;
    private String status;
}
