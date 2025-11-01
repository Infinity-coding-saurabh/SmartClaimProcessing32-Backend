package com.claimprocessing.claimservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ClaimReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String contentType;

    @Lob
    private byte[] data;

    private Long claimId;
    private LocalDateTime uploadedAt = LocalDateTime.now();

}

