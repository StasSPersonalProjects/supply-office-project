package com.supplyoffice.entities;

import com.supplyoffice.dto.DeadlineDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "deadlines")
public class Deadline {

    @Id
    @Column(name = "department_name")
    private String departmentName;
    @Column(name = "deadline")
    private LocalDateTime deadline;
    @Column(name = "active")
    private boolean active;

    public Deadline(String departmentName, LocalDateTime deadline, boolean active) {
        this.departmentName = departmentName;
        this.deadline = deadline;
        this.active = active;
    }

    public Deadline(String departmentName) {
        this.departmentName = departmentName;
    }

    public Deadline() {
    }

    public static Deadline of(DeadlineDTO deadlineDTO, boolean active) {
        return new Deadline(deadlineDTO.getDepartmentName(), deadlineDTO.getDeadline(), active);
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
