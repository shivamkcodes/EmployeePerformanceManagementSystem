package com.example.employeeperformancemanagement.service.impl;

import com.example.employeeperformancemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeeperformancemanagement.dto.response.EmployeeResponse;
import com.example.employeeperformancemanagement.dto.response.DepartmentSummary;
import com.example.employeeperformancemanagement.dto.response.PerformanceReviewResponse;
import com.example.employeeperformancemanagement.dto.response.ProjectSummary;
import com.example.employeeperformancemanagement.model.Department;
import com.example.employeeperformancemanagement.model.Employee;
import com.example.employeeperformancemanagement.model.EmployeeProject;
import com.example.employeeperformancemanagement.model.Project;
import com.example.employeeperformancemanagement.model.PerformanceReview;
import com.example.employeeperformancemanagement.repository.EmployeeRepository;
import com.example.employeeperformancemanagement.repository.PerformanceReviewRepository;
import com.example.employeeperformancemanagement.repository.EmployeeProjectRepository;
import com.example.employeeperformancemanagement.service.DepartmentService;
import com.example.employeeperformancemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PerformanceReviewRepository performanceReviewRepository;

    @Autowired
    private DepartmentService departmentService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee createEmployee(EmployeeCreateRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDateOfJoining(request.getDateOfJoining());
        employee.setSalary(request.getSalary());
        if (request.getDepartmentId() != null) {
            Department department = departmentService.getDepartmentById(request.getDepartmentId());
            employee.setDepartment(department);
        }
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId()).orElse(null);
            employee.setManager(manager);
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getEmployeesByFilters(LocalDate reviewDate, List<String> departmentNames, List<String> projectNames) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> employee = cq.from(Employee.class);
        Join<Employee, PerformanceReview> pr = employee.join("performanceReviews");
        Join<Employee, Department> dept = employee.join("department", JoinType.LEFT);
        Join<Employee, EmployeeProject> ep = employee.join("employeeProjects", JoinType.LEFT);
        Join<EmployeeProject, Project> proj = ep.join("project", JoinType.LEFT);

        Predicate byDate = cb.equal(pr.get("reviewDate"), reviewDate);
        Predicate deptFilter = cb.conjunction();
        if (departmentNames != null && !departmentNames.isEmpty()) {
            deptFilter = dept.get("name").in(departmentNames);
        }
        Predicate projectFilter = cb.conjunction();
        if (projectNames != null && !projectNames.isEmpty()) {
            projectFilter = proj.get("name").in(projectNames);
        }

        cq.select(employee).distinct(true).where(cb.and(byDate, deptFilter, projectFilter));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesByFilters(LocalDate reviewDate,
                                                Integer score,
                                                List<String> departmentContains,
                                                List<String> projectContains,
                                                Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Main query
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> employee = cq.from(Employee.class);
        Join<Employee, PerformanceReview> pr = employee.join("performanceReviews", JoinType.LEFT);
        Join<Employee, Department> dept = employee.join("department", JoinType.LEFT);
        Join<Employee, EmployeeProject> ep = employee.join("employeeProjects", JoinType.LEFT);
        Join<EmployeeProject, Project> proj = ep.join("project", JoinType.LEFT);

        Predicate predicate = cb.conjunction();
        if (reviewDate != null) {
            predicate = cb.and(predicate, cb.equal(pr.get("reviewDate"), reviewDate));
        }
        if (score != null && reviewDate != null) {
            predicate = cb.and(predicate, cb.equal(pr.get("score"), score));
        }

        if (departmentContains != null && !departmentContains.isEmpty()) {
            Predicate deptOr = cb.disjunction();
            for (String term : departmentContains) {
                if (term != null && !term.isBlank()) {
                    deptOr = cb.or(deptOr, cb.like(cb.lower(dept.get("name")), "%" + term.trim().toLowerCase() + "%"));
                }
            }
            predicate = cb.and(predicate, deptOr);
        }

        if (projectContains != null && !projectContains.isEmpty()) {
            Predicate projOr = cb.disjunction();
            for (String term : projectContains) {
                if (term != null && !term.isBlank()) {
                    projOr = cb.or(projOr, cb.like(cb.lower(proj.get("name")), "%" + term.trim().toLowerCase() + "%"));
                }
            }
            predicate = cb.and(predicate, projOr);
        }

        cq.select(employee).distinct(true).where(predicate);

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Employee> employeeCount = countQuery.from(Employee.class);
        Join<Employee, PerformanceReview> prC = employeeCount.join("performanceReviews", JoinType.LEFT);
        Join<Employee, Department> deptC = employeeCount.join("department", JoinType.LEFT);
        Join<Employee, EmployeeProject> epC = employeeCount.join("employeeProjects", JoinType.LEFT);
        Join<EmployeeProject, Project> projC = epC.join("project", JoinType.LEFT);

        Predicate countPredicate = cb.conjunction();
        if (reviewDate != null) {
            countPredicate = cb.and(countPredicate, cb.equal(prC.get("reviewDate"), reviewDate));
        }
        if (score != null && reviewDate != null) {
            countPredicate = cb.and(countPredicate, cb.equal(prC.get("score"), score));
        }
        if (departmentContains != null && !departmentContains.isEmpty()) {
            Predicate deptOr = cb.disjunction();
            for (String term : departmentContains) {
                if (term != null && !term.isBlank()) {
                    deptOr = cb.or(deptOr, cb.like(cb.lower(deptC.get("name")), "%" + term.trim().toLowerCase() + "%"));
                }
            }
            countPredicate = cb.and(countPredicate, deptOr);
        }
        if (projectContains != null && !projectContains.isEmpty()) {
            Predicate projOr = cb.disjunction();
            for (String term : projectContains) {
                if (term != null && !term.isBlank()) {
                    projOr = cb.or(projOr, cb.like(cb.lower(projC.get("name")), "%" + term.trim().toLowerCase() + "%"));
                }
            }
            countPredicate = cb.and(countPredicate, projOr);
        }
        countQuery.select(cb.countDistinct(employeeCount)).where(countPredicate);

        long total = entityManager.createQuery(countQuery).getSingleResult();

        List<Employee> content = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Employee getEmployeeDetails(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            List<PerformanceReview> last3Reviews = performanceReviewRepository.findByEmployeeIdOrderByIdDesc(id, PageRequest.of(0, 3));
            employee.setPerformanceReviews(last3Reviews);
        }
        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeDetailsResponse(Long id, boolean includeProjects, boolean includeReviews, int reviewsLimit) {
        Employee employee = employeeRepository.findByIdWithDeptAndProjects(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        List<PerformanceReview> last3Reviews = includeReviews
                ? performanceReviewRepository.findByEmployeeIdOrderByIdDesc(id, PageRequest.of(0, Math.max(0, reviewsLimit)))
                : List.of();

        Department dept = employee.getDepartment();
        DepartmentSummary deptResp = dept != null ? new DepartmentSummary(dept.getId(), dept.getName()) : null;

        List<ProjectSummary> projectSummaries = null;
        if (includeProjects) {
            List<Project> projects = employeeProjectRepository.findProjectsByEmployeeId(id);
            projectSummaries = projects.stream()
                    .map(p -> new ProjectSummary(p.getId(), p.getName()))
                    .distinct()
                    .collect(Collectors.toList());
        }

        List<PerformanceReviewResponse> reviewResponses = null;
        if (includeReviews) {
            reviewResponses = last3Reviews.stream()
                    .map(r -> new PerformanceReviewResponse(r.getId(), r.getReviewDate(), r.getScore(), r.getReviewComments()))
                    .collect(Collectors.toList());
        }

        return new EmployeeResponse(
                employee.getId(), employee.getName(), employee.getEmail(), employee.getDateOfJoining(), employee.getSalary(),
                deptResp, projectSummaries, reviewResponses
        );
    }
}

