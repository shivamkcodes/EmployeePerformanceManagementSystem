package com.example.employeeperformancemanagement.controller;

import com.example.employeeperformancemanagement.dto.request.PerformanceReviewCreateRequest;
import com.example.employeeperformancemanagement.dto.response.PerformanceReviewResponse;
import com.example.employeeperformancemanagement.model.PerformanceReview;
import com.example.employeeperformancemanagement.service.PerformanceReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/performance-reviews")
public class PerformanceReviewController {

    @Autowired
    private PerformanceReviewService performanceReviewService;

    @PostMapping
    public ResponseEntity<PerformanceReviewResponse> create(@RequestBody PerformanceReviewCreateRequest request) {
        PerformanceReview saved = performanceReviewService.createPerformanceReview(request);
        return ResponseEntity.ok(new PerformanceReviewResponse(saved.getId(), saved.getReviewDate(), saved.getScore(), saved.getReviewComments()));
    }
}


