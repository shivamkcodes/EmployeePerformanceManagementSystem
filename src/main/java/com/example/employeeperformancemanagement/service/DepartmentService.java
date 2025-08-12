package com.example.employeeperformancemanagement.service;

import com.example.employeeperformancemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeeperformancemanagement.model.Department;

public interface DepartmentService {
    Department createDepartment(Department department);
    Department getDepartmentById(Long id);
    Department createDepartment(DepartmentCreateRequest request);
}


