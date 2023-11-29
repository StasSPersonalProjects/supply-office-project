package com.supplyoffice.service;

import com.supplyoffice.dto.DepartmentDTO;
import com.supplyoffice.dto.DepartmentRequestDTO;

import java.util.List;

public interface DepartmentsManagerService {

    List<DepartmentRequestDTO> getAllDepartments();

    String addDepartment(DepartmentRequestDTO departmentDTO);

    DepartmentRequestDTO updateDepartment(DepartmentDTO departmentDTO);

    String removeDepartment(int id);
}
