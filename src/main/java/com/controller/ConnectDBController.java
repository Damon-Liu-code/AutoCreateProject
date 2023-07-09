package com.controller;

import com.entity.mysql.ColumnStructure;
import com.entity.mysql.TableStructure;
import com.util.DatabaseUtil;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ConnectDBController {
    private static final Logger log = Logger.getLogger(ConnectDBController.class);
    @Resource
    private DatabaseUtil databaseUtil;

    @RequestMapping("/connectMySQLGetTableNames")
    public Map<String, Object> connectMySQLGetTableNames(@RequestParam("dbUrl") String dbUrl,
                                                         @RequestParam("username") String username,
                                                         @RequestParam("password") String password) {
        Map<String, Object> response = new HashMap<>();

        Connection connection = null;
        try {
            // 建立数据库连接
            connection = databaseUtil.getMySQLConnectionFromInput(username, password, dbUrl);

            // 创建用于执行SQL语句的Statement对象
            Statement statement = connection.createStatement();

            // 执行SQL查询，获取表名数据
            // 使用正则表达式提取数据库名称
            String dbName = "";
            String pattern = "/([^?/]+)\\?";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(dbUrl);
            if (m.find()) {
                dbName = m.group(1);
            }
            ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = '" + dbName + "'");

            // 存储表名数据的列表
            List<String> tableNames = new ArrayList<>();

            // 遍历结果集，获取表名数据
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                tableNames.add(tableName);
            }

            // 关闭结果集、Statement和连接
            resultSet.close();
            statement.close();
            connection.close();

            // 将表名数据返回给前端
            response.put("tableNames", tableNames);
        } catch (Exception e) {
            response.put("error", "Database connection error: " + e.getMessage());
        } finally {
            // 确保连接关闭
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    response.put("error", "Error while closing database connection: " + e.getMessage());
                }
            }
        }
        return response;
    }

    @RequestMapping(value = "/getColumnNameByTable")
    public Map<String, Object> getColumnNameByTable(@RequestParam String tableName,
                                                    @RequestParam("dbUrl") String dbUrl,
                                                    @RequestParam("username") String username,
                                                    @RequestParam("password") String password) {
        Map<String, Object> response = new HashMap<>();
        List<String> fieldNames = new ArrayList<>();
        try {
            // 根据所选表名执行数据库查询操作，获取字段信息
            TableStructure tableStructure = databaseUtil.getTableStructureByTableNameAndInput(
                    tableName, username, password, dbUrl
            );
            List<ColumnStructure> columns = tableStructure.getTableColumns();
            for (ColumnStructure column : columns) {
                String fieldName = column.getColumnName();
                if (fieldName.equals("id")) {
                    continue;
                }
                fieldNames.add(fieldName);
            }
            response.put("fieldNames", fieldNames);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

}
