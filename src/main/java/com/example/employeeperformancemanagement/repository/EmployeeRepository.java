package com.example.employeeperformancemanagement.repository;

import com.example.employeeperformancemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT DISTINCT e FROM Employee e " +
           "JOIN e.performanceReviews pr " +
           "LEFT JOIN e.department d " +
           "LEFT JOIN e.employeeProjects ep " +
           "LEFT JOIN ep.project p " +
           "WHERE pr.reviewDate = :reviewDate " +
           "AND (:#{#departmentNames == null} = true OR d.name IN :departmentNames) " +
           "AND (:#{#projectNames == null} = true OR p.name IN :projectNames)")
    List<Employee> findEmployeesByFilters(@Param("reviewDate") LocalDate reviewDate,
                                          @Param("departmentNames") List<String> departmentNames,
                                          @Param("projectNames") List<String> projectNames);

    @Query("""
        select e from Employee e
        left join fetch e.department d
        where e.id = :id
    """)
    Employee findByIdWithDeptAndProjects(@Param("id") Long id);

    boolean existsByEmail(String email);
}


