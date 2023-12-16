package com.supplyoffice.repository;

import com.supplyoffice.entities.ExcelFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredRequestsRepository extends JpaRepository<ExcelFile, Long> {
}
