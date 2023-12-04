package com.supplyoffice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class UpdateRequestDTO {

    @NotNull(message = "Department ID must not be null.")
    private long id;
    @NotBlank(message = "Department name field cannot be blank.")
    private String departmentName;
    @NotBlank(message = "Item field cannot be blank.")
    private String item;
    @Min(value = 1, message = "Quantity must be greater than 0.")
    private int quantity;
    @Pattern(regexp = "\\b(kg|gr|units|packs)\\b", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Measure unit must be kg/gr/units/packs.")
    private String measureUnit;
    private String comments;
    private LocalDateTime deadline;

    public UpdateRequestDTO(long id, String departmentName, String item, int quantity, String measureUnit, String comments, LocalDateTime deadline) {
        this.id = id;
        this.departmentName = departmentName;
        this.item = item;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.comments = comments;
        this.deadline = deadline;
    }

    public UpdateRequestDTO() {
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
