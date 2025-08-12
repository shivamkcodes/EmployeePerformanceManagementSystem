package com.example.employeeperformancemanagement.service.impl;

import com.example.employeeperformancemanagement.model.EmployeeProject;
import com.example.employeeperformancemanagement.model.EmployeeProjectId;
import com.example.employeeperformancemanagement.repository.EmployeeProjectRepository;
import com.example.employeeperformancemanagement.service.EmployeeProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeProjectServiceImpl implements EmployeeProjectService {

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Override
    public EmployeeProject createEmployeeProject(EmployeeProject employeeProject) {
        EmployeeProjectId id = new EmployeeProjectId();
        id.setEmployeeId(employeeProject.getEmployee().getId());
        id.setProjectId(employeeProject.getProject().getId());
        employeeProject.setId(id);
        return employeeProjectRepository.save(employeeProject);
    }
}

