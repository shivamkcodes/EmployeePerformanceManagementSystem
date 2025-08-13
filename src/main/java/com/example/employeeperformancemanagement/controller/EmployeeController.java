package com.example.employeeperformancemanagement.controller;

import com.example.employeeperformancemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeeperformancemanagement.dto.response.DepartmentSummary;
import com.example.employeeperformancemanagement.dto.response.EmployeeListItemResponse;
import com.example.employeeperformancemanagement.dto.response.EmployeeResponse;
import com.example.employeeperformancemanagement.dto.response.ProjectSummary;
import com.example.employeeperformancemanagement.dto.response.PageResponse;
import com.example.employeeperformancemanagement.model.Employee;
import com.example.employeeperformancemanagement.model.EmployeeProject;
import com.example.employeeperformancemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<PageResponse<EmployeeListItemResponse>> getEmployees(
            @RequestParam(value = "reviewDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewDate,
            @RequestParam(value = "score", required = false) Integer score,
            @RequestParam(value = "departments", required = false) List<String> departments,
            @RequestParam(value = "projects", required = false) List<String> projects,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        if (score != null && reviewDate == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "score filter requires reviewDate");
        }

        // Support CSV values in a single param instance while also supporting repeated params
        List<String> departmentContains = departments == null ? null : departments.stream()
                .flatMap(s -> List.of(s.split(",")).stream())
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .toList();

        List<String> projectContains = projects == null ? null : projects.stream()
                .flatMap(s -> List.of(s.split(",")).stream())
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .toList();

        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 200));
        Page<Employee> pageResult = employeeService.getEmployeesByFilters(reviewDate, score, departmentContains, projectContains, pageable);

        Page<EmployeeListItemResponse> mapped = pageResult.map(e -> {
            DepartmentSummary dept = e.getDepartment() != null ? new DepartmentSummary(e.getDepartment().getId(), e.getDepartment().getName()) : null;
            List<ProjectSummary> projectsList = e.getEmployeeProjects() == null ? List.of() : e.getEmployeeProjects().stream()
                    .map(EmployeeProject::getProject)
                    .filter(p -> p != null)
                    .map(p -> new ProjectSummary(p.getId(), p.getName()))
                    .distinct()
                    .toList();
            return new EmployeeListItemResponse(e.getId(), e.getName(), e.getEmail(), dept, projectsList);
        });

        return ResponseEntity.ok(PageResponse.from(mapped));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id,
            @RequestParam(value = "includeProjects", defaultValue = "true") boolean includeProjects,
            @RequestParam(value = "includeReviews", defaultValue = "true") boolean includeReviews,
            @RequestParam(value = "reviewsLimit", defaultValue = "3") int reviewsLimit
    ) {
        return ResponseEntity.ok(employeeService.getEmployeeDetailsResponse(id, includeProjects, includeReviews, reviewsLimit));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody @jakarta.validation.Valid EmployeeCreateRequest request) {
        Employee saved = employeeService.createEmployee(request);
        // For creation response, omit projects and reviews by default
        return ResponseEntity.ok(employeeService.getEmployeeDetailsResponse(saved.getId(), false, false, 0));
    }
}


