package com.util;

import com.config.DatabaseConfig;
import com.entity.mysql.ColumnStructure;
import com.entity.mysql.TableStructure;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class DatabaseUtil {

    private static final Logger log = Logger.getLogger(DatabaseUtil.class);
    @Resource
    private DatabaseConfig databaseConfig;

    public static String extractSchemaName(String dbUrl) {
        Pattern pattern = Pattern.compile("jdbc:mysql://[^/]+/([^?]+)");
        Matcher matcher = pattern.matcher(dbUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public TableStructure getTableStructureByTableNameAndYML(String tableName) {
        try {
            // 通过yml文件里的配置建立数据库链接
            Connection connection = getMySQLConnectionFromYML();
            // 获取表的元数据
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取表的列信息
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            TableStructure tableStructure = new TableStructure(tableName, new ArrayList<>());
            while (columns.next()) {
                tableStructure.getTableColumns().add(
                        new ColumnStructure(
                                columns.getString("COLUMN_NAME"),
                                columns.getString("TYPE_NAME"),
                                columns.getInt("COLUMN_SIZE"),
                                columns.getInt("DECIMAL_DIGITS"),
                                columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable,
                                columns.getString("REMARKS")
                        )
                );
            }
            return tableStructure;

        } catch (SQLException e) {
            log.error(e);
        }
        return null;
    }

    public TableStructure getTableStructureByTableNameAndInput(String tableName, String username, String password, String url) {
        try {
            // 通过yml文件里的配置建立数据库链接
            Connection connection = getMySQLConnectionFromInput(username, password, url);
            //获取数据库信息
            String schemaName = extractSchemaName(url);
            // 获取表的元数据
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取表的列信息
            ResultSet columns = metaData.getColumns(schemaName, null, tableName, null);
            TableStructure tableStructure = new TableStructure(tableName, new ArrayList<>());
            while (columns.next()) {
                tableStructure.getTableColumns().add(
                        new ColumnStructure(
                                columns.getString("COLUMN_NAME"),
                                columns.getString("TYPE_NAME"),
                                columns.getInt("COLUMN_SIZE"),
                                columns.getInt("DECIMAL_DIGITS"),
                                columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable,
                                columns.getString("REMARKS")
                        )
                );
            }
            return tableStructure;

        } catch (SQLException e) {
            log.error(e);
        }
        return null;
    }

    public Connection getMySQLConnectionFromYML() {
        try {
            // MySQL数据库连接信息
            String url = databaseConfig.getUrl();
            String username = databaseConfig.getUsername();
            String password = databaseConfig.getPassword();

            Connection connection = DriverManager.getConnection(url, username, password);

            return connection;

        } catch (SQLException e) {
            log.error(e);
        }
        return null;
    }

    public Connection getMySQLConnectionFromInput(String username, String password, String url) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            log.error(e);
        }
        return null;
    }
}
