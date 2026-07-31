package com.example.hivesampling.adapter;

import com.example.hivesampling.model.TableMetadata;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MockMetadataServiceAdapter implements MetadataServiceAdapter {
    @Override
    public TableMetadata fetch(String databaseName, String tableName) {
        TableMetadata metadata = new TableMetadata();
        metadata.columns = List.of("user_id", "event_name", "event_time", "device_type", "dt");
        metadata.partitionColumns = List.of("dt");
        metadata.tableType = "MANAGED_TABLE";
        return metadata;
    }
}
