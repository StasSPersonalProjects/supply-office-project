package com.supplyoffice.dto;

import com.supplyoffice.entities.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class DepartmentDTO {

    private int id;
    @NotBlank(message = "Department name cannot be blank.")
    private String name;
    @NotBlank(message = "The department must have a manager.")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "The string must match a person's name pattern.")
    private String manager;
    private String description;

    public DepartmentDTO(int id, String name, String manager, String description) {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.description = description;
    }

    public DepartmentDTO() {
    }

    public static DepartmentDTO of(Department department) {
        return new DepartmentDTO(department.getId(), department.getName(), department.getManager(), department.getDescription());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        DepartmentDTO that = (DepartmentDTO) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(manager, that.manager) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, manager, description);
    }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manager='" + manager + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
