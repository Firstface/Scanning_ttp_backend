# Security Documentation

## Threat Model (STRIDE)

| Threat Type | Description | Mitigation | Status |
|-------------|-------------|------------|--------|
| **Spoofing** | Fake user identities | (Not required for demo) | N/A |
| **Tampering** | Unauthorized data modification | Input validation (Jakarta Validation) | ✅ |
| **Repudiation** | Deny performing actions | Audit logging (TaskLogService) | ✅ |
| **Information Disclosure** | Sensitive data exposure | Secrets via env vars, no DB | ✅ |
| **Denial of Service** | Overwhelm service | Rate limiting (placeholder) | ✅ |
| **Elevation of Privilege** | Gain unauthorized access | (Not required for demo) | N/A |

## Security Features Implemented

1. **Input Validation**: `jakarta.validation` constraints on DTOs
2. **Secret Management**: All config via env vars (12-factor)
3. **SAST**: Semgrep in CI pipeline
4. **Secret Scan**: Gitleaks in CI pipeline
5. **Container Scan**: Trivy vulnerability scan
6. **DAST**: OWASP ZAP in CI pipeline
7. **Dependency Scan**: Maven dependency tree + Trivy
8. **Secure Headers**: CORS config in WebConfig

## CI Security Stages

| Tool | Purpose | Location |
|------|---------|----------|
| Gitleaks | Secret detection | CI.yml job 1 |
| Semgrep | SAST code analysis | CI.yml job 1 |
| Trivy | Container vulnerability scan | CI.yml job 3 |
| OWASP ZAP | DAST API scan | CI.yml job 4 |

## Known Risks

- **Risk**: No real authentication/authorization
  - **Severity**: Low (demo project)
  - **Mitigation**: Clear documentation

- **Risk**: In-memory storage (no persistence)
  - **Severity**: Low (intended design)
