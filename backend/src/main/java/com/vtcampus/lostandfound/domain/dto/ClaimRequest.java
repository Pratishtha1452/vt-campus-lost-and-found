
package com.vtcampus.lostandfound.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClaimRequest {

    @NotNull(message = "Item report ID is required")
    private Long itemReportId;

    @NotBlank(message = "Claim message is required")
    @Size(
            max = 1000,
            message = "Claim message must not exceed 1000 characters"
    )
    private String message;
}
