package com.controller;

import com.auto.*;
import com.entity.mysql.ColumnStructure;
import com.entity.mysql.TableStructure;
import com.util.DatabaseUtil;
import com.util.YamlUtil;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GenerateFileController {
    private static final Logger log = Logger.getLogger(GenerateFileController.class);

    @Resource
    private DatabaseUtil databaseUtil;

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public Map<String, Object> generateAllFile(
            @RequestBody Map<String, Object> requestData,
            HttpServletRequest servletRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            //连接数据库获得表的结构
            String selectedTable = (String) requestData.get("selectedTable");
            String entityName = (String) requestData.get("entityName");
            String dbUrl = (String) requestData.get("dbUrl");
            String username = (String) requestData.get("username");
            String password = (String) requestData.get("password");
            TableStructure tableStructure = databaseUtil.getTableStructureByTableNameAndInput(selectedTable, username, password, dbUrl);

            //根据用户勾选的值给表结构里的一些属性二次赋值
            List<String> selectedListDisplay = (List<String>) requestData.get("selectedListDisplay");
            List<String> selectedSearchBox = (List<String>) requestData.get("selectedSearchBox");
            List<ColumnStructure> columnStructures = tableStructure.getTableColumns();
            //设置isSelected和isSearch的值
            for (ColumnStructure column : columnStructures) {
                String columnName = column.getColumnName();
                column.setShow(selectedListDisplay.contains(columnName));
                column.setSearch(selectedSearchBox.contains(columnName));
            }

            //设置Session
            HttpSession session = servletRequest.getSession();
            int randomNumber = (int) (Math.random() * 900000) + 100000;
            session.setAttribute("entityName", entityName + randomNumber);

            //生成文件
            String basicPath = YamlUtil.getString("output.file_path");
            String filePath = basicPath + "/" + session.getAttribute("entityName");
            generateFiles(tableStructure, filePath, entityName);


            response.put("success", true);

        } catch (Exception e) {
            log.error(e);
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    private void generateFiles(TableStructure tableStructure, String filePath, String entityName) {
        try {
            EntityGenerator.generateEntityClass(entityName, filePath, tableStructure);
            DaoGenerator.generateDaoClass(entityName, filePath);
            ServiceGenerator.generateServiceClass(entityName, filePath);
            ServiceImplGenerator.generateServiceImplClass(entityName, filePath);
            ControllerGenerator.generateControllerClass(tableStructure, filePath, entityName);
            XMLGenerator.generateMapperXML(tableStructure, filePath, entityName);
            HtmlGenerator.generatePageHTML(tableStructure, filePath, entityName);
            JSGenerator.generatePageJS(tableStructure, filePath, entityName);
        } catch (Exception e) {
            log.error(e);
        }
    }

}
