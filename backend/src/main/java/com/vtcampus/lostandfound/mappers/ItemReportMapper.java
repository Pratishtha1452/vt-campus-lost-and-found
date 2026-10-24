
package com.vtcampus.lostandfound.mappers;

import com.vtcampus.lostandfound.domain.dto.ItemReportRequest;
import com.vtcampus.lostandfound.domain.dto.ItemReportResponse;
import com.vtcampus.lostandfound.domain.entities.ItemReport;
import org.springframework.stereotype.Component;

@Component
public class ItemReportMapper {

    public ItemReport toEntity(ItemReportRequest request) {
        ItemReport report = new ItemReport();

        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setType(request.getType());
        report.setLocation(request.getLocation());
        report.setDateOccurred(request.getDateOccurred());
        report.setImageUrl(request.getImageUrl());

        // The service assigns the authenticated user as the reporter.
        return report;
    }

    public ItemReportResponse toResponse(ItemReport report) {
        ItemReportResponse response = new ItemReportResponse();

        response.setId(report.getId());
        response.setTitle(report.getTitle());
        response.setDescription(report.getDescription());
        response.setType(report.getType());
        response.setLocation(report.getLocation());
        response.setDateOccurred(report.getDateOccurred());
        response.setCreatedAt(report.getCreatedAt());
        response.setImageUrl(report.getImageUrl());
        response.setStatus(report.getStatus());

        if (report.getReportedBy() != null) {
            response.setReportedById(report.getReportedBy().getId());
            response.setReportedByName(report.getReportedBy().getName());
        }

        return response;
    }
}
