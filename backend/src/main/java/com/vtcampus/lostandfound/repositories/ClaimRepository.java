package com.vtcampus.lostandfound.repositories;

import com.vtcampus.lostandfound.domain.entities.Claim;
import com.vtcampus.lostandfound.domain.entities.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByStatus(ClaimStatus claimStatus);
}
