package com.example.hivesampling.adapter;

import com.example.hivesampling.model.TableMetadata;

public interface MetadataServiceAdapter {
    TableMetadata fetch(String databaseName, String tableName);
}
