package com.supplyoffice.dto;

import com.supplyoffice.entities.Deadline;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class DeadlineDTO {

    @NotBlank(message = "Department name cannot be blank.")
    private String departmentName;
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

    public static DeadlineDTO of(String newDepartmentDTO) {
        return new DeadlineDTO(newDepartmentDTO, null);
    }

    public static DeadlineDTO of(Deadline deadline) {
        return new DeadlineDTO(deadline.getDepartmentName(), deadline.getDeadline());
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
