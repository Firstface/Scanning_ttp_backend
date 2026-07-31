# CI/CD Demo Script

Status: **Configured but not executed** for GitHub Actions on the latest verification host.

## What can be demonstrated truthfully

1. Open `.github/workflows/ci.yml` and show the configured gates: Maven verify, Semgrep, Gitleaks, Docker build, Trivy image/filesystem scan, and manual extended Playwright/k6 validation.
2. Open `reports/testing/final-test-summary.md` and show the actual local Maven result: 40 Surefire tests passed, 5 Failsafe tests passed, JaCoCo line coverage 46.40%, branch coverage 22.15%.
3. Open `reports/database/migration-output.txt` and `reports/database/relationship-verification.txt` to show Flyway V1–V4 and the verified JPA relationship evidence.
4. State explicitly that no GitHub Actions run, container scan, DAST, Playwright or k6 result was produced on the current host.

Do not present a green GitHub run, Trivy count, k6 chart, ZAP report or deployment result unless it is produced in the target environment and saved as a new artifact.
