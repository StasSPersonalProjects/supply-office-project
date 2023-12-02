package com.supplyoffice.repositories;

import com.supplyoffice.entities.SupplyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyRequestsRepository extends JpaRepository<SupplyRequest, String> {
}
