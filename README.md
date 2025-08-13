<img width="1725" height="936" alt="image" src="https://github.com/user-attachments/assets/20bdbf95-e21c-4b29-bb86-988735d6f951" />
## Employee Performance Management System

Spring Boot service to manage employees, departments, projects, and performance reviews, built per the provided assignment. Includes filtered, paginated employee listing and detailed employee fetch, Swagger docs, and environment-based configuration.

### Features
- Employees, Departments, Projects, Performance Reviews, and `EmployeeProject` join entity with extra fields.
- List employees with dynamic filters and pagination:
  - Optional review date; optional score (only applied when review date is provided)
  - Department filter: multi-value, case-insensitive contains
  - Project filter: multi-value, case-insensitive contains
- Employee details endpoint including department, projects, and last 3 reviews.
- Swagger UI for API exploration.
- Env-var driven configuration (no secrets in VCS).

### Tech Stack
- Java 17, Spring Boot 3, Spring Data JPA (Hibernate), MySQL 8
- springdoc-openapi (Swagger UI)
- Lombok

## Getting Started

### Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL 8.x

### Configuration (Environment Variables)
Set these (in shell, IntelliJ Run Config, or container env):

```text
SERVER_PORT=8082
DB_URL=jdbc:mysql://localhost:3306/employeedb?createDatabaseIfNotExist=true&useSSL=false
DB_USERNAME=root
DB_PASSWORD=changeme

JPA_DDL_AUTO=update            # or validate | create | create-drop
SHOW_SQL=false
FORMAT_SQL=false
HIGHLIGHT_SQL=false
HIBERNATE_SQL_LOG=INFO         # INFO or DEBUG
HIBERNATE_BIND_LOG=OFF         # OFF or TRACE to log bind params
HIBERNATE_BATCH_FETCH_SIZE=50
```

Sample properties template is available at `src/main/resources/application-example.properties`.

Note: `src/main/resources/application.properties` is `.gitignore`d and uses the env vars above.

### Build & Run

```bash
# build
./mvnw -DskipTests package

# run (using env vars)
DB_PASSWORD=changeme ./mvnw spring-boot:run

# or run the jar
DB_PASSWORD=changeme java -jar target/employee-performance-management-0.0.1-SNAPSHOT.jar
```

## API Documentation

- Swagger UI: `http://localhost:8082/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8082/v3/api-docs`

## Endpoints

### List Employees (filtered, paginated)
GET `/employees`

Query parameters:
- `reviewDate` (optional, ISO yyyy-MM-dd): filters to reviews on that date
- `score` (optional): applied only when `reviewDate` is provided; exact match
- `departments` (optional): multi-value, case-insensitive contains; supports CSV or repeated params
- `projects` (optional): multi-value, case-insensitive contains; supports CSV or repeated params
- `page` (default: 0), `size` (default: 20, max: 200)

Response: paginated list using a compact wrapper
```json
{
  "data": [
    {
      "id": 1,
      "name": "Jane Doe",
      "email": "jane.doe@example.com",
      "department": { "id": 1, "name": "Engineering" },
      "projects": [ { "id": 10, "name": "Web Platform" } ]
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

Examples:
```bash
# departments only (CSV)
curl 'http://localhost:8082/employees?departments=Engineering%2CFinance&page=0&size=20'

# reviewDate + score + projects contains
curl 'http://localhost:8082/employees?reviewDate=2024-12-30&score=4&projects=CRM,Mobile%20App'

# repeated params style
curl 'http://localhost:8082/employees?reviewDate=2024-12-30&departments=Engineering&departments=Data%20Science'
```

Notes:
- Without `score`: employees with any score on that `reviewDate` (if date provided) or no date filter if not provided.
- With `score`: requires `reviewDate`; otherwise returns 400.
- Filters across categories are combined with AND; within a category, multiple values are OR.

### Get Employee Details
GET `/employees/{id}`

Query parameters:
- `includeProjects` (default: true)
- `includeReviews` (default: true)
- `reviewsLimit` (default: 3)

Returns core employee fields, department summary, optional project summaries, and up to last N reviews.

## Data Model (Overview)

- `Employee`: id, name, email (unique), dateOfJoining, salary, department (ManyToOne), manager (self ManyToOne), performanceReviews (OneToMany), employeeProjects (OneToMany)
- `Department`: id, name, budget
- `Project`: id, name, description, startDate, endDate, department (ManyToOne)
- `PerformanceReview`: id, reviewDate, score, reviewComments, employee (ManyToOne)
- `EmployeeProject` (join): composite key (employee_id, project_id), employee (ManyToOne), project (ManyToOne), assignedDate, role

Fetching strategy:
- `@ManyToOne(fetch = LAZY)` on associations; DTOs use summaries to avoid over-fetching.
- Batched lazy loading via `hibernate.default_batch_fetch_size`.

## Validation & Error Handling

- `EmployeeCreateRequest` uses Jakarta Bean Validation:
  - `@NotBlank name`, `@NotBlank @Email email`, `@PastOrPresent dateOfJoining`, `@NotNull @Positive salary`
- Global exception handler returns structured 400 for validation errors and consistent JSON for `ResponseStatusException`.

## Performance Notes

- Pagination with DISTINCT to avoid duplicates from joins.
- LIKE '%term%' filters are not index-friendly; acceptable for small/medium datasets.
- Recommended indexes (DB-level):
  - `performance_review (employee_id, review_date, score)`
  - `employee_project (employee_id, project_id)` PK, and index on `(project_id)`
- For very large datasets, consider FULLTEXT search (MySQL 8) or external search if contains matching becomes a bottleneck.

## Development

- Run locally with env vars; update `application-example.properties` as a template.
- Swagger at `/swagger-ui.html`.
- Consider adding Flyway for migrations and GitHub Actions for CI.


