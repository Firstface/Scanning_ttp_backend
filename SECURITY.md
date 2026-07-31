# Security Documentation

## Implemented and verified locally

- Input validation and JWT/rate-limit components remain in the application.
- JPA persistence adds optimistic locking (`version`) and audit events tied to task runs.
- Flyway-managed schema has foreign keys and indexes for the required domains.

## Configured but not executed

CI defines blocking Semgrep, Gitleaks, and Trivy stages. Trivy runs both image and filesystem scanning with HIGH/CRITICAL severity and exit code 1. Extended Playwright/k6 validation is manually triggered and preserves artifacts.

No local Semgrep, Gitleaks, Trivy, Docker image, DAST, or cloud scan result is claimed because the required tools were unavailable. See `reports/security/security-summary.json`.
