
package com.vtcampus.lostandfound.services.impl;

import com.vtcampus.lostandfound.domain.entities.Claim;
import com.vtcampus.lostandfound.domain.entities.ClaimStatus;
import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemStatus;
import com.vtcampus.lostandfound.repositories.ClaimRepository;
import com.vtcampus.lostandfound.repositories.ItemReportRepository;
import com.vtcampus.lostandfound.services.ClaimService;
import com.vtcampus.lostandfound.services.CurrentUserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final ItemReportRepository itemReportRepository;
    private final CurrentUserService currentUserService;

    public ClaimServiceImpl(
            ClaimRepository claimRepository,
            ItemReportRepository itemReportRepository,
            CurrentUserService currentUserService) {
        this.claimRepository = claimRepository;
        this.itemReportRepository = itemReportRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public Claim createClaim(Claim claim) {
        var currentUser = currentUserService.getCurrentUser();

        Long itemReportId = claim.getItemReport().getId();

        ItemReport item = itemReportRepository.findById(itemReportId)
                .orElseThrow(() ->
                        new RuntimeException("Item report not found"));

        if (item.getStatus() != ItemStatus.ACTIVE) {
            throw new IllegalStateException(
                    "This item is no longer available");
        }

        if (item.getReportedBy().getId().equals(currentUser.getId())) {
            throw new IllegalStateException(
                    "You cannot claim your own report");
        }

        claim.setItemReport(item);
        claim.setClaimant(currentUser);
        claim.setStatus(ClaimStatus.PENDING);
        claim.setId(null);

        return claimRepository.save(claim);
    }

    @Override
    public List<Claim> viewAllClaims() {
        var currentUser = currentUserService.getCurrentUser();

        return claimRepository.findAll().stream()
                .filter(claim ->
                        claim.getClaimant().getId().equals(currentUser.getId())
                                || claim.getItemReport().getReportedBy()
                                .getId().equals(currentUser.getId()))
                .toList();
    }

    @Override
    public Optional<Claim> viewClaimById(Long id) {
        var currentUser = currentUserService.getCurrentUser();

        return claimRepository.findById(id)
                .filter(claim ->
                        claim.getClaimant().getId().equals(currentUser.getId())
                                || claim.getItemReport().getReportedBy()
                                .getId().equals(currentUser.getId()));
    }

    @Override
    public List<Claim> viewClaimsByStatus(ClaimStatus status) {
        var currentUser = currentUserService.getCurrentUser();

        return claimRepository.findByStatus(status).stream()
                .filter(claim ->
                        claim.getClaimant().getId().equals(currentUser.getId())
                                || claim.getItemReport().getReportedBy()
                                .getId().equals(currentUser.getId()))
                .toList();
    }

    @Transactional
    @Override
    public Claim updateClaimStatus(Long id, ClaimStatus status) {
        Claim existingClaim = claimRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Claim not found"));

        var currentUser = currentUserService.getCurrentUser();
        ItemReport item = existingClaim.getItemReport();

        if (!item.getReportedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(
                    "You cannot update claims on someone else's report");
        }

        if (existingClaim.getStatus() != ClaimStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending claims can be updated");
        }

        if (status == ClaimStatus.APPROVED) {
            if (item.getStatus() != ItemStatus.ACTIVE) {
                throw new IllegalStateException(
                        "This item is no longer available");
            }

            item.setStatus(ItemStatus.CLAIMED);
            itemReportRepository.save(item);
        }

        existingClaim.setStatus(status);
        return claimRepository.save(existingClaim);
    }

    @Override
    public void deleteClaim(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Claim not found"));

        var currentUser = currentUserService.getCurrentUser();

        boolean isClaimant = claim.getClaimant().getId()
                .equals(currentUser.getId());

        boolean isReporter = claim.getItemReport().getReportedBy()
                .getId().equals(currentUser.getId());

        if (!isClaimant && !isReporter) {
            throw new AccessDeniedException(
                    "You cannot delete this claim");
        }

        claimRepository.delete(claim);
    }
}
