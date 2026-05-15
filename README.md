# Hive Sampling Mock (Capstone Project)

[![CI Pipeline](https://github.com/your-username/Scanning_ttp-1/actions/workflows/ci.yml/badge.svg)](https://github.com/your-username/Scanning_ttp-1/actions/workflows/ci.yml)
[![GitHub License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/java-17-blue)](pom.xml)
[![GitHub Release](https://img.shields.io/github/v/release/your-username/Scanning_ttp-1?sort=semver)](https://github.com/your-username/Scanning_ttp-1/releases)

这是一个面向教学演示的 Hive 表采样系统 Mock 项目，用于展示一个数据校验采样任务如何通过 executor 流水线完成元信息读取、分区选择、采样拆分、SQL 生成、异步执行和结果汇总。

**注：此项目仅在后端仓库实现完整功能。前端仅用于演示界面展示。**

---

## Quick Start (Docker Compose)

```bash
# Option 1: Use Make (recommended)
make up

# Option 2: Direct Docker Compose
cd deploy
docker-compose up -d --build

# Wait for services to be ready (~30 sec)

# Access the system:
# Backend: http://localhost:8080
# Frontend: http://localhost:80
# Health Check: http://localhost:8080/actuator/health
```

---

## What's Included (Capstone Deliverables)

### Backend-Focused DevOps Features
- ✅ **Containerization**: Docker + Docker Compose
- ✅ **CI/CD Pipeline**: GitHub Actions (ci.yml, cd.yml, rollback.yml)
- ✅ **Unit Tests**: JUnit + JaCoCo coverage
- ✅ **E2E Tests**: Playwright API tests
- ✅ **Load Tests**: k6 performance tests
- ✅ **Security Scans**: Semgrep, Gitleaks, Trivy, OWASP ZAP
- ✅ **IaC**: Terraform (kreuzwerker/docker provider)
- ✅ **Demo Scripts**: Presentation-ready 4.5 min scripts

---

## Executor Pipeline (7 Stages)

The fixed-order pipeline includes:

1. `RetrieveMetaInfosExecutor` - Load table metadata
2. `PartitionSelectorExecutor` - Select partitions
3. `SamplingExecutor` - Split into shards
4. `FinalQueryBuilderExecutor` - Build final SQL
5. `QueryDispatcherExecutor` - Execute shards
6. `ResultCollectorExecutor` - Collect results
7. `FinalizeTaskExecutor` - Final task status

---

## API Documentation

### Create and Start a Task
```bash
curl -X POST 'http://localhost:8080/api/sample-tasks' \
  -H 'Content-Type: application/json' \
  -d '{
    "databaseName": "demo_db",
    "tableName": "demo_event_table",
    "targetSampleRows": 2500,
    "selectedPartitions": ["2026-05-01", "2026-05-02", "2026-05-03"]
  }'
```

### Get Task Status
```bash
curl 'http://localhost:8080/api/sample-tasks/{taskId}'
```

### Get Pipeline Status
```bash
curl 'http://localhost:8080/api/sample-tasks/{taskId}/pipeline'
```

### Get Shard Status
```bash
curl 'http://localhost:8080/api/sample-tasks/{taskId}/shards'
```

### Get Task Logs
```bash
curl 'http://localhost:8080/api/sample-tasks/{taskId}/logs'
```

---

## Local Development (Without Docker)

### Prerequisites
- Java 17
- Maven 3.8+

### Run
```bash
mvn spring-boot:run
```

### Run Tests
```bash
mvn clean test jacoco:report
# Coverage report at: target/site/jacoco/index.html
```

---

## Project Structure

```
Scanning_ttp-1/
├── src/
│   ├── main/java/.../
│   │   ├── executor/           # 7 Executors
│   │   ├── pipeline/           # Pipeline runner & interface
│   │   ├── controller/         # REST API
│   │   ├── service/            # Business logic
│   │   ├── repository/         # In-memory storage
│   │   ├── model/              # Domain models
│   │   └── config/             # App config
│   └── test/java/              # Unit tests
├── deploy/
│   └── docker-compose.yml
├── .github/workflows/
│   ├── ci.yml                  # Push/PR Pipeline
│   ├── cd.yml                  # Tag/Deploy Pipeline
│   └── rollback.yml            # Rollback Pipeline
├── infra/terraform/            # Infrastructure as Code
├── docs/
│   └── demo/                   # Presentation scripts
└── scripts/                    # Utility scripts
```

---

## Makefile Commands

| Command | Purpose |
|---------|---------|
| `make up` | Start all services with Docker Compose |
| `make down` | Stop all services |
| `make logs` | Tail service logs |
| `make test` | Run tests + coverage |
| `make reset` | Full reset (clean + restart) |
| `make build` | Build all images |

---

## Demo Reset Script

```bash
./scripts/demo-reset.sh
```

Cleans up and restarts a fresh demo environment.

---

## GitHub Setup for CI/CD

### Requirements:
1. **Enable GitHub Actions** in repo Settings
2. **Create Environments** in repo → Settings → Environments:
   - `staging`
   - `production` (enable "Required reviewers" for manual gate)
3. **No secrets needed** - Uses GITHUB_TOKEN for GHCR

---

## Capstone Documentation (For PPT)

See [DELIVERABLES.md](DELIVERABLES.md) for full list of deliverables and PPT chapter mapping.

| Document | Location |
|----------|----------|
| Tech Stack | [docs/tech-stack.md](docs/tech-stack.md) |
| Deployment Diagram | [docs/deployment.mmd](docs/deployment.mmd) |
| CI/CD Diagram | [docs/cicd-diagram.md](docs/cicd-diagram.md) |
| Security Docs | [SECURITY.md](SECURITY.md) |
| App Demo Script | [docs/demo/app-demo-script.md](docs/demo/app-demo-script.md) |
| CI/CD Demo Script | [docs/demo/cicd-demo-script.md](docs/demo/cicd-demo-script.md) |
| Screenshot Checklist | [docs/demo/screenshots-checklist.md](docs/demo/screenshots-checklist.md) |

---

## Project Overview (Original Design)

核心链路分为两个阶段：

1. **同步 Pipeline 阶段**：创建 `TaskContext` 后，`PipelineRunner` 按固定顺序执行多个 executor。
2. **结果汇总与完成判定**：`ResultCollectorExecutor` 汇总 shard 结果，`FinalizeTaskExecutor` 最终判定任务成功或失败。

---

## License

MIT
