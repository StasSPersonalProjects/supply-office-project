package com.supplyoffice.entities;

import com.supplyoffice.dto.DepartmentDTO;
import com.supplyoffice.dto.DepartmentRequestDTO;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "manager")
    private String manager;
    @Column(name = "description")
    private String description;

    public Department(String name, String manager, String description) {
        this.name = name;
        this.manager = manager;
        this.description = description;
    }

    public Department(int id, String name, String manager, String description) {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.description = description;
    }

    public Department() {
    }

    public static Department of(DepartmentRequestDTO department) {
        return new Department(department.getName(), department.getManager(), department.getDescription());
    }

    public static Department of(DepartmentDTO department) {
        return new Department(department.getName(), department.getManager(), department.getDescription());
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
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manager='" + manager + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(manager, that.manager) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, manager, description);
    }
}
