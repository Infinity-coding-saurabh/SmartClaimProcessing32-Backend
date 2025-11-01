package com.claimprocessing.claimservice.service;

import com.claimprocessing.claimservice.dto.ClaimRequestDTO;
import com.claimprocessing.claimservice.dto.ClaimResponseDTO;
import com.claimprocessing.claimservice.entity.Claim;
import com.claimprocessing.claimservice.entity.ClaimReceipt;
import com.claimprocessing.claimservice.repository.ClaimReceiptRepository;
import com.claimprocessing.claimservice.repository.ClaimRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private ClaimReceiptRepository claimReceiptRepository;


    public ClaimResponseDTO submitClaim(@Valid ClaimRequestDTO claimRequest) {
        Claim claim = new Claim();
        claim.setClaimId(claimRequest.getClaimId());
        claim.setFirstName(claimRequest.getFirstName());
        claim.setLastName(claimRequest.getLastName());
        claim.setClaimAmount(claimRequest.getClaimAmount());
        claim.setClaimType(claimRequest.getClaimType());
        claim.setDiagnosis(claimRequest.getDiagnosis());
        claim.setCreatedDate(LocalDate.now());
        claim.setPolicyNumber(claimRequest.getPolicyNumber());
        claim.setStatus("Submitted");

        claimRepository.save(claim);

        return new ClaimResponseDTO(claim.getClaimId(),claim.getFirstName(), claim.getLastName(),
                claim.getPolicyNumber(), claim.getClaimType(), claim.getSubmissionDate(), claim.getDiagnosis(),
                claim.getClaimAmount(), claim.getCreatedDate());
    }

    public ClaimResponseDTO getClaimById(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Claim not found"));
        return new ClaimResponseDTO(
                claim.getClaimId(),claim.getFirstName(), claim.getLastName(),
                claim.getPolicyNumber(), claim.getClaimType(), claim.getSubmissionDate(), claim.getDiagnosis(),
                claim.getClaimAmount(), claim.getCreatedDate()
        );
    }

    public void saveClaimReceipt(MultipartFile file, @NotNull Long claimId) {
        try {
            ClaimReceipt receipt = new ClaimReceipt();
            receipt.setFileName(file.getOriginalFilename());
            receipt.setContentType(file.getContentType());
            receipt.setData(file.getBytes());
            receipt.setClaimId(claimId);

            claimReceiptRepository.save(receipt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save PDF", e);
        }
    }
}
