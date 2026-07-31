# Evidence Screenshot Checklist

Only capture results that were actually produced in the target environment.

## Available local evidence

| Item | Artifact |
|---|---|
| Maven verification | `reports/testing/maven-verify.txt` |
| Test totals and coverage | `reports/testing/final-test-summary.md` |
| Flyway migration output | `reports/database/migration-output.txt` |
| Relationship verification | `reports/database/relationship-verification.txt` |
| Schema dump / ERDs | `reports/database/schema.sql`, `erd.mmd`, `erd-domain-separated.mmd` |
| Physical architecture | `docs/physical-architecture.svg` |

## Capture only after executing on a Docker/CI host

- Docker Compose health and staging/production-like deployment logs
- Rollback before/after health checks
- GitHub Actions run details
- Trivy, Semgrep, Gitleaks, Playwright, k6 and DAST artifacts
- ffprobe output and manual playback confirmation for final videos
