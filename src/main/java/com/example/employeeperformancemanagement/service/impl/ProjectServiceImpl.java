package com.example.employeeperformancemanagement.service.impl;

import com.example.employeeperformancemanagement.dto.request.ProjectCreateRequest;
import com.example.employeeperformancemanagement.model.Department;
import com.example.employeeperformancemanagement.model.Project;
import com.example.employeeperformancemanagement.repository.ProjectRepository;
import com.example.employeeperformancemanagement.service.DepartmentService;
import com.example.employeeperformancemanagement.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    @Override
    public Project createProjectUnderDepartment(Long departmentId, ProjectCreateRequest request) {
        Department department = departmentService.getDepartmentById(departmentId);
        Project project = modelMapper.map(request, Project.class);
        project.setDepartment(department);
        return projectRepository.save(project);
    }
}


