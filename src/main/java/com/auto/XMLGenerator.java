package com.auto;

import com.entity.mysql.ColumnStructure;
import com.entity.mysql.TableStructure;
import com.util.FileUtil;

import java.util.List;

public class XMLGenerator {

    public static void generateMapperXML(
            TableStructure tableStructure,
            String filePath,
            String entityName) {
        StringBuilder xmlBuilder = new StringBuilder();

        // XML header
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        xmlBuilder.append("<!DOCTYPE mapper\n");
        xmlBuilder.append("        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n");
        xmlBuilder.append("        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        xmlBuilder.append("<mapper namespace=\"com.dao.").append(entityName).append("Dao\">\n\n");

        // Select getSoundTotal
        xmlBuilder.append("    <select id=\"get").append(entityName).append("Total\" resultType=\"int\">\n");
        xmlBuilder.append("        select count(*) from ").append(tableStructure.getTableName()).append("\n");
        xmlBuilder.append("        <where>\n");
        for (ColumnStructure column : tableStructure.getTableColumns()) {
            if (column.isSearch()) {
                xmlBuilder.append("            <if test=\"").append(column.getColumnName()).append("!=null");
                if (column.getDataType().equalsIgnoreCase("String")) {
                    xmlBuilder.append(" and !").append(column.getColumnName()).append(".isEmpty()\">\n");
                } else {
                    xmlBuilder.append("\">\n");
                }
                xmlBuilder.append("                and ").append(column.getColumnName()).append(" like #{")
                        .append(column.getColumnName()).append("}\n");
                xmlBuilder.append("            </if>\n");
            }
        }
        xmlBuilder.append("        </where>\n");
        xmlBuilder.append("    </select>\n\n");

        // Select getSoundDetail
        xmlBuilder.append("    <select id=\"get").append(entityName).append("Detail\" parameterType=\"Map\" resultType=\"").append(entityName).append("\">\n");
        xmlBuilder.append("        select * from ").append(tableStructure.getTableName()).append("\n");
        xmlBuilder.append("        <where>\n");
        for (ColumnStructure column : tableStructure.getTableColumns()) {
            if (column.isSearch()) {
                xmlBuilder.append("            <if test=\"").append(column.getColumnName()).append("!=null");
                if (column.getDataType().equalsIgnoreCase("String")) {
                    xmlBuilder.append(" and !").append(column.getColumnName()).append(".isEmpty()\">\n");
                } else {
                    xmlBuilder.append("\">\n");
                }
                xmlBuilder.append("                and ").append(column.getColumnName()).append(" like #{")
                        .append(column.getColumnName()).append("}\n");
                xmlBuilder.append("            </if>\n");
            }
        }
        xmlBuilder.append("        </where>\n");
        xmlBuilder.append("        <if test=\"start!=null and size!=null\">\n");
        xmlBuilder.append("            limit #{start},#{size}\n");
        xmlBuilder.append("        </if>\n");
        xmlBuilder.append("    </select>\n\n");

        // Insert addSound
        xmlBuilder.append("    <insert id=\"add").append(entityName).append("\" parameterType=\"").append(entityName).append("\">\n");
        xmlBuilder.append("        insert into ").append(tableStructure.getTableName()).append(" (");

        List<ColumnStructure> columns = tableStructure.getTableColumns();
        for (int i = 0; i < columns.size(); i++) {
            ColumnStructure column = columns.get(i);
            if (!column.getColumnName().equalsIgnoreCase("id")) {
                xmlBuilder.append(column.getColumnName());
                if (i != columns.size() - 1) {
                    xmlBuilder.append(", ");
                }
            }
        }
        xmlBuilder.append(")\n");
        xmlBuilder.append("        values (");
        for (int i = 0; i < columns.size(); i++) {
            ColumnStructure column = columns.get(i);
            if (!column.getColumnName().equalsIgnoreCase("id")) {
                xmlBuilder.append("#{").append(column.getColumnName()).append("}");
                if (i != columns.size() - 1) {
                    xmlBuilder.append(", ");
                }
            }
        }
        xmlBuilder.append(");\n");
        xmlBuilder.append("    </insert>\n\n");

        // Update updateSound
        xmlBuilder.append("    <update id=\"update").append(entityName).append("\" parameterType=\"").append(entityName).append("\">\n");
        xmlBuilder.append("        update ").append(tableStructure.getTableName()).append("\n");
        xmlBuilder.append("        <set>\n");
        for (ColumnStructure column : tableStructure.getTableColumns()) {
            if (column.getColumnName().equalsIgnoreCase("id")) {
                continue;  // Skip the id column
            }
            xmlBuilder.append("            <if test=\"").append(column.getColumnName()).append("!=null");
            if (column.getDataType().equalsIgnoreCase("String")) {
                xmlBuilder.append(" and !").append(column.getColumnName()).append(".isEmpty()\">\n");
            } else {
                xmlBuilder.append("\">\n");
            }
            xmlBuilder.append("                ").append(column.getColumnName()).append("=#{")
                    .append(column.getColumnName()).append("},\n");
            xmlBuilder.append("            </if>\n");
        }
        xmlBuilder.append("        </set>\n");
        xmlBuilder.append("        where id=#{id}\n");
        xmlBuilder.append("    </update>\n\n");

        // Delete deleteSound
        xmlBuilder.append("    <delete id=\"delete").append(entityName).append("\" parameterType=\"Integer\">\n");
        xmlBuilder.append("        delete\n");
        xmlBuilder.append("        from ").append(tableStructure.getTableName()).append("\n");
        xmlBuilder.append("        where id = #{id}\n");
        xmlBuilder.append("    </delete>\n\n");

        // Mapper closing tag
        xmlBuilder.append("</mapper>");

        // Write the XML content to the file
        String fileName = entityName + "Mapper.xml";
        String fileContent = xmlBuilder.toString();
        FileUtil.FileWrite(fileContent, filePath, fileName);
    }
}
