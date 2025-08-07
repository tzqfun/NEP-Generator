package net.itzq.tool.mybatisgenerator.builder;

import net.itzq.tool.mybatisgenerator.DataConvert;
import net.itzq.tool.mybatisgenerator.builder.entity.FieldData;
import net.itzq.tool.mybatisgenerator.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static net.itzq.tool.mybatisgenerator.DataConvert.NEWLINE;

/**
 *
 * @discription
 *
 * @created 2022/11/22 21:29
 */
public abstract class AbstractBuilder {

    String tableAlias = "a";

    String templateFileName;

    protected Set<String> importList = new HashSet<>();

    protected List<Map<String, String>> column;

    protected List<FieldData> fields;

    protected List<FieldData> vueFields;

    protected Map<Var, Object> variable = new LinkedHashMap<>();

    public AbstractBuilder() {
    }

    public void column(List<Map<String, String>> column) {
        this.column = column;
        if (this.column == null) {
            this.column = new ArrayList<>();
        }

        this.fields = convertFieldData();
        putVariable(Var.fields, this.fields);

        this.vueFields = new ArrayList<>();
        for (FieldData field : this.fields) {
            if (StrUtil.equalsAny(field.getJavaField(),
                    "id",
                    "createBy",
                    "createDate",
                    "updateBy",
                    "updateDate",
                    "delFlag")) {
                continue;
            }
            this.vueFields.add(field);
        }
        putVariable(Var.vueFields, this.vueFields);

    }

    protected String convertImportList() {
        StringBuilder line = new StringBuilder();
        for (String s : importList) {
            line.append("import").append(" ").append(s).append(";").append(NEWLINE);
        }
        return line.toString();
    }

    public abstract String templateFileName();

    public abstract String convert();

    public String convertTemplate() {
        preSetVariable();
        return convert();
    }

    /**
     * 转换前预设置变量
     */
    public void preSetVariable() {
        if (StringUtils.isNotBlank(varStringValue(Var.packageName))) {
            String last = StringUtils.substringAfterLast(varStringValue(Var.packageName), ".");
            if (StringUtils.isNotBlank(last)) {
                variable.put(Var.packageLastName, last);    // 最后一个包名
                variable.put(Var.permissionPrefix, last + ":" + varStringValue(Var.className));
            }
        }
        if (StringUtils.isBlank(varStringValue(Var.packageLastName))) {
            variable.put(Var.permissionPrefix, varStringValue(Var.className));
        }

        variable.put(Var.tableAlias,tableAlias);
    }

    public String getTemplateFileName() {

        if (StringUtils.isNotBlank(templateFileName())) {
            return templateFileName();
        }

        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public AbstractBuilder tableName(String tableName) {
        variable.put(Var.tableName, tableName);    // 表名
        return this;
    }

    public AbstractBuilder packageName(String packageName) {
        variable.put(Var.packageName, packageName);    // 包名
        return this;
    }

    public AbstractBuilder className(String className) {
        // 类名
        variable.put(Var.ClassName, getClassNameUpper(className));
        variable.put(Var.className, getClassNameLower(className));
        return this;
    }

    public Object variable(Var var) {
        return variable.get(var);
    }

    public Object putVariable(Var var, Object obj) {
        return variable.put(var, obj);
    }

    public String varStringValue(Var var) {
        return variable.containsKey(var) ? String.valueOf(variable.get(var)) : null;
    }

    public String getClassNameUpper(String className) {
        if (StrUtil.isNotBlank(className)) {
            return new StringBuilder(className).replace(0, 1, String.valueOf(className.charAt(0)).toUpperCase())
                    .toString();
        }
        return className;
    }

    public String getClassNameLower(String className) {
        if (StrUtil.isNotBlank(className)) {
            return new StringBuilder(className).replace(0, 1, String.valueOf(className.charAt(0)).toLowerCase())
                    .toString();
        }
        return className;
    }

    public Map<String, String> buildBaseMapping() {

        Map<String, String> mapping = new LinkedHashMap<>();

        for (Map.Entry<Var, Object> entry : variable.entrySet()) {
            Var key = entry.getKey();
            Object value = entry.getValue();

            mapping.put(key.getKey(), value == null ? null : value.toString());
        }

        return mapping;
    }

    private List<FieldData> convertFieldData() {

        List<FieldData> arr = new ArrayList<>();
        Set<String> importList = new LinkedHashSet<>();

        for (Map<String, String> entity : column) {
            String columnName = entity.get("column_name");
            String javaField = DataConvert.toLowerCamel(columnName);// 转驼峰
            String comment = entity.get("column_comment");
            if (StringUtils.isBlank(comment)) {
                comment = "";
            }
            String dbType = entity.get("data_type");
            String javaType = DataConvert.mysqlDataTypeMapping(dbType, importList);

            FieldData fieldData = new FieldData();
            fieldData.setColumnName(columnName);
            fieldData.setJavaField(javaField);
            fieldData.setColumnComment(comment);
            fieldData.setDbType(dbType);
            fieldData.setJavaType(javaType);

            arr.add(fieldData);
        }

        return arr;
    }
}
