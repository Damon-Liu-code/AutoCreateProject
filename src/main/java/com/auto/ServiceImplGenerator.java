package com.auto;

import com.util.FileUtil;

public class ServiceImplGenerator {

    public static void generateServiceImplClass(
            String entityName,
            String filePath) {

        StringBuilder classContent = new StringBuilder();

        // Package declaration
        classContent.append("package com.service.impl;\n\n");

        // Import statements
        classContent.append("import com.dao.").append(entityName).append("Dao;\n");
        classContent.append("import com.entity.").append(entityName).append(";\n");
        classContent.append("import com.service.").append(entityName).append("Service;\n");
        classContent.append("import org.springframework.stereotype.Service;\n");
        classContent.append("import org.springframework.transaction.annotation.Transactional;\n");
        classContent.append("import javax.annotation.Resource;\n");
        classContent.append("import java.util.List;\n");
        classContent.append("import java.util.Map;\n\n");

        // Class declaration
        classContent.append("@Transactional\n");
        classContent.append("@Service(\"").append(entityName).append("Service\")\n");
        classContent.append("public class ").append(entityName).append("ServiceImpl implements ").append(entityName).append("Service {\n\n");

        // Field declaration
        classContent.append("    @Resource\n");
        classContent.append("    private ").append(entityName).append("Dao ").append(entityName.toLowerCase()).append("Dao;\n\n");

        // Method implementations
        classContent.append("    @Override\n");
        classContent.append("    public List<").append(entityName).append("> get").append(entityName).append("Detail(Map<String, Object> map) {\n");
        classContent.append("        return ").append(entityName.toLowerCase()).append("Dao.get").append(entityName).append("Detail(map);\n");
        classContent.append("    }\n\n");

        classContent.append("    @Override\n");
        classContent.append("    public int get").append(entityName).append("Total(Map<String, Object> map) {\n");
        classContent.append("        return ").append(entityName.toLowerCase()).append("Dao.get").append(entityName).append("Total(map);\n");
        classContent.append("    }\n\n");

        classContent.append("    @Override\n");
        classContent.append("    public int add").append(entityName).append("(").append(entityName).append(" ").append(entityName.toLowerCase()).append(") {\n");
        classContent.append("        return ").append(entityName.toLowerCase()).append("Dao.add").append(entityName).append("(").append(entityName.toLowerCase()).append(");\n");
        classContent.append("    }\n\n");

        classContent.append("    @Override\n");
        classContent.append("    public int update").append(entityName).append("(").append(entityName).append(" ").append(entityName.toLowerCase()).append(") {\n");
        classContent.append("        return ").append(entityName.toLowerCase()).append("Dao.update").append(entityName).append("(").append(entityName.toLowerCase()).append(");\n");
        classContent.append("    }\n\n");

        classContent.append("    @Override\n");
        classContent.append("    public int delete").append(entityName).append("(Integer id) {\n");
        classContent.append("        return ").append(entityName.toLowerCase()).append("Dao.delete").append(entityName).append("(id);\n");
        classContent.append("    }\n\n");

        // Closing bracket
        classContent.append("}");

        // Write content to file
        String fileName = entityName + "ServiceImpl.java";
        String fileContent = classContent.toString();
        FileUtil.FileWrite(fileContent, filePath, fileName);
    }

}
