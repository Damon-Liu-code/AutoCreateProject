package com.auto;


import com.entity.mysql.ColumnStructure;
import com.entity.mysql.TableStructure;
import com.util.FileUtil;

public class ControllerGenerator {

    public static void generateControllerClass(
            TableStructure tableStructure,
            String filePath,
            String entityName) {
        StringBuilder classContent = new StringBuilder();

        // Package declaration
        classContent.append("package com.controller;\n\n");

        // Import statements
        classContent.append("import com.entity.").append(entityName).append(";\n");
        classContent.append("import com.entity.PageBean;\n");
        classContent.append("import com.entity.jsonEntity.DefaultDataResult;\n");
        classContent.append("import com.service.").append(entityName).append("Service;\n");
        classContent.append("import com.util.ResponseUtil;\n");
        classContent.append("import com.util.StringUtil;\n");
        classContent.append("import org.apache.log4j.Logger;\n");
        classContent.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
        classContent.append("import org.springframework.web.bind.annotation.RequestParam;\n");
        classContent.append("import org.springframework.web.bind.annotation.RestController;\n\n");
        classContent.append("import javax.annotation.Resource;\n");
        classContent.append("import javax.servlet.http.HttpServletResponse;\n");
        classContent.append("import java.util.HashMap;\n");
        classContent.append("import java.util.Map;\n\n");

        // Class declaration
        classContent.append("@RestController\n");
        classContent.append("public class ").append(entityName).append("Controller {\n\n");

        // Field declaration
        classContent.append("    @Resource\n");
        classContent.append("    private ").append(entityName).append("Service ").append(entityName.toLowerCase()).append("Service;\n");
        classContent.append("    private static final Logger log = Logger.getLogger(").append(entityName).append("Controller.class);\n\n");

        // soundList method
        classContent.append("    @RequestMapping(\"/").append(entityName.toLowerCase()).append("List\")\n");
        classContent.append("    public void ").append(entityName.toLowerCase()).append("List(@RequestParam(value = \"page\", required = false) String page, @RequestParam(value = \"rows\", required = false) String rows, ").append(entityName).append(" ").append(entityName.toLowerCase()).append(", HttpServletResponse response) throws Exception {\n");
        classContent.append("        try {\n");
        classContent.append("            PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));\n");
        classContent.append("            Map<String, Object> map = new HashMap<>();\n");

        // add search option
        for (ColumnStructure column : tableStructure.getTableColumns()) {
            if (column.isSearch()) {
                String propertyName = column.getColumnName();
                String searchLine = String.format(
                        "            map.put(\"%s\", StringUtil.formatLike(%s.get%s()));",
                        propertyName, entityName.toLowerCase(),
                        propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1)
                );
                classContent.append(searchLine).append("\n");
            }
        }

        classContent.append("            map.put(\"start\", pageBean.getStart());\n");
        classContent.append("            map.put(\"size\", pageBean.getPageSize());\n");
        classContent.append("            DefaultDataResult ddr = new DefaultDataResult();\n");
        classContent.append("            ddr.rows = ").append(entityName.toLowerCase()).append("Service.get").append(entityName).append("Detail(map);\n");
        classContent.append("            ddr.total = ").append(entityName.toLowerCase()).append("Service.get").append(entityName).append("Total(map);\n");
        classContent.append("            ResponseUtil.sendJsonResponse(response, ddr);\n");
        classContent.append("        } catch (Exception ex) {\n");
        classContent.append("            System.out.println(ex);\n");
        classContent.append("            log.error(ex);\n");
        classContent.append("            ResponseUtil.sendFailureResponse(response, \"操作失败\");\n");
        classContent.append("        }\n");
        classContent.append("    }\n\n");

        // saveSound method
        classContent.append("    @RequestMapping(\"/save").append(entityName).append("\")\n");
        classContent.append("    public void save").append(entityName).append("(").append(entityName).append(" ").append(entityName.toLowerCase()).append(", HttpServletResponse response) throws Exception {\n");
        classContent.append("        try {\n");
        classContent.append("            int resultTotal = 0;\n");
        classContent.append("            if (").append(entityName.toLowerCase()).append(" != null && ").append(entityName.toLowerCase()).append(".getId() == null) {\n");
        classContent.append("                resultTotal = ").append(entityName.toLowerCase()).append("Service.add").append(entityName).append("(").append(entityName.toLowerCase()).append(");\n");
        classContent.append("            } else {\n");
        classContent.append("                resultTotal = ").append(entityName.toLowerCase()).append("Service.update").append(entityName).append("(").append(entityName.toLowerCase()).append(");\n");
        classContent.append("            }\n");
        classContent.append("            if (resultTotal > 0) {\n");
        classContent.append("                ResponseUtil.sendSuccessResponse(response, \"操作成功\");\n");
        classContent.append("            } else {\n");
        classContent.append("                ResponseUtil.sendFailureResponse(response, \"操作失败\");\n");
        classContent.append("            }\n");
        classContent.append("        } catch (Exception ex) {\n");
        classContent.append("            System.out.println(ex);\n");
        classContent.append("            log.error(ex);\n");
        classContent.append("            ResponseUtil.sendFailureResponse(response, \"操作失败\");\n");
        classContent.append("        }\n");
        classContent.append("    }\n\n");

        // deleteSound method
        classContent.append("    @RequestMapping(\"/delete").append(entityName).append("\")\n");
        classContent.append("    public void delete").append(entityName).append("(@RequestParam(value = \"ids\") String ids, HttpServletResponse response) throws Exception {\n");
        classContent.append("        try {\n");
        classContent.append("            int resultTotal = 0;\n");
        classContent.append("            String[] idsStr = ids.split(\",\");\n");
        classContent.append("            for (int i = 0; i < idsStr.length; i++) {\n");
        classContent.append("                resultTotal += ").append(entityName.toLowerCase()).append("Service.delete").append(entityName).append("(Integer.parseInt(idsStr[i]));\n");
        classContent.append("            }\n");
        classContent.append("            if (resultTotal > 0) {\n");
        classContent.append("                ResponseUtil.sendSuccessResponse(response, \"操作成功\");\n");
        classContent.append("            } else {\n");
        classContent.append("                ResponseUtil.sendFailureResponse(response, \"操作失败\");\n");
        classContent.append("            }\n");
        classContent.append("        } catch (Exception ex) {\n");
        classContent.append("            System.out.println(ex);\n");
        classContent.append("            log.error(ex);\n");
        classContent.append("            ResponseUtil.sendFailureResponse(response, \"操作失败\");\n");
        classContent.append("        }\n");
        classContent.append("    }\n\n");

        // Closing bracket
        classContent.append("}");

        // Write content to file
        String fileName = entityName + "Controller.java";
        String fileContent = classContent.toString();
        FileUtil.FileWrite(fileContent, filePath, fileName);
    }
}
