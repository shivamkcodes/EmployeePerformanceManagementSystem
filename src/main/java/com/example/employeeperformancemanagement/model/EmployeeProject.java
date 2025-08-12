package com.example.employeeperformancemanagement.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(indexes = {
        @Index(name = "idx_employee_project_employee_id", columnList = "employee_id")
})
@Getter
@Setter
@ToString(exclude = {"employee", "project"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeProject {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private EmployeeProjectId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDate assignedDate;
    private String role;
}



