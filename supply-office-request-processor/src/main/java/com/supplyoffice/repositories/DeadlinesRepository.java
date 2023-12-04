package com.supplyoffice.repositories;

import com.supplyoffice.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeadlinesRepository extends JpaRepository<Deadline, String> {

    @Query(value = "SELECT department_name, deadline FROM deadlines d WHERE d.department_name = :name", nativeQuery = true)
    Deadline findByName(@Param("name") String name);

    @Query(value = "UPDATE deadlines SET active = FALSE WHERE department_name = :name", nativeQuery = true)
    void setToFalseByName(@Param("name") String name);
}
