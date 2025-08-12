package com.example.employeeperformancemanagement.controller;

import com.example.employeeperformancemanagement.dto.response.EmployeeProjectResponse;
import com.example.employeeperformancemanagement.model.Employee;
import com.example.employeeperformancemanagement.model.EmployeeProject;
import com.example.employeeperformancemanagement.model.Project;
import com.example.employeeperformancemanagement.service.EmployeeProjectService;
import com.example.employeeperformancemanagement.service.EmployeeService;
import com.example.employeeperformancemanagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/employee-projects")
public class EmployeeProjectController {

    @Autowired
    private EmployeeProjectService employeeProjectService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;

    @PutMapping("/employees/{employeeId}/projects/{projectId}")
    public ResponseEntity<EmployeeProjectResponse> assignEmployeeToProject(
            @PathVariable Long employeeId,
            @PathVariable Long projectId
    ) {
        Employee employee = employeeService.getEmployeeDetails(employeeId);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        Project project = projectService.getProjectById(projectId);

        EmployeeProject assignment = new EmployeeProject();
        assignment.setEmployee(employee);
        assignment.setProject(project);
        assignment.setAssignedDate(LocalDate.now());
        assignment.setRole("Member");

        employeeProjectService.createEmployeeProject(assignment);

        EmployeeProjectResponse body = new EmployeeProjectResponse(
                employeeId,
                projectId,
                assignment.getAssignedDate(),
                assignment.getRole()
        );

        return ResponseEntity.created(URI.create("/employee-projects/employees/" + employeeId + "/projects/" + projectId))
                .body(body);
    }
}


