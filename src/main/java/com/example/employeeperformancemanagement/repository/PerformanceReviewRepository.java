package com.example.employeeperformancemanagement.repository;

import com.example.employeeperformancemanagement.model.PerformanceReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    List<PerformanceReview> findByEmployeeIdOrderByIdDesc(Long employeeId, Pageable pageable);
}



