
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
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Report type is required")
    private ItemType type;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    @NotNull(message = "Date occurred is required")
    private LocalDate dateOccurred;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
}
