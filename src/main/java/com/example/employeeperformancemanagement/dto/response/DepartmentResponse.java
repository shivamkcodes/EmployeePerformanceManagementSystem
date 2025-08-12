package com.example.employeeperformancemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String name;
    private Double budget;
}



