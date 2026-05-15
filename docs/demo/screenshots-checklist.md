# Screenshots Checklist - 20+ Key Screenshots for Demo

Capture these screenshots in order for your presentation.

## Part 1: Code & Tests (6 screenshots)

| # | Screenshot Content | File/URL | Notes |
|---|------------------|---------|-------|
| 1 | JaCoCo Coverage Report | `reports/jacoco/index.html` | Focus on line coverage percentage |
| 2 | Unit Test Results - Terminal | Maven test output | Show all green tests |
| 3 | Semgrep Scan Output | Terminal | Show scan complete message |
| 4 | Gitleaks Secret Scan | Terminal | Show "no secrets" message |
| 5 | Trivy Container Scan | `reports/trivy.txt` | Show HIGH/CRITICAL counts |
| 6 | Integration Test Results | Terminal | Show all green IT tests |

## Part 2: GitHub Actions (5 screenshots)

| # | Screenshot Content | File/URL | Notes |
|---|------------------|---------|-------|
| 7 | GitHub Actions Workflow List | `<YOUR_GH_URL>/actions` | Show ci.yml running/completed |
| 8 | CI Pipeline - All Green Jobs | Actions → ci.yml → Run | Show all jobs green |
| 9 | GHCR Package List | Profile → Packages | Show scanning-ttp-backend |
| 10 | GHCR Package Tags | Package page | Show latest and SHA tags |
| 11 | CD Pipeline Approval Gate | Actions → cd.yml → Run | Paused at production approval |

## Part 3: API & Demo (7 screenshots)

| # | Screenshot Content | File/URL | Notes |
|---|------------------|---------|-------|
| 12 | HTTPS Browser - Small Lock Icon | `https://localhost` | Focus on the padlock in address bar |
| 13 | JWT Login Request - Success | Terminal/curl | Show token returned |
| 14 | Unauthorized Request (401) | Terminal | Without JWT token |
| 15 | Rate Limit Hit (429) | Terminal | After 60+ requests |
| 16 | Create Task API Response | Terminal | Show taskId returned |
| 17 | Pipeline Status (7 steps) | API response or browser | Show all executor statuses |
| 18 | Task Detail Page | Frontend browser | Show pipeline visualization |

## Part 4: Performance & Security (3 screenshots)

| # | Screenshot Content | File/URL | Notes |
|---|------------------|---------|-------|
| 19 | k6 Load Test Output | Terminal | Show RPS and p95 |
| 20 | OWASP ZAP Report | `reports/zap/zap-report.html` | Browser view of DAST report |
| 21 | Terraform Plan Output | `reports/terraform-plan.txt` | Show 6 resources to add |

## Bonus Screenshots

| # | Screenshot Content | File/URL | Notes |
|---|------------------|---------|-------|
| 22 | Rollback Workflow Trigger | Actions → rollback.yml → Run workflow | Manual trigger UI |
| 23 | Docker Compose Running | Terminal `docker ps` | Show all 3 services |
| 24 | Full Pipeline Mermaid Diagram | `docs/cicd-diagram.md` | Architecture diagram |
| 25 | Security STRIDE Table | `SECURITY.md` | Threat modeling table |

## Tips for Great Screenshots

- Use browser dev tools → Responsive design mode for consistent size
- Close other tabs and apps
- Use clean terminal theme
- Highlight important parts with arrows/text overlays (in Keynote/PowerPoint)
- For browser screenshots, hide bookmarks bar
