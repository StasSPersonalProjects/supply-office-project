package com.supplyoffice.repositories;

import com.supplyoffice.entities.SupplyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplyRequestsRepository extends JpaRepository<SupplyRequest, String> {

    @Query(value = "SELECT * FROM supply_requests WHERE department_name = :name;", nativeQuery = true)
    List<SupplyRequest> findByName(String name);
}
