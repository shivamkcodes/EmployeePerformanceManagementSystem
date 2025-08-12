package com.example.employeeperformancemanagement.service;

import com.example.employeeperformancemanagement.dto.request.ProjectCreateRequest;
import com.example.employeeperformancemanagement.model.Project;

public interface ProjectService {
    Project createProject(Project project);
    Project getProjectById(Long id);
    Project createProjectUnderDepartment(Long departmentId, ProjectCreateRequest request);
}


