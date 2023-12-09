package com.supplyoffice.repositories;

import com.supplyoffice.entities.SupplyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SupplyRequestsRepository extends JpaRepository<SupplyRequest, String> {

    @Modifying
    @Query(value = "UPDATE supply_requests SET deadline = :deadline WHERE department_name = :departmentName", nativeQuery = true)
    void updateDeadlineByName(@Param("departmentName") String departmentName, @Param("deadline") LocalDateTime deadline);

    @Query(value = "SELECT * FROM supply_requests s WHERE s.department_name = :name", nativeQuery = true)
    List<SupplyRequest> findAllByName(String name);
}
