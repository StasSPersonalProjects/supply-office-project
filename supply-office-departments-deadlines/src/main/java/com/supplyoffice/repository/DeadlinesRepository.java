package com.supplyoffice.repository;

import com.supplyoffice.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DeadlinesRepository extends JpaRepository<Deadline, String> {

    @Query(value = "SELECT * FROM deadlines d WHERE d.department_name = :name", nativeQuery = true)
    Deadline findByName(@Param("name") String name);

    @Query(value = "SELECT deadline FROM deadlines d WHERE d.department_name = :name", nativeQuery = true)
    LocalDateTime findDeadlineByName(@Param("name") String name);

    @Query(value = "UPDATE deadlines SET active = :status WHERE department_name = :name", nativeQuery = true)
    @Modifying
    void updateStatusByName(@Param("name") String name, @Param("status") int status);

    @Query(value = "SELECT * FROM deadlines d WHERE d.active = 1", nativeQuery = true)
    List<Deadline> findAllWhereActiveTrue();
}
