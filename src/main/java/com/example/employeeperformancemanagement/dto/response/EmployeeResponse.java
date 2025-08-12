package com.example.employeeperformancemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmployeeResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDate dateOfJoining;
    private Double salary;

    private DepartmentSummary department;
    private List<ProjectSummary> projects;
    private List<PerformanceReviewResponse> reviews;
}



