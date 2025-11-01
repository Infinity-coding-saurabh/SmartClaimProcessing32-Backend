package com.claimprocessing.claimservice.controller;

import com.claimprocessing.claimservice.dto.ClaimRequestDTO;
import com.claimprocessing.claimservice.dto.ClaimResponseDTO;
import com.claimprocessing.claimservice.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ClaimResponseDTO> submitClaim(@Valid @RequestBody ClaimRequestDTO claimRequest,
                                                        @RequestPart("file") MultipartFile file) {
        //save claim data
        ClaimResponseDTO response = claimService.submitClaim(claimRequest);

        //save pdf file
        claimService.saveClaimReceipt(file, response.getClaimId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponseDTO> getClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }
}

