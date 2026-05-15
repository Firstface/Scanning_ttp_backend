# CI/CD Demo Script - 4.5 minutes

## Scene 1: Making a Change (0:00 - 0:30)

**Mouse Action**: Show code in IDE, make small change

**Speak**: "Let's make a simple change to our codebase. I'll add a comment or update some text - this will trigger our full CI/CD pipeline!"

**Git Action**:
```bash
git add .
git commit -m "feat: add demo comment"
git push origin main
```

---

## Scene 2: Watch CI Pipeline (0:30 - 2:30)

**Mouse Action**: Open GitHub Actions tab

**Speak**: "Perfect! The CI pipeline is already running. Let's walk through what's happening..."

**Show Each Stage**:
1. ✅ **Lint & Scan** - Semgrep and Gitleaks pass
2. ✅ **Unit Tests** - Green checkmarks, coverage report
3. ✅ **Build & Push** - Docker image built and pushed to GHCR
4. 🔄 **E2E Tests** - Running now...
5. 🔄 **DAST Scan** - OWASP ZAP at work

**Speak**: "Every push runs all these checks - security, tests, builds - automatically!"

---

## Scene 3: Review Artifacts (2:30 - 3:30)

**Mouse Action**: Click on artifacts, browse reports

**Speak**: "Look at all these beautiful reports! JaCoCo for coverage, Trivy for vulnerabilities, k6 for performance, OWASP ZAP for DAST, and Playwright traces!"

**Show**:
- Artifacts section in CI run
- Coverage HTML report (highlight >50%)
- Trivy SARIF (no criticals)
- k6 performance graph

---

## Scene 4: Trigger CD Deploy (3:30 - 4:15)

**Mouse Action**: Create a new release tag, or use workflow dispatch

**Speak**: "Now let's deploy to production! I'll use workflow dispatch... first to staging, then we need manual approval for production."

**Click**:
- Actions → CD Pipeline → Run workflow
- Choose environment: production
- Watch "Manual Approval" pause

**Speak**: "This is a safety gate - real users can approve before production!"

---

## Scene 5: Rollback Demo (4:15 - 4:30)

**Mouse Action**: Show Rollback workflow

**Speak**: "And if something goes wrong? One click rollback. DevOps made simple!"

**End**
