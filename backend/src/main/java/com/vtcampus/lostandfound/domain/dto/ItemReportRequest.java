
package com.vtcampus.lostandfound.domain.dto;

import com.vtcampus.lostandfound.domain.entities.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemReportRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000)
    private String description;

    @NotNull(message = "Report type is required")
    private ItemType type;

    @NotBlank(message = "Location is required")
    @Size(max = 200)
    private String location;

    @NotNull(message = "Date occurred is required")
    private LocalDate dateOccurred;

    private String imageUrl;

    @NotNull(message = "Reporter ID is required")
    private Long reportedById;
}
