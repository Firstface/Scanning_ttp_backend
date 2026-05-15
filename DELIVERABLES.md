# Capstone Project Deliverables

## PPT Chapter Mapping

| PPT Section | Deliverable File | Description |
|-------------|------------------|-------------|
| **Tech Stack** | `docs/tech-stack.md` | Full list of technologies |
| **Architecture & Deployment** | `docs/deployment.mmd` | Mermaid deployment diagram |
| **CI/CD Pipeline** | `.github/workflows/*.yml` | 3 workflow files (ci, cd, rollback) |
| ↳ (Visualization) | `docs/cicd-diagram.md` | Mermaid CI/CD diagram |
| **Testing** | `tests/` + `pom.xml` | Unit, E2E, load tests |
| ↳ (Coverage Report) | `target/site/jacoco/` (when generated) | Test coverage HTML |
| **Security** | `SECURITY.md` | STRIDE analysis + mitigation list |
| **Infrastructure as Code** | `infra/terraform/` | Terraform files for Docker |
| **App Demo** | `docs/demo/app-demo-script.md` | 4.5 min presentation script |
| **CI/CD Demo** | `docs/demo/cicd-demo-script.md` | 4.5 min CI/CD script |
| **Checklist** | `docs/demo/screenshots-checklist.md` | 20+ screenshots needed |
| **Quick Start** | `README.md` | Badges + how to run |

## File Structure (Backend Repo)

```
📁 Scanning_ttp-1/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/.../
│   │   │   ├── 📁 executor/ (7 executors)
│   │   │   └── ...
│   │   └── 📁 resources/
│   └── 📁 test/ (New Unit Tests)
├── 📁 deploy/
│   └── 📄 docker-compose.yml (New)
├── 📁 tests/ (New)
│   ├── 📁 e2e/
│   │   ├── 📄 api.spec.js
│   │   └── 📄 playwright.config.js
│   └── 📁 k6/
│       └── 📄 load-test.js
├── 📁 .github/workflows/ (New)
│   ├── 📄 ci.yml
│   ├── 📄 cd.yml
│   └── 📄 rollback.yml
├── 📁 infra/terraform/ (New)
│   ├── 📄 main.tf
│   ├── 📄 variables.tf
│   └── 📄 outputs.tf
├── 📁 docs/ (New)
│   ├── 📁 demo/
│   │   ├── 📄 app-demo-script.md
│   │   ├── 📄 cicd-demo-script.md
│   │   └── 📄 screenshots-checklist.md
│   ├── 📄 cicd-diagram.md
│   ├── 📄 tech-stack.md
│   └── 📄 deployment.mmd
├── 📁 scripts/ (New)
│   └── 📄 demo-reset.sh
├── 📄 Dockerfile (New)
├── 📄 Makefile (New)
├── 📄 SECURITY.md (New)
├── 📄 .env.example (New)
└── 📄 DELIVERABLES.md (This File)
```

## Frontend Repo Notes (Not in PPT)

**Frontend Only Change**: Added `Dockerfile`

```
📁 Scanning_ttp_frontend/
└── 📄 Dockerfile (New)
```

## Demo Scripts

| Script | Purpose |
|--------|---------|
| `scripts/demo-reset.sh` | Reset local demo environment |
| `docs/demo/app-demo-script.md` | App demo presentation flow |
| `docs/demo/cicd-demo-script.md` | CI/CD demo presentation flow |
| `docs/demo/screenshots-checklist.md` | List of screenshots needed |

## GitHub Setup for CI/CD

**Required**:
1. Enable GitHub Actions in repo Settings
2. Set up "staging" and "production" environments in repo → Settings → Environments
3. Allow GHCR write access (GitHub Actions has this by default)
