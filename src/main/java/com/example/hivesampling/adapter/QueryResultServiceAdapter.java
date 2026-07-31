package com.example.hivesampling.adapter;

import com.example.hivesampling.model.ShardTask;

public interface QueryResultServiceAdapter {
    long collectRows(ShardTask shard);
}
