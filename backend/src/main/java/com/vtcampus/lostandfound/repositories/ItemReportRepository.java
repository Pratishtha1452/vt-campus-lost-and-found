package com.vtcampus.lostandfound.repositories;

import com.vtcampus.lostandfound.domain.entities.ItemReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemReportRepository extends JpaRepository<ItemReport, Long> {
}
