package net.itzq.tool.mybatisgenerator.builder;

import net.itzq.tool.mybatisgenerator.util.StrUtil;
import net.itzq.tool.mybatisgenerator.DataConvert;
import net.itzq.tool.mybatisgenerator.TemplateLoader;

import java.util.Map;

import static net.itzq.tool.mybatisgenerator.DataConvert.INDENT;
import static net.itzq.tool.mybatisgenerator.DataConvert.NEWLINE;

/**
 *
 * @discription
 *
 * @created 2022/11/22 21:14
 */
public class EntityBuilder extends AbstractBuilder {


    @Override
    public String templateFileName() {
        return "entity.txt";
    }

    @Override
    public String convert() {

        Map<String, String> mapping = buildBaseMapping();

        String fields = convertDataType();
        mapping.put("fieldLine", fields);

        mapping.put("import", convertImportList());

        String content = TemplateLoader.convertTemplate(getTemplateFileName(), mapping);

        return content;
    }

    private String convertDataType() {
        StringBuilder line = new StringBuilder();

        for (Map<String, String> entity : column) {
            String fieldName = entity.get("column_name");
            String javaField = DataConvert.toLowerCamel(fieldName);// 转驼峰
            String comment = entity.get("column_comment");

            if (StrUtil.isNotBlank(comment)) {
                line.append(INDENT).append("/* ").append(NEWLINE);
                line.append(INDENT).append(" * ").append(comment).append(NEWLINE);
                line.append(INDENT).append(" */ ").append(NEWLINE);
            }

            String dbType = entity.get("data_type");
            line.append(INDENT)
                    .append("private")
                    .append(" ")
                    .append(DataConvert.mysqlDataTypeMapping(dbType, this.importList))
                    .append(" ")
                    .append(javaField)
                    .append(";")
                    .append(NEWLINE)
                    .append(NEWLINE);

        }

        return line.toString();
    }

}
