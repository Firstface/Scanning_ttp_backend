# Verification Report Summary

Generated from actual local commands on 2026-07-30 and 2026-07-31.

## Implemented locally and verified

| Area | Evidence | Result |
|---|---|---|
| Maven tests | `reports/testing/mvn-clean-verify-after-concurrency-fix.txt` | 41 unit + 5 integration tests passed |
| JaCoCo | `target/site/jacoco/jacoco.csv`, `reports/testing/test-summary.json` | Line 46.00%, branch 21.08% |
| Flyway migrations | `reports/database/migration-output.txt` | V1–V4 applied successfully |
| JPA relationship verification | `reports/database/relationship-verification.txt` | Required 1:N foreign keys verified |
| Local Compose staging | `reports/deployment/staging-k6-sla-reset.txt` | MySQL, backend, frontend, and Nginx healthy |
| Playwright E2E | `reports/e2e/playwright-console.txt` | 3 passed |
| k6 load validation | `reports/k6/k6-console.txt`, `reports/k6/k6-summary.json` | 50 VUs, 60 s, all thresholds passed |
| Local production-like deployment | `reports/deployment/production-like-deployment.txt` | Profile active; health and frontend smoke checks passed |
| Local rollback | `reports/deployment/rollback-test.txt` | Rolled back to immutable tag; health and frontend smoke checks passed |
| Semgrep | `reports/security/semgrep.json` | 0 findings, exit code 0 |
| Gitleaks | `reports/security/gitleaks.json` | 0 leaks, exit code 0 |

## Configured but not executed

- Trivy filesystem and image scans: registry TLS verification blocked vulnerability-database retrieval; no vulnerability count is claimed.
- Video audio validation: no final video file is present.

No cloud deployment, production URL, GitHub Actions run, or artificial security result is claimed.
