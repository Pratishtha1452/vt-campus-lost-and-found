package com.vtcampus.lostandfound.repositories;

import com.vtcampus.lostandfound.domain.entities.ItemReport;
import com.vtcampus.lostandfound.domain.entities.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemReportRepository extends JpaRepository<ItemReport, Long> {
    List<ItemReport> findByType(ItemType itemType);
}
