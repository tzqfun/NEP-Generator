package net.itzq.tool.mybatisgenerator.builder.entity;


/**
 *  FieldData
 *
 *  @author tangzq
 */

public class FieldData {

    String columnName; // 列名
    String javaField; // java驼峰列名
    String columnComment; // 列注释
    String dbType; // 数据库类型
    String javaType; // java类型

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getJavaField() {
        return javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}
