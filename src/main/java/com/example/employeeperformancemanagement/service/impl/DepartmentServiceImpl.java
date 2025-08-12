package com.example.employeeperformancemanagement.service.impl;

import com.example.employeeperformancemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeeperformancemanagement.model.Department;
import com.example.employeeperformancemanagement.repository.DepartmentRepository;
import com.example.employeeperformancemanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department createDepartment(DepartmentCreateRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setBudget(request.getBudget());
        return departmentRepository.save(department);
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
    }
}


