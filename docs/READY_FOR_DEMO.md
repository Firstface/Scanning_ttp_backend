# Final Demonstration Readiness

## Implemented locally and verified

- `mvn clean verify` passed: 40 Surefire tests and 5 Failsafe tests, all passing.
- JaCoCo: line coverage 46.40%; branch coverage 22.15%.
- JPA/Flyway persistence path passed H2 MySQL-mode migration and relationship verification.
- Physical architecture and database evidence are in `docs/physical-architecture.svg` and `reports/database/`.
- Docker Engine 29.6.2 and Docker Compose v5.3.1 were verified.
- Semgrep reported 0 findings and Gitleaks reported 0 leaks.

## Configured but not executed on this host

Staging deployment was attempted twice with immutable image tags. The second attempt used revised ARM64-compatible backend base images, but Docker BuildKit failed TLS certificate verification for Docker Hub base images. Therefore production-like deployment, rollback execution, frontend smoke test, Playwright, k6, Trivy image scan, DAST and GitHub Actions remain unexecuted. Trivy filesystem scanning is also blocked by vulnerability-database registry TLS verification. No final video file was supplied for audio validation.

## Required before a production-like demonstration

1. Repair Docker Desktop / corporate certificate trust so Docker BuildKit can pull base images from Docker Hub without disabling TLS verification.
2. Re-run staging, then production-like deployment and rollback; save health and deployment logs under `reports/deployment/`.
3. Re-run Trivy filesystem and image scans, then run Playwright and k6 against the deployed stack.
4. Provide final videos and run ffprobe plus a manual playback check.

Do not claim these steps passed until their artifacts exist.
