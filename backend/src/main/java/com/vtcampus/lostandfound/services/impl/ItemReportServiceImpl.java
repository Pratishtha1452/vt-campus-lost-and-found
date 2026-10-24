
package com.vtcampus.lostandfound.services.impl;

import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemStatus;
import com.vtcampus.lostandfound.domain.entities.ItemType;
import com.vtcampus.lostandfound.repositories.ItemReportRepository;
import com.vtcampus.lostandfound.services.CurrentUserService;
import com.vtcampus.lostandfound.services.ItemReportService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemReportServiceImpl implements ItemReportService {

    private final ItemReportRepository itemReportRepository;
    private final CurrentUserService currentUserService;

    public ItemReportServiceImpl(
            ItemReportRepository itemReportRepository,
            CurrentUserService currentUserService) {
        this.itemReportRepository = itemReportRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public ItemReport createReport(ItemReport itemReport) {
        var reporter = currentUserService.getCurrentUser();

        itemReport.setId(null);
        itemReport.setReportedBy(reporter);
        itemReport.setStatus(ItemStatus.ACTIVE);

        return itemReportRepository.save(itemReport);
    }

    @Override
    public List<ItemReport> viewAll() {
        return itemReportRepository.findAll();
    }

    @Override
    public Optional<ItemReport> viewOne(Long id) {
        return itemReportRepository.findById(id);
    }

    @Override
    public List<ItemReport> filterByType(ItemType itemType) {
        return itemReportRepository.findByType(itemType);
    }

    @Override
    public ItemReport updateItemReport(Long id, ItemReport itemReport) {
        ItemReport existing = itemReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        var currentUser = currentUserService.getCurrentUser();

        if (!existing.getReportedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You cannot edit this report");
        }

        if (existing.getStatus() != ItemStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only active reports can be updated");
        }

        existing.setTitle(itemReport.getTitle());
        existing.setDescription(itemReport.getDescription());
        existing.setLocation(itemReport.getLocation());
        existing.setDateOccurred(itemReport.getDateOccurred());
        existing.setType(itemReport.getType());
        existing.setImageUrl(itemReport.getImageUrl());

        return itemReportRepository.save(existing);
    }

    @Override
    public void deleteItemReport(Long id) {
        ItemReport existing = itemReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        var currentUser = currentUserService.getCurrentUser();

        if (!existing.getReportedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You cannot delete this report");
        }

        itemReportRepository.delete(existing);
    }
}
