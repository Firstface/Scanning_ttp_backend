# App Demo Script - 4.5 minutes

## Scene 1: Project Overview (0:00 - 0:30)

**Mouse Action**: Open IDE, show project structure

**Speak**: "This is the Hive Sampling Mock project - a demo showing how to use an Executor Pipeline architecture for real data systems like Hadoop and Spark."

**Show**:
- `/src/main/java` structure
- `/executor` folder with 7 executors
- `/controller` REST API

---

## Scene 2: API Demo (0:30 - 1:30)

**Mouse Action**: Open Postman/curl window

**Speak**: "Let's start by creating a sample task. The API takes a database, table, target rows, and partitions."

**Execute**:
```bash
curl -X POST http://localhost:8080/api/sample-tasks \
  -H "Content-Type: application/json" \
  -d '{
    "databaseName": "demo_db",
    "tableName": "user_events",
    "targetSampleRows": 2500,
    "selectedPartitions": ["2026-05-01", "2026-05-02", "2026-05-03"]
  }'
```

**Show**: Response with taskId

---

## Scene 3: Task Details & Pipeline (1:30 - 3:00)

**Mouse Action**: Open frontend or use curl to show pipeline endpoint

**Speak**: "Now watch the pipeline execute in real-time! Each executor does one job: RetrieveMetaInfos, PartitionSelector, Sampling, FinalQueryBuilder, QueryDispatcher, ResultCollector, and FinalizeTask."

**Poll**:
```bash
watch -n 1 "curl -s http://localhost:8080/api/sample-tasks/{taskId}/pipeline | python3 -m json.tool"
```

**Highlight**:
- Status changing from PENDING → RUNNING → SUCCESS
- Each executor's action and output summary
- PipelineState object structure

---

## Scene 4: Results Summary (3:00 - 4:00)

**Mouse Action**: Show task detail, shards, logs

**Speak**: "Great! Now we can see all the shards, the total sampled rows, and the full log. We hit our target of 2500 rows so the task marked SUCCESS!"

**Show**:
- `/api/sample-tasks/{taskId}`
- `/api/sample-tasks/{taskId}/shards`
- `/api/sample-tasks/{taskId}/logs`

---

## Scene 5: Architecture (4:00 - 4:30)

**Mouse Action**: Point to PipelineRunner code

**Speak**: "This design is powerful because each executor is independent, testable, and reusable. If we wanted to swap sampling strategies, we just plug in a different executor!"

**End**
