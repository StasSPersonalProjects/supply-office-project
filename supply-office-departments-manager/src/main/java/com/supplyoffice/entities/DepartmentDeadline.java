package com.supplyoffice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "deadlines")
public class DepartmentDeadline {

    @Id
    @Column(name = "department_name")
    private String departmentName;
    @Column(name = "deadline")
    private LocalDateTime deadline;

    public DepartmentDeadline(String departmentName) {
        this.departmentName = departmentName;
    }

    public DepartmentDeadline() {
    }

    public static DepartmentDeadline of(Department department) {
        return new DepartmentDeadline(department.getName());
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
