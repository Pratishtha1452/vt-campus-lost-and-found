
package com.vtcampus.lostandfound.controller;

import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemType;
import com.vtcampus.lostandfound.services.ItemReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ItemReportController {

    private final ItemReportService itemReportService;

    public ItemReportController(ItemReportService itemReportService) {
        this.itemReportService = itemReportService;
    }

    @PostMapping
    public ResponseEntity<ItemReport> createReport(
            @RequestBody ItemReport itemReport) {

        ItemReport createdReport =
                itemReportService.createReport(itemReport);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdReport);
    }

    @GetMapping
    public ResponseEntity<List<ItemReport>> getAllReports() {
        return ResponseEntity.ok(itemReportService.viewAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemReport> getReportById(
            @PathVariable Long id) {

        return itemReportService.viewOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ItemReport>> getReportsByType(
            @PathVariable ItemType type) {

        return ResponseEntity.ok(
                itemReportService.filterByType(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemReport> updateReport(
            @PathVariable Long id,
            @RequestBody ItemReport itemReport) {

        return ResponseEntity.ok(
                itemReportService.updateItemReport(id, itemReport));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @PathVariable Long id) {

        itemReportService.deleteItemReport(id);
        return ResponseEntity.noContent().build();
    }
}
