package com.auto;

import com.entity.mysql.ColumnStructure;
import com.entity.mysql.TableStructure;
import com.util.FileUtil;

import java.util.List;

public class EntityGenerator {

    public static void generateEntityClass(
            String entityName,
            String filePath,
            TableStructure tableStructure) {
        List<ColumnStructure> columns = tableStructure.getTableColumns();

        StringBuilder classContent = new StringBuilder();

        // Package declaration
        classContent.append("package com.entity;\n\n");

        // Import statements
        classContent.append("import java.util.Date;\n");
        classContent.append("import lombok.Getter;\n");
        classContent.append("import lombok.Setter;\n");
        classContent.append("import com.fasterxml.jackson.annotation.JsonFormat;\n");
        classContent.append("import org.springframework.format.annotation.DateTimeFormat;\n\n");

        // Class declaration
        classContent.append("@Getter\n");
        classContent.append("@Setter\n");
        classContent.append("public class ").append(entityName).append(" {\n");

        // Fields
        for (ColumnStructure column : columns) {
            String fieldName = column.getColumnName();
            String fieldType = getFieldType(column.getDataType());
            if (fieldType == "Date") {
                classContent.append("    @JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+8\")\n");
                classContent.append("    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n");
            }
            classContent.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
        }

        // Closing bracket
        classContent.append("}");

        // Write content to file
        String fileName = entityName + ".java";
        String fileContent = classContent.toString();
        FileUtil.FileWrite(fileContent, filePath, fileName);
    }

    private static String getFieldType(String line) {
        if (line.toLowerCase().contains("int")) {
            return "Integer";
        } else if (line.toLowerCase().contains("double")) {
            return "double";
        } else if (line.toLowerCase().contains("datetime")) {
            return "Date";
        } else {
            return "String";
        }
    }

}
