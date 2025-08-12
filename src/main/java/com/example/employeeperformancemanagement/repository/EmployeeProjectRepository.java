package com.example.employeeperformancemanagement.repository;

import com.example.employeeperformancemanagement.model.EmployeeProject;
import com.example.employeeperformancemanagement.model.EmployeeProjectId;
import com.example.employeeperformancemanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, EmployeeProjectId> {
    @Query("select p from EmployeeProject ep join ep.project p where ep.employee.id = :employeeId")
    java.util.List<Project> findProjectsByEmployeeId(@Param("employeeId") Long employeeId);
}



