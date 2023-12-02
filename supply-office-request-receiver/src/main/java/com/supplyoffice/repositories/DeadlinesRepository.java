package com.supplyoffice.repositories;

import com.supplyoffice.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface DeadlinesRepository extends JpaRepository<Deadline, String> {

    @Query(value = "SELECT deadline FROM deadlines d WHERE d.department_name = :name", nativeQuery = true)
    LocalDateTime findDeadlineByName(@Param("name") String name);
}
