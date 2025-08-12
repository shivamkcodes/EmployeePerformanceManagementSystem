package com.example.employeeperformancemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PerformanceReviewResponse {
    private Long id;
    private LocalDate reviewDate;
    private Integer score;
    private String reviewComments;
}



