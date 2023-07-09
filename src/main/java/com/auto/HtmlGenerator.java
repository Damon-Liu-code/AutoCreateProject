package com.auto;

import com.entity.mysql.ColumnStructure;
import com.entity.mysql.TableStructure;
import com.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class HtmlGenerator {

    public static void generatePageHTML(
            TableStructure tableStructure,
            String filePath,
            String entityName) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<!DOCTYPE html>\n");
        htmlBuilder.append("<html lang=\"en\">\n");
        htmlBuilder.append("<head>\n");
        htmlBuilder.append("    <meta charset=\"UTF-8\">\n");
        htmlBuilder.append("    <title>").append(entityName).append("信息</title>\n");
        htmlBuilder.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/jquery-easyui-1.3.3/themes/scientific/easyui.css\">\n");
        htmlBuilder.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/jquery-easyui-1.3.3/themes/icon.css\">\n");
        htmlBuilder.append("    <script type=\"text/javascript\" src=\"/jquery-easyui-1.3.3/jquery.min.js\"></script>\n");
        htmlBuilder.append("    <script type=\"text/javascript\" src=\"/jquery-easyui-1.3.3/jquery.easyui.min.js\"></script>\n");
        htmlBuilder.append("    <script type=\"text/javascript\" src=\"/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js\"></script>\n");
        htmlBuilder.append("    <script type=\"text/javascript\" src=\"/jquery-easyui-1.3.3/jquery.edatagrid.js\"></script>\n");
        htmlBuilder.append("    <script type=\"text/javascript\" src=\"/js/common.js\"></script>\n");
        htmlBuilder.append("    <script type=\"text/javascript\" src=\"/js/").append(entityName.toLowerCase()).append("_info.js\"></script>\n");
        htmlBuilder.append("    <title>").append(entityName).append("信息</title>\n");
        htmlBuilder.append("</head>\n");
        htmlBuilder.append("<body style=\"margin: 1px\">\n");
        htmlBuilder.append("<table id=\"dg\" class=\"easyui-datagrid\" data-options=\"pageSize:50\"\n");
        htmlBuilder.append("       pagination=\"true\" rownumbers=\"true\"\n");
        htmlBuilder.append("       url=\"/").append(entityName.toLowerCase()).append("List\" fit=\"true\" toolbar=\"#tb\">\n");
        htmlBuilder.append("    <thead data-options=\"frozen:true\">\n");
        htmlBuilder.append("        <tr>\n");
        htmlBuilder.append("            <th field=\"cb\" checkbox=\"true\" align=\"center\"></th>\n");
        htmlBuilder.append("        </tr>\n");
        htmlBuilder.append("    </thead>\n");
        htmlBuilder.append("    <thead>\n");
        htmlBuilder.append("        <tr>\n");
        List<ColumnStructure> columns = getShowTableColumns(tableStructure);
        for (ColumnStructure column : columns) {
            String columnName = column.getColumnName();
            String columnCNName = column.getRemarks();
            if (!columnName.equalsIgnoreCase("id")) {
                htmlBuilder.append("            <th field=\"").append(columnName).append("\" width=\"180\" align=\"center\">").append(columnCNName).append("</th>\n");
            }
        }
        htmlBuilder.append("        </tr>\n");
        htmlBuilder.append("    </thead>\n");
        htmlBuilder.append("</table>\n");
        htmlBuilder.append("\n");
        htmlBuilder.append("<div id=\"tb\">\n");
        htmlBuilder.append("    <div>\n");
        htmlBuilder.append("        <a href=\"javascript:open").append(entityName).append("AddDialog()\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" plain=\"true\">创建</a>\n");
        htmlBuilder.append("        <a href=\"javascript:open").append(entityName).append("ModifyDialog()\" class=\"easyui-linkbutton\" iconCls=\"icon-edit\" plain=\"true\">修改</a>\n");
        htmlBuilder.append("        <a href=\"javascript:delete").append(entityName).append("()\" class=\"easyui-linkbutton\" iconCls=\"icon-remove\" plain=\"true\">删除</a>\n");
        htmlBuilder.append("    </div>\n");

        String searchHtml = generateSearchFormCode(tableStructure, entityName);
        htmlBuilder.append(searchHtml);

        htmlBuilder.append("</div>\n");
        htmlBuilder.append("\n");
        htmlBuilder.append("\n");
        htmlBuilder.append("<div id=\"dlg\" class=\"easyui-dialog\" style=\"width:700px;height:520px;padding: 10px 20px\"\n");
        htmlBuilder.append("     closed=\"true\" buttons=\"#dlg-buttons\">\n");
        htmlBuilder.append("\n");

        htmlBuilder.append("<form id=\"").append(entityName.toLowerCase()).append("Form\" method=\"post\">\n");
        htmlBuilder.append("    <table>\n");

        for (int i = 0; i < columns.size(); i += 2) {
            ColumnStructure column1 = columns.get(i);
            ColumnStructure column2 = i + 1 < columns.size() ? columns.get(i + 1) : null;

            // 处理第一个列
            if (!column1.getColumnName().equalsIgnoreCase("id")) {
                String columnName1 = column1.getColumnName();
                String columnCNName1 = column1.getRemarks();
                String inputType1 = column1.getDataType().equalsIgnoreCase("DATETIME") ? "datetimebox" : "text";

                htmlBuilder.append("        <tr>\n");
                htmlBuilder.append("            <td>").append(columnCNName1).append("：</td>\n");
                htmlBuilder.append("            <td>\n");
                htmlBuilder.append("                <input type=\"text\"  id=\"").append(columnName1).append("\" name=\"").append(columnName1).append("\" class=\"easyui-").append(inputType1).append("\" required=\"true\"");

                if (column1.getDataType().equalsIgnoreCase("DATETIME")) {
                    htmlBuilder.append(" data-options=\"valueField: 'value', textField: 'text', editable: false, panelHeight: 'auto'\"");
                } else {
                    htmlBuilder.append(" value=\"0\"");
                }

                htmlBuilder.append("/>\n");
                htmlBuilder.append("                &nbsp;<font color=\"red\">*</font>\n");
                htmlBuilder.append("            </td>\n");
            }

            // 处理第二个列
            if (column2 != null && !column2.getColumnName().equalsIgnoreCase("id")) {
                String columnName2 = column2.getColumnName();
                String columnCNName2 = column2.getRemarks();
                String inputType2 = column2.getDataType().equalsIgnoreCase("DATETIME") ? "datetimebox" : "text";

                htmlBuilder.append("            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n");
                htmlBuilder.append("            <td>").append(columnCNName2).append("：</td>\n");
                htmlBuilder.append("            <td>\n");
                htmlBuilder.append("                <input type=\"text\" id=\"").append(columnName2).append("\" name=\"").append(columnName2).append("\" class=\"easyui-").append(inputType2).append("\" required=\"true\"");

                if (column2.getDataType().equalsIgnoreCase("DATETIME")) {
                    htmlBuilder.append(" data-options=\"valueField: 'value', textField: 'text', editable: false, panelHeight: 'auto'\"");
                } else {
                    htmlBuilder.append(" value=\"0\"");
                }

                htmlBuilder.append("/>\n");
                htmlBuilder.append("                &nbsp;<font color=\"red\">*</font>\n");
                htmlBuilder.append("            </td>\n");
                htmlBuilder.append("        </tr>\n");
            } else {
                htmlBuilder.append("        </tr>\n");
            }
        }

        htmlBuilder.append("    </table>\n");
        htmlBuilder.append("</form>");


        htmlBuilder.append("\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("\n");
        htmlBuilder.append("<div id=\"dlg-buttons\">\n");
        htmlBuilder.append("    <a href=\"javascript:save").append(entityName).append("()\" class=\"easyui-linkbutton\" iconCls=\"icon-btn-ok\">保存</a>\n");
        htmlBuilder.append("    <a href=\"javascript:close").append(entityName).append("Dialog()\" class=\"easyui-linkbutton\" iconCls=\"icon-btn-cancel\">关闭</a>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</body>\n");
        htmlBuilder.append("</html>");

        // Write the HTML content to the file
        String fileName = entityName.toLowerCase() + "_info.html";
        String fileContent = htmlBuilder.toString();
        FileUtil.FileWrite(fileContent, filePath, fileName);
    }


    private static List<ColumnStructure> getShowTableColumns(TableStructure tableStructure) {
        List<ColumnStructure> columns = tableStructure.getTableColumns();
        List<ColumnStructure> filteredColumns = new ArrayList<>();

        for (ColumnStructure column : columns) {
            if (column.isShow()) {
                filteredColumns.add(column);
            }
        }

        return filteredColumns;
    }

    public static String generateSearchFormCode(TableStructure tableStructure, String entityName) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<div>\n");
        for (ColumnStructure column : tableStructure.getTableColumns()) {
            if (column.isSearch()) {
                String remarks = column.getRemarks();
                String columnName = column.getColumnName();

                htmlBuilder.append("&nbsp;").append(remarks).append("：&nbsp;<input type=\"text\" id=\"search-")
                        .append(columnName).append("\" size=\"20\" onkeydown=\"if(event.keyCode==13) search" + entityName + "()\"/>\n");
            }
        }
        htmlBuilder.append("<a href=\"javascript:search" + entityName + "()\" class=\"easyui-linkbutton\" iconCls=\"icon-search\" plain=\"true\">搜索</a>\n");
        htmlBuilder.append("</div>");
        return htmlBuilder.toString();
    }


}

