package com.entity.mysql;

import lombok.Data;

import java.util.List;

@Data
public class TableStructure {
    private String tableName;
    private List<ColumnStructure> tableColumns;

    public TableStructure(String tableName, List<ColumnStructure> tableColumns) {
        this.tableName = tableName;
        this.tableColumns = tableColumns;
    }

}
