package com.example.hivesampling.model;

import java.util.ArrayList;
import java.util.List;

public class TableMetadata {

    public List<String> columns = new ArrayList<>();
    public List<String> partitionColumns = new ArrayList<>();
    public String tableType;
}
