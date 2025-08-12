package com.example.employeeperformancemanagement.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCreateRequest {
    private String name;
    private String email;
    private LocalDate dateOfJoining;
    private Double salary;
    private Long departmentId;
    private Long managerId;
}



