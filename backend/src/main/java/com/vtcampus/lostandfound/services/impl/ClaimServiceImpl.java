package com.vtcampus.lostandfound.services.impl;

import com.vtcampus.lostandfound.domain.entities.Claim;
import com.vtcampus.lostandfound.domain.entities.ClaimStatus;
import com.vtcampus.lostandfound.repositories.ClaimRepository;
import com.vtcampus.lostandfound.services.ClaimService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository){
        this.claimRepository = claimRepository;
    }

    @Override
    public Claim createClaim(Claim claim) {
        return claimRepository.save(claim);
    }

    @Override
    public List<Claim> viewAllClaims() {
        return claimRepository.findAll();
    }

    @Override
    public Optional<Claim> viewClaimById(Long id) {
        return claimRepository.findById(id);
    }

    @Override
    public List<Claim> viewClaimsByStatus(ClaimStatus status) {
        return claimRepository.findByStatus(status);
    }

    @Override
    public Claim updateClaimStatus(Long id, ClaimStatus status) {
        Claim existingClaim = claimRepository.findById(id).orElseThrow();
        existingClaim.setStatus(status);
        return claimRepository.save(existingClaim);
    }

    @Override
    public void deleteClaim(Long id) {
        claimRepository.deleteById(id);
    }
}
