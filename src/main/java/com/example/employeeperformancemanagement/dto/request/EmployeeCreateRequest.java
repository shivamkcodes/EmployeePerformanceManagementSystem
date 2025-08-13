package com.example.employeeperformancemanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCreateRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @PastOrPresent
    private LocalDate dateOfJoining;

    @NotNull
    @Positive
    private Double salary;

    private Long departmentId;
    private Long managerId;
}



