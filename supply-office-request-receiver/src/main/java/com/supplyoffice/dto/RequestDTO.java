package com.supplyoffice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class RequestDTO {

    @NotBlank(message = "Department name field cannot be blank.")
    private String departmentName;
    @NotBlank(message = "Item field cannot be blank.")
    private String item;
    @Min(value = 1, message = "Quantity must be greater than 0.")
    private int quantity;
//    @Pattern(regexp = "\b(units|kg|gr)\b")
    private String measureUnit;
    private String comments;
    private LocalDateTime deadline;

    public RequestDTO(String departmentName, String item, int quantity, String measureUnit, String comments, LocalDateTime deadline) {
        this.departmentName = departmentName;
        this.item = item;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.comments = comments;
        this.deadline = deadline;
    }

    public RequestDTO() {
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
