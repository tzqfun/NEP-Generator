package net.itzq.tool.mybatisgenerator.builder;

import net.itzq.tool.mybatisgenerator.util.StrUtil;
import net.itzq.tool.mybatisgenerator.DataConvert;
import net.itzq.tool.mybatisgenerator.TemplateLoader;

import java.util.Map;

import static net.itzq.tool.mybatisgenerator.DataConvert.*;

/**
 *
 * @discription
 *
 * @created 2022/11/22 22:04
 */
public class MapperXmlBuilder extends AbstractBuilder {

    @Override
    public String templateFileName() {
        return "xml.txt";
    }

    public MapperXmlBuilder tableAlias(String tableAlias) {
        if (StrUtil.isBlank(tableAlias)) {
            tableAlias = "a";
        }
        this.tableAlias = tableAlias;
        return this;
    }

    @Override
    public String convert() {

        Map<String, String> mapping =  buildBaseMapping();

        mapping.put("tableAlias", tableAlias);    // 表别名

        String sqlColumns = convertSqlColumns();
        mapping.put("sqlColumns", sqlColumns);    // columns AS javaName

        String findWhere = convertSqlFindWhere();
        mapping.put("findWhere", findWhere);    //  IF test = ... != null or .. != ''

        //        String findOrderBy = INDENTx3 + "ORDER BY " + tableAlias + ".id";
        //        mapping.put("findOrderBy", findOrderBy);    //   order by a.id

        String insertSqlFields = convertInsertSqlFields();
        mapping.put("insertSqlFields", insertSqlFields); // INSERT INTO  (....)

        String insertJavaFields = convertInsertJavaFields();
        mapping.put("insertJavaFields", insertJavaFields); // VALUES ( ....)

        String updateFields = convertUpdateFields();
        mapping.put("updateFields", updateFields); // update .. set ... = #{...} , ... = #{...}

        String content = TemplateLoader.convertTemplate(getTemplateFileName(), mapping);

        return content;
    }

    /**
     * 转换updateFields
     */
    public String convertUpdateFields() {
        StringBuilder lines = new StringBuilder();

        for (Map<String,String> entity : column) {
            String fieldName = entity.get("column_name");
            String javaField = DataConvert.toLowerCamel(fieldName);    // 转驼峰

            lines.append(INDENTx3);
            lines.append("`").append(fieldName).append("`");
            lines.append("=").append("#{").append(javaField).append("}").append(",").append(NEWLINE);
        }

        if (lines.length() > 0) {
            lines.setLength(lines.length() - ("," + NEWLINE).length());
        }
        return lines.toString();
    }

    /**
     * 转换insertJavaFields
     */
    public String convertInsertJavaFields() {
        StringBuilder lines = new StringBuilder();

        for (Map<String,String> entity : column) {
            String fieldName = entity.get("column_name");
            String javaField = DataConvert.toLowerCamel(fieldName);    // 转驼峰

            lines.append(INDENTx3).append("#{").append(javaField).append("}").append(",").append(NEWLINE);
        }

        if (lines.length() > 0) {
            lines.setLength(lines.length() - ("," + NEWLINE).length());
        }
        return lines.toString();
    }

    /**
     * 转换insertSqlFields
     */
    public String convertInsertSqlFields() {
        StringBuilder lines = new StringBuilder();

        for (Map<String,String> entity : column) {
            String fieldName = entity.get("column_name");

            lines.append(INDENTx3);
            lines.append("`").append(fieldName).append("`");
            lines.append(",").append(NEWLINE);
        }

        if (lines.length() > 0) {
            lines.setLength(lines.length() - ("," + NEWLINE).length());
        }
        return lines.toString();
    }

    /**
     * 转换find where
     */
    public String convertSqlFindWhere() {
        StringBuilder lines = new StringBuilder();

        for (Map<String,String> entity : column) {
            String fieldName = entity.get("column_name");
            String javaField = DataConvert.toLowerCamel(fieldName);    // 转驼峰

            String ifBegin = "<if test='" + javaField + " != null and " + javaField + " != \"\"'>";
            String where = "AND " + tableAlias + "." + fieldName + " = #{" + javaField + "}";
            String ifEnd = "</if>";

            lines.append(INDENTx3).append(ifBegin).append(NEWLINE);
            lines.append(INDENTx4).append(where).append(NEWLINE);
            lines.append(INDENTx3).append(ifEnd).append(NEWLINE);
        }
        if (lines.length() == 0) {
            lines.append(INDENTx3).append(" 1 = 1 ").append(NEWLINE);
        }

        return lines.toString();
    }

    /**
     * 转换Sql列
     */
    public String convertSqlColumns() {

        StringBuilder str = new StringBuilder();

        StringBuilder lines = new StringBuilder();
        for (Map<String,String> entity : column) {
            str.setLength(0);
            String fieldName = entity.get("column_name");
            String javaField = DataConvert.toLowerCamel(fieldName);    // 转驼峰

            str.append(INDENT).append(INDENT);
            str.append(tableAlias).append(".");
            str.append("`").append(fieldName).append("`");
            str.append(" AS ");
            str.append("`").append(javaField).append("`,");
            str.append(NEWLINE);

            String line = str.toString();
            lines.append(line);
        }

        if (lines.length() > 0) {
            lines.setLength(lines.length() - ("," + NEWLINE).length());
        }

        return lines.toString();
    }

}
