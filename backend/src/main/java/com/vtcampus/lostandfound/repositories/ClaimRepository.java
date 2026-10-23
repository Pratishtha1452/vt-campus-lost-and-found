package com.vtcampus.lostandfound.repositories;

import com.vtcampus.lostandfound.domain.entities.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}
