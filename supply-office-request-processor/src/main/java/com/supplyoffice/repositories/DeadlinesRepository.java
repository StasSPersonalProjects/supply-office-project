package com.supplyoffice.repositories;

import com.supplyoffice.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DeadlinesRepository extends JpaRepository<Deadline, String> {

    @Modifying
    @Query(value = "UPDATE deadlines SET active = 0 WHERE department_name = :name", nativeQuery = true)
    void setToFalseByName(String name);
}
