package com.vtcampus.lostandfound.services.impl;

import com.vtcampus.lostandfound.domain.entities.Claim;
import com.vtcampus.lostandfound.domain.entities.ClaimStatus;
import com.vtcampus.lostandfound.repositories.ClaimRepository;
import com.vtcampus.lostandfound.repositories.ItemReportRepository;
import com.vtcampus.lostandfound.repositories.UsersRepository;
import com.vtcampus.lostandfound.services.ClaimService;
import org.springframework.stereotype.Service;
import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemStatus;
import com.vtcampus.lostandfound.repositories.ItemReportRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final ItemReportRepository itemReportRepository;
    private final UsersRepository usersRepository;

    public ClaimServiceImpl(
            ClaimRepository claimRepository,
            ItemReportRepository itemReportRepository, UsersRepository usersRepository) {
        this.claimRepository = claimRepository;
        this.itemReportRepository = itemReportRepository;
        this.usersRepository = usersRepository;
    }
    @Override
    public Claim createClaim(Claim claim) {
        Long itemReportId = claim.getItemReport().getId();
        Long claimantId = claim.getClaimant().getId();

        ItemReport item = itemReportRepository.findById(itemReportId)
                .orElseThrow(() -> new RuntimeException("Item report not found"));

        if (item.getStatus() != ItemStatus.ACTIVE) {
            throw new IllegalStateException("This item is no longer available");
        }

        var claimant = usersRepository.findById(claimantId)
                .orElseThrow(() -> new RuntimeException("Claimant not found"));

        claim.setItemReport(item);
        claim.setClaimant(claimant);
        claim.setStatus(ClaimStatus.PENDING);

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

    @Transactional
    @Override
    public Claim updateClaimStatus(Long id, ClaimStatus status) {
        Claim existingClaim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        if (existingClaim.getStatus() != ClaimStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending claims can be updated"
            );
        }

        ItemReport item = existingClaim.getItemReport();

        if (status == ClaimStatus.APPROVED) {
            if (item.getStatus() != ItemStatus.ACTIVE) {
                throw new IllegalStateException(
                        "This item is no longer available"
                );
            }

            item.setStatus(ItemStatus.CLAIMED);
            itemReportRepository.save(item);
        }

        existingClaim.setStatus(status);
        return claimRepository.save(existingClaim);
    }

    @Override
    public void deleteClaim(Long id) {
        claimRepository.deleteById(id);
    }
}
