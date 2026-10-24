
package com.vtcampus.lostandfound.mappers;

import com.vtcampus.lostandfound.domain.dto.ClaimRequest;
import com.vtcampus.lostandfound.domain.dto.ClaimResponse;
import com.vtcampus.lostandfound.domain.entities.Claim;
import com.vtcampus.lostandfound.domain.entities.ItemReport;
import org.springframework.stereotype.Component;

@Component
public class ClaimMapper {

    public Claim toEntity(ClaimRequest request) {
        Claim claim = new Claim();

        claim.setMessage(request.getMessage());

        // Set the item reference; the service verifies it exists.
        ItemReport itemReport = new ItemReport();
        itemReport.setId(request.getItemReportId());
        claim.setItemReport(itemReport);

        // The service assigns the authenticated user as claimant.
        return claim;
    }

    public ClaimResponse toResponse(Claim claim) {
        ClaimResponse response = new ClaimResponse();

        response.setId(claim.getId());
        response.setMessage(claim.getMessage());
        response.setStatus(claim.getStatus());
        response.setCreatedAt(claim.getCreatedAt());

        if (claim.getItemReport() != null) {
            response.setItemReportId(claim.getItemReport().getId());
            response.setItemTitle(claim.getItemReport().getTitle());
        }

        if (claim.getClaimant() != null) {
            response.setClaimantId(claim.getClaimant().getId());
            response.setClaimantName(claim.getClaimant().getName());
        }

        return response;
    }
}
