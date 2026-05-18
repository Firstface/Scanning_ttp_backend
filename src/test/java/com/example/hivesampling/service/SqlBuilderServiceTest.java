package com.example.hivesampling.service;

import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.TaskContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlBuilderServiceTest {

    private final SqlBuilderService sqlBuilderService = new SqlBuilderService();

    @Test
    void buildInnerSql_ShouldRenderPartitionsAndOffset() {
        TaskContext context = new TaskContext();
        context.databaseName = "demo_db";
        context.tableName = "sample_table";
        ShardTask shard = new ShardTask();
        shard.partitionGroup = List.of("2026-05-01", "2026-05-02");
        shard.plannedRowsPerRun = 500;
        shard.offset = 200;

        String sql = sqlBuilderService.buildInnerSql(context, shard);

        assertTrue(sql.contains("FROM demo_db.sample_table"));
        assertTrue(sql.contains("WHERE dt IN ('2026-05-01', '2026-05-02')"));
        assertTrue(sql.endsWith("LIMIT 500 OFFSET 200"));
    }

    @Test
    void buildFinalSql_ShouldWrapInnerSqlIntoInsertStatement() {
        String sql = sqlBuilderService.buildFinalSql("SELECT * FROM demo");

        assertTrue(sql.startsWith("WITH table_rows AS"));
        assertTrue(sql.contains("INSERT INTO mock_result_table"));
        assertTrue(sql.contains("SELECT * FROM table_rows"));
    }
}
