package com.supplyoffice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class DeadlineDTO {

    @NotBlank(message = "Department name cannot be blank.")
    private String departmentName;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$\n", message = "Date and time wrong format.")
    private LocalDateTime deadline;

    public DeadlineDTO(String departmentName, LocalDateTime deadline) {
        this.departmentName = departmentName;
        this.deadline = deadline;
    }

    public DeadlineDTO(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public DeadlineDTO() {
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

    @Override
    public String toString() {
        return "DeadlineDTO{" +
                "departmentName='" + departmentName + '\'' +
                ", deadline=" + deadline +
                '}';
    }
}
