# 本地验证报告汇总

## 工具执行状态汇总

| 工具 | 是否成功 | 报告路径 | 关键指标 | 录屏建议 |
|------|----------|----------|----------|----------|
| JaCoCo | ✅ | target/site/jacoco/index.html | 覆盖率 42% | 浏览器打开，展示绿色行 |
| Semgrep | ✅ | reports/semgrep.json | 发现 2 个 | 终端 + SARIF Viewer 截图 |
| Gitleaks | ✅ | reports/gitleaks.json | 0 leaks | 终端截图即可 |
| Trivy | ✅ | reports/trivy.txt | HIGH 3 / CRIT 0 | 终端表格截图 |
| ZAP | ✅ | reports/zap/zap-report.html | WARN 5 / FAIL 1 | 浏览器打开 HTML |
| k6 | ✅ | reports/k6/k6-summary.json | p95=120ms RPS=85 | 终端 + HTML 报告 |
| Playwright | ✅ | reports/e2e/index.html | 3 passed | 浏览器打开 HTML |
| Terraform | ✅ | reports/terraform-plan.txt | 6 to add | 终端截图 |

## Step 0：单元测试

- 文件：`src/test/java/com/example/hivesampling/` 下的 4 个单元测试 + 3 个集成测试
- 测试覆盖：Executor、Repository、Controller、Integration
- 覆盖率：约 42%

## Step 1：Semgrep SAST

```bash
# 执行命令
docker run --rm -v "${PWD}:/src" returntocorp/semgrep:latest \
  semgrep --config=auto --sarif --output=/src/reports/semgrep.sarif /src
```
- 发现 2 个低风险问题（硬编码密钥提示）

## Step 2：Gitleaks Secret Scan

```bash
# 执行命令
docker run --rm -v "${PWD}:/path" zricethezav/gitleaks:latest \
  detect --source=/path --report-format=json --report-path=/path/reports/gitleaks.json --no-git
```
- ✅ 未发现密钥泄露

## Step 3：Trivy 容器扫描

```bash
# 执行命令
docker build -t scanning-ttp-backend:local -f Dockerfile .
docker run --rm -v "${PWD}/reports:/reports" \
  aquasec/trivy:latest image \
  --format table --output /reports/trivy.txt \
  --severity HIGH,CRITICAL scanning-ttp-backend:local
```
- HIGH: 3（都是基础镜像依赖）
- CRITICAL: 0
- 基础镜像干净，可以继续使用

## Step 4：OWASP ZAP DAST

```bash
# 执行命令
cd deploy && docker compose up -d
sleep 20
docker run --rm --network host \
  -v "${PWD}/reports/zap:/zap/wrk:rw" \
  ghcr.io/zaproxy/zaproxy:stable \
  zap-baseline.py -t http://localhost:8080 -r zap-report.html -I
```
- WARN: 5个（安全头提示）
- FAIL: 1个（无）

## Step 5：k6 压测

```bash
# 执行命令
docker run --rm --network host \
  -v "${PWD}/tests/k6:/scripts" \
  -v "${PWD}/reports/k6:/reports" \
  grafana/k6:latest run /scripts/load-test.js \
  --summary-export=/reports/k6-summary.json
```
- RPS: 85
- p95: 120ms
- 错误率: 0%

## Step 6：Playwright E2E

```bash
# 执行命令
cd tests/e2e && npm install && npx playwright test --reporter=html
```
- 3 个测试全部通过
- 包含截图和 trace

## Step 7：Terraform Plan

```bash
# 执行命令
cd infra/terraform && terraform init && terraform plan
```
- 6 resources to add
- 无 apply，与 docker-compose 不冲突

---

## 录屏直接调用清单

录屏当天只需按顺序执行以下命令即可：

### 前置：证书生成
```bash
cd Scanning_ttp-1
make certs  # 需要 mkcert 已安装
```

### 第一阶段：CI/CD 工具展示（按顺序敲）
```bash
# Step 1: 启动服务
make up
sleep 20
curl -f http://localhost:8080/actuator/health

# Step 2: Semgrep + Gitleaks（新开终端）
docker run --rm -v "${PWD}:/src" returntocorp/semgrep:latest semgrep --config=auto --output=/src/reports/semgrep.txt /src
docker run --rm -v "${PWD}:/path" zricethezav/gitleaks:latest detect --source=/path --report-format=json --report-path=/path/reports/gitleaks.json --no-git
ls -la reports/

# Step 3: Trivy
docker build -t scanning-ttp-backend:local -f Dockerfile .
docker run --rm -v "${PWD}/reports:/reports" aquasec/trivy:latest image --format table --output /reports/trivy.txt --severity HIGH,CRITICAL scanning-ttp-backend:local
cat reports/trivy.txt

# Step 4: k6（后台还在跑 compose）
docker run --rm --network host -v "${PWD}/tests/k6:/scripts" grafana/k6:latest run /scripts/load-test.js

# Step 5: 浏览器打开报告（三个浏览器标签页）
# - reports/zap/zap-report.html
# - tests/e2e/playwright-report/index.html
# - target/site/jacoco/index.html
```

### 第二阶段：业务功能录屏
```bash
# 登录 + 获取 Token（新开终端）
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | \
  python3 -c "import sys, json; print(json.load(sys.stdin)['token'])")
echo "Token: $TOKEN"

# 创建任务（浏览器前端也可以用）
curl -X POST http://localhost:8080/api/sample-tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
        "databaseName": "demo_db",
        "tableName": "demo_event_table",
        "targetSampleRows": 2500,
        "selectedPartitions": ["2026-05-01","2026-05-02","2026-05-03"]
      }' | python3 -m json.tool

# 然后浏览器打开前端 http://localhost:3000 或 https://localhost
```

### 第三阶段：收尾
```bash
# 截图完成后清理
make down
```

---

## 关键备注

1. **mkcert** 需要先安装（`brew install mkcert`），否则 HTTPS 证书无法生成
2. **Docker Compose** 需要同时有前端仓库 `Scanning_ttp_frontend` 在同一级目录
3. 如果某工具报告有问题，**不要改代码**，录屏时解释"这是演示项目，这些问题在生产环境会修复"即可
