# CI/CD Pipeline Diagram

```mermaid
flowchart TD
    A[Code Push] --> B[Trigger CI Workflow]
    
    subgraph CI [CI Pipeline]
        B --> C[Lint & Security Scan]
        C -->|Semgrep + Gitleaks| D[Unit Tests & Coverage]
        D -->|JaCoCo + JUnit| E[Build & Push Docker]
        E -->|Trivy Scan| F[E2E & Load Tests]
        F -->|Playwright + k6| G[DAST with OWASP ZAP]
    end
    
    G --> H[Tag Trigger]
    
    subgraph CD [CD Pipeline]
        H --> I[Deploy to Staging]
        I --> J[Smoke Tests]
        J --> K[Manual Approval]
        K -->|Approve| L[Deploy to Production]
    end
    
    subgraph Rollback [Rollback Pipeline]
        M[Manual Trigger] --> N[Rollback to Previous Version]
    end
```

## Pipeline Stages

### CI Pipeline (ci.yml)
1. **Lint & Security Scan**: Semgrep (SAST) + Gitleaks (Secret Detection)
2. **Unit Tests**: JUnit + JaCoCo (Coverage Report)
3. **Build & Container Scan**: Build image + Trivy Vulnerability Scan
4. **E2E & Load Tests**: Playwright (API) + k6 (Performance)
5. **DAST**: OWASP ZAP Dynamic Scan

### CD Pipeline (cd.yml)
1. **Deploy to Staging**: Auto-deploy new tags
2. **Smoke Tests**: Verify basic functionality
3. **Manual Approval**: Review before production
4. **Deploy to Production**: Final rollout

### Rollback (rollback.yml)
- Manual trigger with previous version tag
- Quick rollback capability
