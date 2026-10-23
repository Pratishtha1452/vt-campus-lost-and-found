
package com.vtcampus.lostandfound.services.impl;

import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemType;
import com.vtcampus.lostandfound.repositories.ItemReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemReportServiceImplTest {

    @Mock
    private ItemReportRepository itemReportRepository;

    @InjectMocks
    private ItemReportServiceImpl itemReportService;

    private ItemReport report;

    @BeforeEach
    void setUp() {
        report = ItemReport.builder()
                .id(1L)
                .title("Lost wallet")
                .description("Black leather wallet")
                .type(ItemType.LOST)
                .location("College library")
                .dateOccurred(LocalDate.of(2026, 10, 20))
                .build();
    }

    @Test
    void createReportShouldSaveAndReturnReport() {
        when(itemReportRepository.save(report)).thenReturn(report);

        ItemReport result = itemReportService.createReport(report);

        assertEquals(report, result);
        verify(itemReportRepository).save(report);
    }

    @Test
    void viewAllShouldReturnAllReports() {
        when(itemReportRepository.findAll()).thenReturn(List.of(report));

        List<ItemReport> result = itemReportService.viewAll();

        assertEquals(1, result.size());
        assertEquals(report, result.get(0));
    }

    @Test
    void viewOneShouldReturnReportWhenFound() {
        when(itemReportRepository.findById(1L))
                .thenReturn(Optional.of(report));

        Optional<ItemReport> result = itemReportService.viewOne(1L);

        assertTrue(result.isPresent());
        assertEquals(report, result.get());
    }

    @Test
    void filterByTypeShouldReturnMatchingReports() {
        when(itemReportRepository.findByType(ItemType.LOST))
                .thenReturn(List.of(report));

        List<ItemReport> result =
                itemReportService.filterByType(ItemType.LOST);

        assertEquals(1, result.size());
        assertEquals(ItemType.LOST, result.get(0).getType());
    }

    @Test
    void updateItemReportShouldUpdateExistingReport() {
        when(itemReportRepository.findById(1L))
                .thenReturn(Optional.of(report));
        when(itemReportRepository.save(any(ItemReport.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ItemReport updatedReport = ItemReport.builder()
                .title("Lost phone")
                .description("Black mobile phone")
                .type(ItemType.FOUND)
                .location("College canteen")
                .dateOccurred(LocalDate.of(2026, 10, 21))
                .imageUrl("phone.jpg")
                .build();

        ItemReport result =
                itemReportService.updateItemReport(1L, updatedReport);

        assertEquals("Lost phone", result.getTitle());
        assertEquals("Black mobile phone", result.getDescription());
        assertEquals(ItemType.FOUND, result.getType());
        assertEquals("College canteen", result.getLocation());
        assertEquals("phone.jpg", result.getImageUrl());

        verify(itemReportRepository).save(report);
    }

    @Test
    void deleteItemReportShouldCallRepository() {
        itemReportService.deleteItemReport(1L);

        verify(itemReportRepository).deleteById(1L);
    }
}
