package com.supplyoffice.service;

import com.supplyoffice.dto.DepartmentDTO;
import com.supplyoffice.dto.NewDepartmentDTO;

import java.util.List;

public interface DepartmentsManagerService {

    List<NewDepartmentDTO> getAllDepartments();

    String addDepartment(NewDepartmentDTO departmentDTO);

    NewDepartmentDTO updateDepartment(DepartmentDTO departmentDTO);

    String removeDepartment(int id);

    boolean existsByName(String name);
}
