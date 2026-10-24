
package com.vtcampus.lostandfound.domain.dto;

import com.vtcampus.lostandfound.domain.entities.ItemStatus;
import com.vtcampus.lostandfound.domain.entities.ItemType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemReportResponse {

    private Long id;
    private String title;
    private String description;
    private ItemType type;
    private String location;
    private LocalDate dateOccurred;
    private LocalDateTime createdAt;
    private String imageUrl;
    private ItemStatus status;

    private Long reportedById;
    private String reportedByName;
}
