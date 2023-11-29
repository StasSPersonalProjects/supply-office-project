package com.supplyoffice.dto;

import com.supplyoffice.entities.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class DepartmentRequestDTO {

    @NotBlank(message = "Department name cannot be blank.")
    private String name;
    @NotBlank(message = "The department must have a manager.")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "The string must match a person's name pattern.")
    private String manager;
    private String description;

    public DepartmentRequestDTO(String name, String manager, String description) {
        this.name = name;
        this.manager = manager;
        this.description = description;
    }

    public DepartmentRequestDTO() {
    }

    public static DepartmentRequestDTO of(Department department) {
        return new DepartmentRequestDTO(department.getName(), department.getManager(), department.getDescription());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentRequestDTO that = (DepartmentRequestDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(manager, that.manager) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, manager, description);
    }

    @Override
    public String toString() {
        return "DepartmentRequestDTO{" +
                "name='" + name + '\'' +
                ", manager='" + manager + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
