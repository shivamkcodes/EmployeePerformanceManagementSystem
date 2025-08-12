package com.example.employeeperformancemanagement.controller;

import com.example.employeeperformancemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeeperformancemanagement.dto.response.DepartmentResponse;
import com.example.employeeperformancemanagement.model.Department;
import com.example.employeeperformancemanagement.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentCreateRequest request) {
        Department saved = departmentService.createDepartment(request);
        return ResponseEntity.ok(new DepartmentResponse(saved.getId(), saved.getName(), saved.getBudget()));
    }
}


