# Final Test Summary

Status: **Implemented locally and verified** for the local staging stack, Maven tests, Playwright E2E, k6 load validation, Flyway/JPA persistence tests, Semgrep, and Gitleaks.

## Executed

- `mvn clean verify`
- `BASE_URL=http://localhost:8088 npm --prefix tests/e2e exec -- playwright test --config=tests/e2e/playwright.config.js --reporter=list,json`
- `BASE_URL=http://localhost:8088 k6 run --summary-export=reports/k6/k6-summary.json tests/k6/load-test.js`
- Docker Compose staging reset and `up -d --no-build --wait` with the immutable local image tag recorded in `reports/deployment/image-tags.txt`
- `curl --fail --silent --show-error http://localhost:8088/actuator/health`
- Semgrep and Gitleaks commands recorded in their respective evidence files

## Results

| Suite | Total | Passed | Failed | Skipped | Pass rate | Duration |
|---|---:|---:|---:|---:|---:|---:|
| Surefire unit tests | 41 | 41 | 0 | 0 | 100.0% | Included in Maven verify |
| Failsafe integration tests | 5 | 5 | 0 | 0 | 100.0% | Included in Maven verify |
| Playwright E2E | 3 | 3 | 0 | 0 | 100.0% | 20.5 s |
| k6 load validation | 16,503 checks | 16,503 | 0 | 0 | 100.0% | 60.7 s |

Maven `clean verify` completed in **20.847 s**.

JaCoCo line coverage: **46.00%** (403 covered / 876 total)

JaCoCo branch coverage: **21.08%** (35 covered / 166 total)

k6 used 50 virtual users for 60 seconds. All enforced thresholds passed: HTTP failure rate 0.00%; overall HTTP p95 423.14 ms (< 2 s); health-endpoint p95 164.9 ms (< 500 ms); task-create p95 1.31 s (< 2 s).

Static scan results: **Semgrep 0 findings; Gitleaks 0 leaks**.

## Artifacts

- `reports/testing/mvn-clean-verify-after-concurrency-fix.txt`
- `reports/e2e/playwright-console.txt`
- `reports/e2e/exit-code.txt`
- `reports/k6/k6-console.txt`
- `reports/k6/k6-summary.json`
- `reports/k6/exit-code.txt`
- `reports/k6/backend-after-k6-sla.log`
- `reports/deployment/staging-k6-sla-reset.txt`
- `reports/deployment/image-tags.txt`
- `target/surefire-reports/`, `target/failsafe-reports/`, and `target/site/jacoco/`

## Configured but not executed

- Production-like deployment and rollback.
- Trivy filesystem and image scans: vulnerability database retrieval remained blocked by registry TLS verification, so no CVE count is claimed.
- ffprobe audio validation: no final video file is present.
