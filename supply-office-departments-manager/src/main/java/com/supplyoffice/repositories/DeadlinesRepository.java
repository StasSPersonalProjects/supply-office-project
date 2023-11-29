package com.supplyoffice.repositories;

import com.supplyoffice.entities.DepartmentDeadline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadlinesRepository extends JpaRepository<DepartmentDeadline, Integer> {


}
