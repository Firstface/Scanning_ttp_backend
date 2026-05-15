# Security Documentation

## Threat Model (STRIDE)

| Threat Type | Description | Mitigation | Status |
|-------------|-------------|------------|--------|
| **Spoofing** | Fake user identities | JWT Authentication (HS256) | ✅ |
| **Tampering** | Unauthorized data modification | Input validation (Jakarta Validation) | ✅ |
| **Repudiation** | Deny performing actions | Audit logging (TaskLogService) | ✅ |
| **Information Disclosure** | Sensitive data exposure | Secrets via env vars, no DB | ✅ |
| **Denial of Service** | Overwhelm service | Rate limiting (Bucket4j, 60/min/IP) | ✅ |
| **Elevation of Privilege** | Gain unauthorized access | (Not required for demo) | N/A |

## Security Features Implemented

1. **JWT Authentication**: HS256 signed tokens, 1h expiration, configurable via `app.auth.enabled`
2. **Rate Limiting**: Bucket4j, 60 requests/minute per IP, configurable via `app.rate-limit.enabled`
3. **Input Validation**: `jakarta.validation` constraints on DTOs
4. **Secret Management**: All config via env vars (12-factor)
5. **SAST**: Semgrep in CI pipeline
6. **Secret Scan**: Gitleaks in CI pipeline
7. **Container Scan**: Trivy vulnerability scan
8. **DAST**: OWASP ZAP in CI pipeline
9. **Dependency Scan**: Maven dependency tree + Trivy
10. **Secure Headers**: CORS config in WebConfig
11. **HTTPS**: Nginx reverse proxy with mkcert certificates

## Demo Account

| Username | Password |
|----------|----------|
| `admin` | `admin123` |

## CI Security Stages

| Tool | Purpose | Location |
|------|---------|----------|
| Gitleaks | Secret detection | CI.yml job 1 |
| Semgrep | SAST code analysis | CI.yml job 1 |
| Trivy | Container vulnerability scan | CI.yml job 4 |
| OWASP ZAP | DAST API scan | CI.yml job 5 |

## Known Risks

- **Risk**: No real authorization/roles
  - **Severity**: Low (demo project)
  - **Mitigation**: Clear documentation

- **Risk**: In-memory storage (no persistence)
  - **Severity**: Low (intended design)
