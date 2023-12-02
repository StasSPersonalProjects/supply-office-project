package com.supplyoffice.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "supply_requests")
public class SupplyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "department_name")
    private String departmentName;
    @Column(name = "item")
    private String item;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "measure_unit")
    private String measureUnit;
    @Column(name = "comments")
    private String comments;
    @Column(name = "deadline")
    private LocalDateTime deadline;

    public SupplyRequest(String departmentName, String item, int quantity, String measureUnit, String comments) {
        this.departmentName = departmentName;
        this.item = item;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.comments = comments;
    }

    public SupplyRequest() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
