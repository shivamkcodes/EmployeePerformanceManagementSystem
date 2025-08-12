package com.example.employeeperformancemanagement.service;

import com.example.employeeperformancemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeeperformancemanagement.dto.response.EmployeeResponse;
import com.example.employeeperformancemanagement.model.Employee;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    List<Employee> getEmployeesByFilters(LocalDate reviewDate, List<String> departmentNames, List<String> projectNames);

    Page<Employee> getEmployeesByFilters(LocalDate reviewDate,
                                         Integer score,
                                         List<String> departmentContains,
                                         List<String> projectContains,
                                         Pageable pageable);
    Employee getEmployeeDetails(Long id);
    EmployeeResponse getEmployeeDetailsResponse(Long id, boolean includeProjects, boolean includeReviews, int reviewsLimit);
    Employee createEmployee(Employee employee);
    Employee createEmployee(EmployeeCreateRequest request);
}

