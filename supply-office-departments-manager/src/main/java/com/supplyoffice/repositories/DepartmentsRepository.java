package com.supplyoffice.repositories;

import com.supplyoffice.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentsRepository extends JpaRepository<Department, Integer> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM departments d WHERE d.name = :name",
            nativeQuery = true)
    Long existsByName(@Param("name") String name);

}
