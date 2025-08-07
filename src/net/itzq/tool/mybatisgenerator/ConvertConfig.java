package net.itzq.tool.mybatisgenerator;

import java.util.List;
import java.util.Map;

/**
 *
 * @discription
 *
 * @created 2022/11/23 19:10
 */
public class ConvertConfig {
    String exportPath;
    String dbURL;
    String dbUserName;
    String dbPassword;
    String dbName;
    String tableName;
    String className;
    String packageName;

    List<Map<String,String>> colsList;

    public ConvertConfig() {
        this.packageName = "";
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<Map<String,String>> getColsList() {
        return colsList;
    }

    public void setColsList(List<Map<String,String>> colsList) {
        this.colsList = colsList;
    }
}
