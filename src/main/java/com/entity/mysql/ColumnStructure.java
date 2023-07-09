package com.entity.mysql;

import lombok.Data;

@Data
public class ColumnStructure {
    private String columnName;
    private String dataType;
    private Integer columnSize;
    private Integer decimalDigits;
    private boolean isNullable;
    private String remarks;
    private boolean isShow;
    private boolean isSearch;

    public ColumnStructure(String columnName, String dataType, Integer columnSize, Integer decimalDigits, boolean isNullable, String remarks) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.columnSize = columnSize;
        this.decimalDigits = decimalDigits;
        this.isNullable = isNullable;
        this.remarks = remarks;
    }
}