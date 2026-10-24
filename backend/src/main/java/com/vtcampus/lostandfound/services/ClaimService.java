
package com.vtcampus.lostandfound.services;

import com.vtcampus.lostandfound.domain.entities.Claim;
import com.vtcampus.lostandfound.domain.entities.ClaimStatus;

import java.util.List;
import java.util.Optional;

public interface ClaimService {

    Claim createClaim(Claim claim);

    List<Claim> viewAllClaims();

    Optional<Claim> viewClaimById(Long id);

    List<Claim> viewClaimsByStatus(ClaimStatus status);

    Claim updateClaimStatus(Long id, ClaimStatus status);

    void deleteClaim(Long id);
}
