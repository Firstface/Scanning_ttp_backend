# Final Evidence Index

Generated from actual local execution on 2026-07-30 and 2026-07-31.

| Review item | Status | Implementation files | Verification command / evidence | Result |
|---|---|---|---|---|
| Profile-selectable storage | Implemented locally and verified | `repository/TaskStore.java`, `repository/InMemoryTaskRepository.java`, `persistence/JpaTaskStore.java`, `application-*.yml` | `mvn clean verify`; `reports/testing/mvn-clean-verify-after-concurrency-fix.txt` | Default demo preserves in-memory storage; persistence tests passed |
| JPA/Flyway domains and migrations | Implemented locally and verified | `persistence/entity/**`, `persistence/repository/**`, `db/migration/V1__*.sql`–`V4__*.sql` | `mvn clean verify`; `reports/database/migration-output.txt` | Four migrations applied successfully |
| Required 1:N relationships | Implemented locally and verified | JPA entity mappings and V2–V4 foreign keys | `reports/database/relationship-verification.txt`; `reports/database/erd.mmd` | Required task/run/sampling/shard/log/audit relationships verified |
| Compose staging topology | Implemented locally and verified | `deploy/docker-compose.yml`, `application-staging.yml`, `Dockerfile` | `reports/deployment/staging-k6-sla-reset.txt` | MySQL, backend, frontend, and Nginx healthy on local staging |
| Playwright E2E | Implemented locally and verified | `tests/e2e/**` | `reports/e2e/playwright-console.txt`; `reports/e2e/exit-code.txt` | 3/3 passed |
| k6 load validation | Implemented locally and verified | `tests/k6/load-test.js` | `reports/k6/k6-console.txt`; `reports/k6/k6-summary.json`; `reports/k6/exit-code.txt` | 50 VUs for 60 s; 16,503/16,503 checks passed; HTTP failure rate 0.00% |
| Production-like deployment | Implemented locally and verified | `deploy/docker-compose.yml`, `application-production-like.yml` | `reports/deployment/production-like-deployment.txt` | All services healthy; backend ran with `production-like` profile; health and frontend smoke checks passed |
| Rollback | Implemented locally and verified | `scripts/rollback-compose.sh` | `reports/deployment/rollback-test.txt` | Rolled back from `capstone-6e7a509-20260730194949` to `capstone-6e7a509-20260730194627`; health and frontend smoke checks passed |
| Semgrep static analysis | Implemented locally and verified | `.github/workflows/*.yml` | `reports/security/semgrep.json`; `reports/security/semgrep-console.txt` | 0 findings; exit code 0 |
| Gitleaks secret scan | Implemented locally and verified | `.gitleaks.toml` | `reports/security/gitleaks.json`; `reports/security/gitleaks-console.txt` | 0 leaks; exit code 0 |
| Trivy resolution and rescan | Implemented locally and verified | `pom.xml`, `Dockerfile`, CI Trivy steps | `reports/security/trivy-summary-20260731-151840.md`; `reports/security/trivy-fs-20260731-151424.json`; `reports/security/trivy-image-runtime-verify-20260731-151840.json` | Filesystem and runtime image scans both returned `HIGH=0`, `CRITICAL=0` |
| Video audio validation | Not implemented | `reports/video/audio-validation.md` | `reports/video/discovered-video-files.txt` | No final video file is present |

## Commands actually executed

```text
mvn clean verify
BASE_URL=http://localhost:8088 npm --prefix tests/e2e exec -- playwright test --config=tests/e2e/playwright.config.js --reporter=list,json
BASE_URL=http://localhost:8088 k6 run --summary-export=reports/k6/k6-summary.json tests/k6/load-test.js
HTTP_PORT=8089 IMAGE_TAG=capstone-6e7a509-20260730194949 SPRING_PROFILE=production-like docker compose -f deploy/docker-compose.yml up -d --no-build --wait
HTTP_PORT=8089 sh scripts/rollback-compose.sh capstone-6e7a509-20260730194627 production-like
curl --fail --silent --show-error http://localhost:8089/actuator/health
semgrep scan --config p/default --error --json --output reports/security/semgrep.json .
gitleaks detect --source . --no-git --config .gitleaks.toml --report-format json --report-path reports/security/gitleaks.json
```

No cloud deployment, production URL, GitHub Actions run, or video validation is claimed.
