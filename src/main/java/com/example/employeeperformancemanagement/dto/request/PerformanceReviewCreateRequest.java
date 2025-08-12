package com.example.employeeperformancemanagement.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PerformanceReviewCreateRequest {
    private Long employeeId;
    private LocalDate reviewDate;
    private Integer score;
    private String reviewComments;
}



