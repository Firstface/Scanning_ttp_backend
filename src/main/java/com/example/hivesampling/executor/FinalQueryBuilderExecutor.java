package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.SqlBuilderService;
import com.example.hivesampling.service.TaskLogService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Order(4)
@Component
public class FinalQueryBuilderExecutor implements SampleTaskExecutor {

    private final SqlBuilderService sqlBuilderService;
    private final TaskLogService taskLogService;

    public FinalQueryBuilderExecutor(SqlBuilderService sqlBuilderService, TaskLogService taskLogService) {
        this.sqlBuilderService = sqlBuilderService;
        this.taskLogService = taskLogService;
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        randomDelay();
        List<String> finalSqls = new ArrayList<>();
        for (ShardTask shard : context.shards) {
            shard.finalSql = sqlBuilderService.buildFinalSql(shard.innerSql);
            finalSqls.add(shard.finalSql);
        }
        context.finalSqls = finalSqls;
        taskLogService.info(context.taskId, "FinalQueryBuilderExecutor generated "
                + finalSqls.size() + " final SQLs");
        return ExecutorResult.success(
                name(),
                "Wrap inner SQL into final executable SQL",
                "finalSqls=" + finalSqls.size());
    }

    private void randomDelay() {
        try {
            Thread.sleep(500 + new Random().nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
