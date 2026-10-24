package com.vtcampus.lostandfound.services.impl;

import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemStatus;
import com.vtcampus.lostandfound.domain.entities.ItemType;
import com.vtcampus.lostandfound.repositories.ItemReportRepository;
import com.vtcampus.lostandfound.services.ItemReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemReportServiceImpl implements ItemReportService {

    private final ItemReportRepository itemReportRepository;

    public ItemReportServiceImpl(ItemReportRepository itemReportRepository){
        this.itemReportRepository = itemReportRepository;
    }

    @Override
    public ItemReport createReport(ItemReport itemReport) {
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
        ItemReport existingReport = itemReportRepository.findById(id).orElseThrow();
        if (existingReport.getStatus() != ItemStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only active reports can be updated"
            );
        }
        existingReport.setTitle(itemReport.getTitle());
        existingReport.setDescription(itemReport.getDescription());
        existingReport.setLocation(itemReport.getLocation());
        existingReport.setDateOccurred(itemReport.getDateOccurred());
        existingReport.setType(itemReport.getType());
        existingReport.setImageUrl(itemReport.getImageUrl());

        return itemReportRepository.save(existingReport);
    }

    @Override
    public void deleteItemReport(Long id) {
        itemReportRepository.deleteById(id);
    }
}
