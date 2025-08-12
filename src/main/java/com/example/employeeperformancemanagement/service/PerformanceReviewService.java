package com.example.employeeperformancemanagement.service;

import com.example.employeeperformancemanagement.dto.request.PerformanceReviewCreateRequest;
import com.example.employeeperformancemanagement.model.PerformanceReview;

public interface PerformanceReviewService {
    PerformanceReview createPerformanceReview(PerformanceReview performanceReview);
    PerformanceReview createPerformanceReview(PerformanceReviewCreateRequest request);
}


