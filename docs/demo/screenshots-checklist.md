# Screenshots Checklist for Capstone Demo

## App Demo Screenshots (All Backend-Focused)

| # | Description | Where to get | Notes |
|---|-------------|--------------|-------|
| 1 | IDE project structure - /executor folder | IDE | Show all 7 executors |
| 2 | API request - POST /api/sample-tasks | Terminal/Postman | Show curl command |
| 3 | API response - Task created with taskId | Terminal | Highlight taskId field |
| 4 | PipelineState - PENDING executors | curl -s /pipeline | Show all 7 greyed out |
| 5 | PipelineState - RUNNING first executor | curl -s /pipeline | Show blue + spinner |
| 6 | PipelineState - SUCCESS first executor | curl -s /pipeline | Show green + checkmark |
| 7 | PipelineState - All SUCCESS | curl -s /pipeline | All green, full pipeline |
| 8 | Task detail - ParentStatus SUCCESS | curl -s /task/{id} | Show sampledRows >= target |
| 9 | Shards list - All shards completed | curl -s /shards | Show partitionName, sampledRows |
| 10 | Logs timeline - Full pipeline log | curl -s /logs | Show timestamped entries |

## CI/CD Demo Screenshots

| # | Description | Where to get | Notes |
|---|-------------|--------------|-------|
| 11 | GitHub repo main page | GitHub | Show README badges |
| 12 | CI workflow triggered - Running | GitHub Actions tab | Show all stages |
| 13 | All CI stages green - Success | GitHub Actions | Show ✅ green checkmarks |
| 14 | Unit Test Results - JUnit report | CI Artifacts | Download and show |
| 15 | Coverage Report - JaCoCo HTML | CI Artifacts | Highlight %50+ |
| 16 | Trivy Scan Results - SARIF | CI Artifacts | Show no criticals |
| 17 | k6 Performance Report | CI Artifacts | Show load test graph |
| 18 | CD Workflow - Manual approval gate | GitHub Actions | Show "Waiting" status |
| 19 | Production Deploy - Success summary | GitHub Actions | Show summary table |
| 20 | Rollback Workflow - Ready to run | GitHub Actions | Show dispatch form |

## Bonus/Optional

| # | Description | Where to get |
|---|-------------|--------------|
| 21 | Terraform plan output | Terminal | terraform plan |
| 22 | Docker ps - Containers running | Terminal | docker-compose ps |
| 23 | SECURITY.md - STRIDE table | GitHub | Show security docs |
| 24 | Mermaid diagrams in docs | GitHub | Show CICD flow diagram |
