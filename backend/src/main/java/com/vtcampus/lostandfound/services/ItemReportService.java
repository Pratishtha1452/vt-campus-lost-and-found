package com.vtcampus.lostandfound.services;

import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemType;

import java.util.List;
import java.util.Optional;

public interface ItemReportService {

    //creaete a item report
    ItemReport createReport(ItemReport itemReport);

    //view Reports
    List<ItemReport> viewAll();

    //view one report
    Optional<ItemReport> viewOne(Long id);

    //filter reports
    List<ItemReport> filterByType(ItemType type);


    //updateReport
    ItemReport updateItemReport(Long id, ItemReport itemReport);

    //deleteReport
    void deleteItemReport(Long id);
}
