package com.claimprocessing.claimservice.repository;

import com.claimprocessing.claimservice.entity.ClaimReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimReceiptRepository extends JpaRepository<ClaimReceipt, Long> {
}

