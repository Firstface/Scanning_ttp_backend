# Hive Sampling Mock

这是一个面向教学演示的 Hive 表采样系统 Mock 项目，用于展示一个数据校验采样任务如何通过 executor 流水线完成元信息读取、分区选择、采样拆分、SQL 生成、异步执行和结果汇总。

项目不依赖真实 Hive、调度平台或数据库，所有任务、日志和状态都保存在内存中。

## 项目整体设计

核心链路分为两个阶段：

1. 同步 Pipeline 阶段：创建 `TaskContext` 后，`PipelineRunner` 按固定顺序执行多个 executor。
2. 异步执行与收敛阶段：`QueryDispatcherExecutor` 将 shard 置为 `QUEUED`，`ShardExecutionService` 模拟异步执行，`SampleTaskMonitorService` 定时收敛结果。

分层结构：

- `controller`：REST API 入口。
- `dto`：请求对象。
- `model`：父任务、子任务、状态和 executor 结果。
- `pipeline`：统一 executor 接口和流水线运行器。
- `executor`：每个采样阶段的 executor。
- `service`：任务服务、SQL 生成、异步执行、日志服务。
- `repository`：内存任务仓储。
- `monitor`：结果监听、累计、提前结束、续跑和失败判定。

## Executor 流水线设计

`PipelineRunner` 固定顺序执行：

1. `RetrieveMetaInfosExecutor`
2. `PartitionSelectorExecutor`
3. `SamplingExecutor`
4. `FinalQueryBuilderExecutor`
5. `QueryDispatcherExecutor`

统一接口：

```java
public interface SampleTaskExecutor {
    ExecutorResult execute(TaskContext context);
}
```

## 核心类结构

- `TaskContext`：父任务上下文，贯穿整个 pipeline。
- `ShardTask`：采样子任务，每个 shard 对应一组分区和一条 SQL。
- `ExecutorResult`：记录每个 executor 的执行结果。
- `PipelineRunner`：顺序驱动 executor。
- `ShardExecutionService`：模拟异步执行 shard。
- `SampleTaskMonitorService`：轮询 shard 结果并更新父任务。
- `InMemoryTaskRepository`：内存保存任务。

## TaskContext 设计

`TaskContext` 包含：

- `taskId`：任务 ID。
- `databaseName`：库名。
- `tableName`：表名。
- `targetSampleRows`：目标采样行数。
- `sampledRows`：已累计采样行数。
- `metadata`：表字段、分区字段、表类型。
- `selectedPartitions`：被选择的分区。
- `shards`：拆分后的 shard 子任务。
- `finalSqls`：最终执行 SQL。
- `status`：父任务状态。
- `pipelineResults`：各 executor 执行结果。

`ExecutorResult` 包含：

- `executorName`：当前阶段名称。
- `success`：该阶段是否成功。
- `action`：这一步做了什么。
- `outputSummary`：这一步产出了什么。
- `executedAt`：执行时间。

父任务状态：

- `CREATED`
- `PIPELINE_RUNNING`
- `DISPATCHED`
- `RUNNING`
- `SUCCESS`
- `FAILED`

子任务状态：

- `QUEUED`
- `RUNNING`
- `SUCCESS`
- `FAILED`
- `CANCELLED`

## Executor 职责与输入输出

### RetrieveMetaInfosExecutor

- 输入：`databaseName`、`tableName`
- 处理：Mock 表字段、分区字段和表类型。
- 输出：`metadata`

### PartitionSelectorExecutor

- 输入：请求中的 `selectedPartitions`
- 处理：如果请求未传分区，则使用内存 Mock 分区。
- 输出：`selectedPartitions`

### SamplingExecutor

- 输入：`selectedPartitions`
- 处理：每 5 个分区生成一个 shard，并生成 inner SQL。
- 输出：`shards`

inner SQL 示例：

```sql
SELECT *
FROM demo_db.demo_table
WHERE dt IN ('2026-05-01', '2026-05-02')
LIMIT 1000
```

续跑 SQL 示例：

```sql
SELECT *
FROM demo_db.demo_table
WHERE dt IN ('2026-05-01', '2026-05-02')
LIMIT 1000 OFFSET 1000
```

### FinalQueryBuilderExecutor

- 输入：每个 shard 的 inner SQL。
- 处理：包装成最终 SQL。
- 输出：`finalSqls`

最终 SQL 示例：

```sql
WITH table_rows AS (
SELECT *
FROM demo_db.demo_table
WHERE dt IN ('2026-05-01', '2026-05-02')
LIMIT 1000
)
INSERT INTO mock_result_table
SELECT * FROM table_rows
```

### QueryDispatcherExecutor

- 输入：`shards`、`finalSqls`
- 处理：将 shard 状态置为 `QUEUED`，父任务置为 `DISPATCHED`，提交异步执行。
- 输出：父任务进入 `RUNNING`，子任务开始异步运行。

### SampleTaskMonitorService

- 输入：异步 shard 执行结果。
- 处理：累计 `sampledRows`，判断是否达到目标。
- 输出：父任务 `SUCCESS` 或 `FAILED`，必要时推进下一轮采样。

## REST API 设计

### 创建并启动任务

```http
POST /api/sample-tasks
Content-Type: application/json
```

### 查看父任务

```http
GET /api/sample-tasks/{taskId}
```

### 查看子任务

```http
GET /api/sample-tasks/{taskId}/shards
```

### 查看 Pipeline 结果

```http
GET /api/sample-tasks/{taskId}/pipeline
```

返回值直接是 `ExecutorResult[]`，适合课堂上逐项讲解“做了什么、产出了什么”。

### 查看任务日志

```http
GET /api/sample-tasks/{taskId}/logs
```

## 运行方式

```bash
mvn spring-boot:run
```

## 演示样例请求

```bash
curl -X POST 'http://localhost:8080/api/sample-tasks' \
  -H 'Content-Type: application/json' \
  -d '{
    "databaseName": "demo_db",
    "tableName": "demo_event_table",
    "targetSampleRows": 2500,
    "selectedPartitions": [
      "2026-05-01",
      "2026-05-02",
      "2026-05-03",
      "2026-05-04",
      "2026-05-05",
      "2026-05-06",
      "2026-05-07",
      "2026-05-08",
      "2026-05-09",
      "2026-05-10",
      "2026-05-11",
      "2026-05-12"
    ]
  }'
```

查看日志：

```bash
curl 'http://localhost:8080/api/sample-tasks/{taskId}/logs'
```

查看子任务：

```bash
curl 'http://localhost:8080/api/sample-tasks/{taskId}/shards'
```

## 课堂讲解流程

1. 用户提交任务，系统创建 `TaskContext`，父任务状态为 `CREATED`。
2. `PipelineRunner` 开始执行，父任务状态变为 `PIPELINE_RUNNING`。
3. `RetrieveMetaInfosExecutor` Mock 表结构和分区字段。
4. `PartitionSelectorExecutor` 确定本次采样分区。
5. `SamplingExecutor` 将分区按 5 个一组拆成多个 shard，并生成 inner SQL。
6. `FinalQueryBuilderExecutor` 将 inner SQL 包装成最终 SQL。
7. `QueryDispatcherExecutor` 将 shard 子任务置为 `QUEUED` 并提交异步执行。
8. `ShardExecutionService` 模拟 SQL 执行，shard 状态从 `QUEUED` 变为 `RUNNING`，再变为 `SUCCESS`。
9. `SampleTaskMonitorService` 每秒收敛结果，成功一个 shard 就累计一次 `sampledRows`。
10. 如果达到 `targetSampleRows`，父任务提前 `SUCCESS`，未开始 shard 会被 `CANCELLED`。
11. 如果一轮结束但未达标，系统使用 `OFFSET` 生成下一轮 SQL，表达“续跑”概念。
12. 如果所有 shard 达到最大尝试次数仍未达标，父任务变为 `FAILED`。
