
package com.vtcampus.lostandfound.controller;

import com.vtcampus.lostandfound.domain.dto.ItemReportRequest;
import com.vtcampus.lostandfound.domain.dto.ItemReportResponse;
import com.vtcampus.lostandfound.domain.entities.ItemType;
import com.vtcampus.lostandfound.mappers.ItemReportMapper;
import com.vtcampus.lostandfound.services.ItemReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ItemReportController {

    private final ItemReportService itemReportService;
    private final ItemReportMapper itemReportMapper;

    public ItemReportController(
            ItemReportService itemReportService,
            ItemReportMapper itemReportMapper) {
        this.itemReportService = itemReportService;
        this.itemReportMapper = itemReportMapper;
    }

    @PostMapping
    public ResponseEntity<ItemReportResponse> createReport(
            @Valid @RequestBody ItemReportRequest request) {

        var report = itemReportMapper.toEntity(request);
        var savedReport = itemReportService.createReport(report);
        var response = itemReportMapper.toResponse(savedReport);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ItemReportResponse>> getAllReports() {
        List<ItemReportResponse> responses = itemReportService.viewAll()
                .stream()
                .map(itemReportMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemReportResponse> getReportById(
            @PathVariable Long id) {

        return itemReportService.viewOne(id)
                .map(itemReportMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ItemReportResponse>> getReportsByType(
            @PathVariable ItemType type) {

        List<ItemReportResponse> responses = itemReportService.filterByType(type)
                .stream()
                .map(itemReportMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemReportResponse> updateReport(
            @PathVariable Long id,
            @Valid @RequestBody ItemReportRequest request) {

        var report = itemReportMapper.toEntity(request);
        var updatedReport = itemReportService.updateItemReport(id, report);
        var response = itemReportMapper.toResponse(updatedReport);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        itemReportService.deleteItemReport(id);
        return ResponseEntity.noContent().build();
    }
}
