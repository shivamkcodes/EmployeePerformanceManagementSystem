package com.example.employeeperformancemanagement.controller;

import com.example.employeeperformancemanagement.dto.request.ProjectCreateRequest;
import com.example.employeeperformancemanagement.dto.response.ProjectResponse;
import com.example.employeeperformancemanagement.model.Project;
import com.example.employeeperformancemanagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/departments/{departmentId}")
    public ResponseEntity<ProjectResponse> createProject(
            @PathVariable Long departmentId,
            @RequestBody ProjectCreateRequest request
    ) {
        Project saved = projectService.createProjectUnderDepartment(departmentId, request);
        return ResponseEntity.created(URI.create("/projects/" + saved.getId())).body(
                new ProjectResponse(
                        saved.getId(), saved.getName(), saved.getDescription(), saved.getStartDate(), saved.getEndDate(),
                        saved.getDepartment() != null ? saved.getDepartment().getId() : null
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        Project p = projectService.getProjectById(id);
        return ResponseEntity.ok(new ProjectResponse(
                p.getId(), p.getName(), p.getDescription(), p.getStartDate(), p.getEndDate(),
                p.getDepartment() != null ? p.getDepartment().getId() : null
        ));
    }
}


