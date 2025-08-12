package com.example.employeeperformancemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeListItemResponse {
    private Long id;
    private String name;
    private String email;
    private DepartmentSummary department;
    private List<ProjectSummary> projects;
}



