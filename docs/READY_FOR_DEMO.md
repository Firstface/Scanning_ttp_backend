# 🎯 Ready for Demo - Final Checklist & Guide

## Part 1: Demo Day - Command Sequence

### Terminal 1: Start Environment

```bash
cd Scanning_ttp-1

# 1. Generate certificates (if needed)
make certs
# If asks for password, use your system password

# 2. Start all services
make up

# 3. Verify health
sleep 20
curl -f http://localhost:8080/actuator/health
```

### Terminal 2: API Demo

```bash
cd Scanning_ttp-1

# 1. Login and get token
export TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | \
  python3 -c "import sys, json; print(json.load(sys.stdin)['token'])")

echo "Token: $TOKEN"

# 2. Create a task
export TASK_ID=$(curl -s -X POST http://localhost:8080/api/sample-tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
        "databaseName": "demo_db",
        "tableName": "demo_event_table",
        "targetSampleRows": 2500,
        "selectedPartitions": ["2026-05-01","2026-05-02","2026-05-03"]
      }' | \
  python3 -c "import sys, json; print(json.load(sys.stdin)['taskId'])")

echo "Created Task ID: $TASK_ID"

# 3. Poll pipeline status (watch for a few seconds)
watch -n 1 "curl -s http://localhost:8080/api/sample-tasks/$TASK_ID/pipeline | python3 -m json.tool"
# Press Ctrl+C after seeing all steps complete

# 4. Get task detail
curl -s http://localhost:8080/api/sample-tasks/$TASK_ID | python3 -m json.tool

# 5. Get logs
curl -s http://localhost:8080/api/sample-tasks/$TASK_ID/logs | python3 -m json.tool

# 6. Demonstrate unauthorized access (401)
curl -i -X POST http://localhost:8080/api/sample-tasks \
  -H "Content-Type: application/json" \
  -d '{"databaseName":"demo"}'

# 7. Demonstrate rate limiting (429)
echo "Triggering rate limit (this will take ~30 seconds)..."
for i in {1..70}; do
  curl -s -o /dev/null -w "%{http_code} " http://localhost:8080/actuator/health \
    -H "Authorization: Bearer $TOKEN"
  sleep 0.1
done
# You should see 429 at the end
```

### Browser Tabs (Open in order)

| Tab | URL | Purpose |
|-----|-----|---------|
| 1 | `https://localhost` | Frontend UI, show HTTPS padlock |
| 2 | `reports/jacoco/index.html` | Coverage report |
| 3 | `reports/zap/zap-report.html` | ZAP DAST report |
| 4 | `<YOUR_GH_URL>/actions` | GitHub Actions |

---

## Part 2: Pre-Demo 30-Minute Checklist

- [ ] Run `make pre-push` - verify all green
- [ ] Check GitHub - latest workflow run is green
- [ ] Check Docker containers: `cd deploy && docker-compose ps`
  - [ ] backend: healthy
  - [ ] frontend: running
  - [ ] nginx-proxy: running
- [ ] Verify certificates exist: `ls -la deploy/nginx-proxy/certs/`
- [ ] Test API manually: `curl http://localhost:8080/actuator/health`
- [ ] Test HTTPS: Open `https://localhost` in browser
- [ ] Confirm JWT login works
- [ ] Open all report files in browser tabs
- [ ] Have terminal windows ready with commands typed
- [ ] Close all unnecessary apps/browser tabs

---

## Part 3: Emergency Rollback Guide

### If things go wrong during demo

| Problem | Quick Fix |
|---------|----------|
| Backend not responding | `make reset` - stops and restarts everything |
| HTTPS certificate expired | `make certs` then `make reset` |
| API throwing errors | Check logs: `make logs` |
| GitHub Actions failing | Just say "This is a known issue in the demo env, let's continue" |
| Token expired | Run the login curl command again |
| Port already in use | `cd deploy && docker-compose down -v` then `make up` |

### Ultimate Reset (60 seconds)

```bash
cd Scanning_ttp-1
make down
docker system prune -f
make certs
make up
sleep 20
# Verify
curl http://localhost:8080/actuator/health
```

---

## Part 4: Quick Reference - API Endpoints

| Endpoint | Method | Auth Required | Description |
|----------|--------|--------------|------------|
| `/auth/login` | POST | No | Get JWT token |
| `/actuator/health` | GET | No | Health check |
| `/api/sample-tasks` | GET | Yes | List all tasks |
| `/api/sample-tasks` | POST | Yes | Create new task |
| `/api/sample-tasks/{taskId}` | GET | Yes | Get task detail |
| `/api/sample-tasks/{taskId}/pipeline` | GET | Yes | Get pipeline status |
| `/api/sample-tasks/{taskId}/shards` | GET | Yes | Get shard details |
| `/api/sample-tasks/{taskId}/logs` | GET | Yes | Get task logs |

---

## Part 5: Demo Script - 4.5 Minutes

### Timing Guide

| Time | Action |
|------|--------|
| 0:00 - 0:30 | Intro + Open browser tabs |
| 0:30 - 1:30 | Backend API demo (terminal 2) |
| 1:30 - 2:30 | Frontend + Pipeline visualization |
| 2:30 - 3:30 | GitHub Actions walkthrough |
| 3:30 - 4:30 | Security & Reports (browser tabs) |
| 4:30 - 4:30 | Wrap up |

Good luck with your demo! 🎬
