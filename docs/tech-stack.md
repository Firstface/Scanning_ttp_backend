# Tech Stack

## Implemented and verified locally

- Java 17, Spring Boot 3.5.14, Maven
- Executor pipeline pattern with default in-memory demo store
- Spring Data JPA entities/repositories and Flyway V1–V4 migrations
- H2 in MySQL compatibility mode for persistence integration verification
- JUnit 5, Failsafe and JaCoCo
- Mock metadata, scheduler and query/result adapters, explicitly labelled as mock adapters

## Configured but not executed on the latest host

- MySQL 8.4 + Docker Compose staging/production-like topology
- Nginx reverse proxy and React frontend container
- GitHub Actions Semgrep, Gitleaks, Docker build, Trivy, manual DAST/E2E and k6 stages

The host used for the evidence run did not have Docker, Trivy, Semgrep, Gitleaks or ffprobe installed. No scan, deployment, DAST, k6 or video-validation success is claimed.
