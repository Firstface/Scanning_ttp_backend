package com.example.hivesampling.service;

import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.TaskContext;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SqlBuilderService {

    public String buildInnerSql(TaskContext context, ShardTask shard) {
        String partitions = shard.partitionGroup.stream()
                .map(value -> "'" + value + "'")
                .collect(Collectors.joining(", "));
        String offsetPart = shard.offset > 0 ? " OFFSET " + shard.offset : "";
        return "SELECT *\n"
                + "FROM " + context.databaseName + "." + context.tableName + "\n"
                + "WHERE dt IN (" + partitions + ")\n"
                + "LIMIT " + shard.plannedRowsPerRun + offsetPart;
    }

    public String buildFinalSql(String innerSql) {
        return "WITH table_rows AS (\n"
                + innerSql + "\n"
                + ")\n"
                + "INSERT INTO mock_result_table\n"
                + "SELECT * FROM table_rows";
    }
}
