package com.example.employeeperformancemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EmployeeProjectResponse {
    private Long employeeId;
    private Long projectId;
    private LocalDate assignedDate;
    private String role;
}


