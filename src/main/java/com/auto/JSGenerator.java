package com.auto;

import com.entity.mysql.ColumnStructure;
import com.entity.mysql.TableStructure;
import com.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class JSGenerator {

    public static void generatePageJS(
            TableStructure tableStructure,
            String filePath,
            String entityName) {
        List<ColumnStructure> columns = getTableColumnsWithoutId(tableStructure);
        StringBuilder jsBuilder = new StringBuilder();

        jsBuilder.append("var url;\n\n");

        jsBuilder.append("function reloadCurGrid() {\n");
        jsBuilder.append("    console.log(\"reloadCurGrid()\");\n");
        jsBuilder.append("    resetValue();\n");
        jsBuilder.append("    resetSelectValue();\n");
        jsBuilder.append("    search").append(entityName).append("();\n");
        jsBuilder.append("}\n\n");

        jsBuilder.append("function search").append(entityName).append("() {\n");

        jsBuilder.append("$(\"#dg\").datagrid('load', {");
        for (ColumnStructure column : columns) {
            if (column.isSearch()) {
                String columnName = column.getColumnName();
                String searchInputId = "search-" + columnName;
                jsBuilder.append("\"").append(columnName).append("\": $(\"#").append(searchInputId).append("\").val(),\n");
            }
        }
        jsBuilder.delete(jsBuilder.length() - 2, jsBuilder.length());
        jsBuilder.append("});\n");

        jsBuilder.append("}\n\n");

        jsBuilder.append("function open").append(entityName).append("AddDialog() {\n");
        jsBuilder.append("    $(\"#dlg\").dialog(\"open\").dialog(\"setTitle\", \"添加信息\");\n");
        jsBuilder.append("    url = \"/save").append(entityName).append("\";\n");
        jsBuilder.append("}\n\n");

        jsBuilder.append("function open").append(entityName).append("ModifyDialog() {\n");
        jsBuilder.append("    var selectedRows = $(\"#dg\").datagrid(\"getSelections\");\n");
        jsBuilder.append("    if (selectedRows.length != 1) {\n");
        jsBuilder.append("        $.messager.alert(\"系统提示\", \"请选择一条要编辑的数据！\");\n");
        jsBuilder.append("        return;\n");
        jsBuilder.append("    }\n");
        jsBuilder.append("    var row = selectedRows[0];\n");
        jsBuilder.append("    $(\"#dlg\").dialog(\"open\").dialog(\"setTitle\", \"编辑信息\");\n");
        jsBuilder.append("    $(\"#").append(entityName.toLowerCase()).append("Form\").form(\"load\", row);\n");
        jsBuilder.append("    url = \"/save").append(entityName).append("?id=\" + row.id;\n");
        jsBuilder.append("}\n\n");

        jsBuilder.append("function save").append(entityName).append("() {\n");
        jsBuilder.append("    var requestData = {\n");
        for (ColumnStructure column : columns) {
            String columnName = column.getColumnName();
            jsBuilder.append("        ").append(columnName).append(": ");
            if (column.getDataType().equals("DATETIME")) {
                jsBuilder.append("$(\"#").append(columnName).append("\").datetimebox(\"getValue\"),\n");
            } else {
                jsBuilder.append("$(\"#").append(columnName).append("\").val(),\n");
            }
        }
        jsBuilder.append("    };\n");
        jsBuilder.append("    console.log(requestData);\n");
        jsBuilder.append("    $.ajax({\n");
        jsBuilder.append("        url: url,\n");
        jsBuilder.append("        type: 'POST',\n");
        jsBuilder.append("        dataType: 'json',\n");
        jsBuilder.append("        data: requestData,\n");
        jsBuilder.append("        success: function (response) {\n");
        jsBuilder.append("            console.log(response);\n");
        jsBuilder.append("            if (response.success) {\n");
        jsBuilder.append("                resetValue();\n");
        jsBuilder.append("                $(\"#dlg\").dialog(\"close\");\n");
        jsBuilder.append("                $(\"#dg\").datagrid(\"reload\");\n");
        jsBuilder.append("                $.messager.alert(\"系统提示\", response.message);\n");
        jsBuilder.append("            } else {\n");
        jsBuilder.append("                $.messager.alert(\"系统提示\", response.message);\n");
        jsBuilder.append("            }\n");
        jsBuilder.append("        },\n");
        jsBuilder.append("        error: function (xhr, status, error) {\n");
        jsBuilder.append("            console.log('请求发生错误: ' + error);\n");
        jsBuilder.append("        }\n");
        jsBuilder.append("    });\n");
        jsBuilder.append("}\n\n");

        jsBuilder.append("function resetSelectValue() {\n");
        for (ColumnStructure column : columns) {
            if (column.isSearch()) {
                String columnName = column.getColumnName();
                String searchInputId = "search-" + columnName;
                jsBuilder.append("    $(\"#").append(searchInputId).append("\").val(\"\");\n");
            }
        }
        jsBuilder.append("}\n\n");

        jsBuilder.append("function resetValue() {\n");
        for (ColumnStructure column : columns) {
            String columnName = column.getColumnName();
            if (column.getDataType().equals("DATETIME")) {
                jsBuilder.append("    $(\"#").append(columnName).append("\").datetimebox(\"setValue\", \"\");\n");
            } else {
                jsBuilder.append("    $(\"#").append(columnName).append("\").val(\"\");\n");
            }
        }
        jsBuilder.append("}\n\n");

        jsBuilder.append("function close").append(entityName).append("Dialog() {\n");
        jsBuilder.append("    $(\"#dlg\").dialog(\"close\");\n");
        jsBuilder.append("    resetValue();\n");
        jsBuilder.append("}\n\n");

        jsBuilder.append("function delete").append(entityName).append("() {\n");
        jsBuilder.append("    var selectedRows = $(\"#dg\").datagrid(\"getSelections\");\n");
        jsBuilder.append("    if (selectedRows.length == 0) {\n");
        jsBuilder.append("        $.messager.alert(\"系统提示\", \"请选择要删除的数据！\");\n");
        jsBuilder.append("        return;\n");
        jsBuilder.append("    }\n");
        jsBuilder.append("    var strIds = [];\n");
        jsBuilder.append("    for (var i = 0; i < selectedRows.length; i++) {\n");
        jsBuilder.append("        strIds.push(selectedRows[i].id);\n");
        jsBuilder.append("    }\n");
        jsBuilder.append("    var ids = strIds.join(\",\");\n");
        jsBuilder.append("    $.messager.confirm(\"系统提示\", \"您确定要删除这<font color=red>\" + selectedRows.length + \"</font>条数据吗？\", function (r) {\n");
        jsBuilder.append("        if (r) {\n");
        jsBuilder.append("            $.post(\"/delete").append(entityName).append("\", {ids: ids}, function (result) {\n");
        jsBuilder.append("                if (result.success) {\n");
        jsBuilder.append("                    $.messager.alert(\"系统提示\", \"数据已成功删除！\");\n");
        jsBuilder.append("                    $(\"#dg\").datagrid(\"reload\");\n");
        jsBuilder.append("                } else {\n");
        jsBuilder.append("                    $.messager.alert(\"系统提示\", \"数据删除失败，请联系系统管理员！\");\n");
        jsBuilder.append("                }\n");
        jsBuilder.append("            }, \"json\");\n");
        jsBuilder.append("        }\n");
        jsBuilder.append("    });\n");
        jsBuilder.append("}\n");

        // Write the JS content to the file
        String fileName = entityName.toLowerCase() + "_info.js";
        String fileContent = jsBuilder.toString();
        FileUtil.FileWrite(fileContent, filePath, fileName);
    }

    private static List<ColumnStructure> getTableColumnsWithoutId(TableStructure tableStructure) {
        List<ColumnStructure> columns = tableStructure.getTableColumns();
        List<ColumnStructure> filteredColumns = new ArrayList<>();

        for (ColumnStructure column : columns) {
            if (!column.getColumnName().equalsIgnoreCase("id")) {
                filteredColumns.add(column);
            }
        }

        return filteredColumns;
    }

}

