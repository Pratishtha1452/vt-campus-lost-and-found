
package com.vtcampus.lostandfound.domain.dto;

import com.vtcampus.lostandfound.domain.entities.ClaimStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaimResponse {

    private Long id;

    private Long itemReportId;
    private String itemTitle;

    private Long claimantId;
    private String claimantName;

    private String message;
    private ClaimStatus status;
    private LocalDateTime createdAt;
}
