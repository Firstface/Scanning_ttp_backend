# Scanning TTP Backend

Spring Boot backend for a table-sampling task pipeline. The existing demo workflow remains available through the default/dev profiles; a profile-selected JPA/Flyway persistence path is available for staging and production-like local Compose deployments.

## Verified local result

`mvn clean verify` completed successfully on 2026-07-30:

- Surefire unit tests: **40 / 40 passed**
- Failsafe integration tests: **5 / 5 passed**
- JaCoCo line coverage: **46.40%**
- JaCoCo branch coverage: **22.15%**
- H2 in MySQL compatibility mode applied Flyway migrations V1–V4 and verified the required physical relationships.

See `reports/testing/final-test-summary.md` and `reports/database/` for generated evidence.

## Storage profiles

| Profile | Store | Status |
|---|---|---|
| default / `dev` / `demo` | Existing `InMemoryTaskRepository` and in-memory task logs | Implemented and verified by existing tests |
| `persistence` | Spring Data JPA + Flyway; H2 is used by the persistence integration test | Implemented locally and verified |
| `staging` / `production-like` | Spring Data JPA + Flyway configured for MySQL 8 | Configured but not executed on this host |

The persistent model includes `app_user`, `app_role`, `user_role`, `validation_task`, `task_run`, `sampling`, `shard_task`, `task_log`, and `audit_event`. Required 1:N relationships are documented and verified in `reports/database/relationship-verification.txt`.

## Local development

```bash
mvn clean verify
mvn spring-boot:run
```

For the in-memory demo, the default profile is sufficient. For a MySQL-backed process, use a Docker-capable host and the `staging` or `production-like` profile.

## Local production-like Compose topology

`deploy/docker-compose.yml` defines MySQL 8.4, backend, frontend and Nginx. It uses Flyway on backend startup and supports immutable image tags:

```bash
IMAGE_TAG=<immutable-tag> SPRING_PROFILE=staging docker compose -f deploy/docker-compose.yml up -d --build --wait
sh scripts/rollback-compose.sh <immutable-tag> production-like
```

**Configured but not executed:** Docker is unavailable on the host used for the latest evidence run. No cloud or production deployment is claimed. Deployment limitations are recorded under `reports/deployment/`.

## Security and extended validation

GitHub Actions configuration includes Maven verification, JaCoCo artifacts, Semgrep, Gitleaks, Docker build, Trivy filesystem/image scans that block HIGH/CRITICAL findings, and manually-triggered extended Playwright/k6 validation. Local scan results are not claimed because Trivy, Semgrep, Gitleaks and Docker were unavailable; see `reports/security/`.

## Architecture

- Diagram source: `docs/physical-architecture.mmd`
- Renderable SVG: `docs/physical-architecture.svg`
- Deployment guide: `docs/deployment.md`
