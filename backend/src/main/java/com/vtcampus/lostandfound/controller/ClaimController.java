
package com.vtcampus.lostandfound.controller;

import com.vtcampus.lostandfound.domain.dto.ClaimRequest;
import com.vtcampus.lostandfound.domain.dto.ClaimResponse;
import com.vtcampus.lostandfound.domain.entities.ClaimStatus;
import com.vtcampus.lostandfound.mappers.ClaimMapper;
import com.vtcampus.lostandfound.services.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;
    private final ClaimMapper claimMapper;

    public ClaimController(
            ClaimService claimService,
            ClaimMapper claimMapper) {
        this.claimService = claimService;
        this.claimMapper = claimMapper;
    }

    @PostMapping
    public ResponseEntity<ClaimResponse> createClaim(
            @Valid @RequestBody ClaimRequest request) {

        var claim = claimMapper.toEntity(request);
        var savedClaim = claimService.createClaim(claim);
        var response = claimMapper.toResponse(savedClaim);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        List<ClaimResponse> responses = claimService.viewAllClaims()
                .stream()
                .map(claimMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaimById(
            @PathVariable Long id) {

        return claimService.viewClaimById(id)
                .map(claimMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByStatus(
            @PathVariable ClaimStatus status) {

        List<ClaimResponse> responses = claimService.viewClaimsByStatus(status)
                .stream()
                .map(claimMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClaimResponse> updateClaimStatus(
            @PathVariable Long id,
            @RequestParam ClaimStatus status) {

        var updatedClaim = claimService.updateClaimStatus(id, status);
        var response = claimMapper.toResponse(updatedClaim);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }
}
