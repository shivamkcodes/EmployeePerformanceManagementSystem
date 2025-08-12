package com.example.employeeperformancemanagement.service.impl;

import com.example.employeeperformancemanagement.dto.request.PerformanceReviewCreateRequest;
import com.example.employeeperformancemanagement.model.Employee;
import com.example.employeeperformancemanagement.model.PerformanceReview;
import com.example.employeeperformancemanagement.repository.PerformanceReviewRepository;
import com.example.employeeperformancemanagement.service.EmployeeService;
import com.example.employeeperformancemanagement.service.PerformanceReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    @Autowired
    private PerformanceReviewRepository performanceReviewRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public PerformanceReview createPerformanceReview(PerformanceReview performanceReview) {
        return performanceReviewRepository.save(performanceReview);
    }

    @Override
    public PerformanceReview createPerformanceReview(PerformanceReviewCreateRequest request) {
        Employee employee = employeeService.getEmployeeDetails(request.getEmployeeId());
        if (employee == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Employee not found");
        }
        PerformanceReview pr = new PerformanceReview();
        pr.setEmployee(employee);
        pr.setReviewDate(request.getReviewDate());
        pr.setScore(request.getScore());
        pr.setReviewComments(request.getReviewComments());
        return performanceReviewRepository.save(pr);
    }
}


