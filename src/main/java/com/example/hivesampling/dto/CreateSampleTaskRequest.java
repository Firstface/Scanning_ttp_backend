package com.example.hivesampling.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class CreateSampleTaskRequest {

    @NotBlank
    public String databaseName;

    @NotBlank
    public String tableName;

    @Min(1)
    public long targetSampleRows = 3000;

    public List<String> selectedPartitions = new ArrayList<>();
}
