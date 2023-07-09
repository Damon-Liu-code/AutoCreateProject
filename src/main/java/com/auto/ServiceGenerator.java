package com.auto;

import com.util.FileUtil;

public class ServiceGenerator {

    public static void generateServiceClass(
            String entityName,
            String filePath) {

        StringBuilder classContent = new StringBuilder();

        // Package declaration
        classContent.append("package com.service;\n\n");

        // Import statements
        classContent.append("import com.entity.").append(entityName).append(";\n");
        classContent.append("import java.util.List;\n");
        classContent.append("import java.util.Map;\n");

        // Class declaration
        classContent.append("public interface ").append(entityName).append("Service {\n");

        // Fields
        classContent.append("    int get").append(entityName).append("Total(Map<String, Object> map);\n");
        classContent.append("    List<").append(entityName).append("> get").append(entityName).append("Detail(Map<String, Object> map);\n");
        classContent.append("    int add").append(entityName).append("(").append(entityName).append(" ").append(entityName.toLowerCase()).append(");\n");
        classContent.append("    int update").append(entityName).append("(").append(entityName).append(" ").append(entityName.toLowerCase()).append(");\n");
        classContent.append("    int delete").append(entityName).append("(Integer id);\n");

        // Closing bracket
        classContent.append("}");

        // Write content to file
        String fileName = entityName + "Service.java";
        String fileContent = classContent.toString();
        FileUtil.FileWrite(fileContent, filePath, fileName);
    }

}
