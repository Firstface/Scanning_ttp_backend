package com.example.hivesampling.adapter;

import com.example.hivesampling.model.ShardTask;
import org.springframework.stereotype.Component;

@Component
public class MockQueryResultServiceAdapter implements QueryResultServiceAdapter {
    @Override
    public long collectRows(ShardTask shard) {
        return shard.sampledRows;
    }
}
