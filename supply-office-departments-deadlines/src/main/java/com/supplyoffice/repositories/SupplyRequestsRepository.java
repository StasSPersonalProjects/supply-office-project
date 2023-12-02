package com.supplyoffice.repositories;

import com.supplyoffice.entities.SupplyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface SupplyRequestsRepository extends JpaRepository<SupplyRequest, String> {

    @Modifying
    @Query(value = "UPDATE supply_requests SET deadline = :deadline WHERE department_name = :departmentName", nativeQuery = true)
    void updateDeadlineByName(@Param("departmentName") String departmentName, @Param("deadline") LocalDateTime deadline);

}
