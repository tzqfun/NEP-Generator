package net.itzq.tool.mybatisgenerator;

import net.itzq.tool.mybatisgenerator.builder.*;
import org.reflections.Reflections;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @discription
 *
 * @created 2022/11/23 20:19
 */
public class Generator {

    ConvertConfig config;

    Map<String, Class<? extends AbstractBuilder>> builderMap = new LinkedHashMap<>();

    public Generator(ConvertConfig convertConfig) {
        this.config = convertConfig;

        Reflections reflections = new Reflections("net.itzq.tool.mybatis_generator.builder");
        Set<Class<? extends AbstractBuilder>> allClasses = reflections.getSubTypesOf(AbstractBuilder.class);
        for (Class<? extends AbstractBuilder> clazz : allClasses) {
            try {
                AbstractBuilder instance = clazz.getDeclaredConstructor().newInstance();
                String templateFileName = instance.getTemplateFileName();
                builderMap.put(templateFileName, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String genCode(String templateGroupDirName, String templateFileName, String templateContent) {
        try {

            List<Map<String, String>> colsList = config.getColsList();
            AbstractBuilder builder = new FreemarkerBuilder();


            builder.setTemplateFileName(templateFileName);
            builder.setTemplateContent(templateContent);

            builder.column(colsList);
            builder.className(config.getClassName());
            builder.tableName(config.getTableName());
            builder.packageName(config.getPackageName());

            return builder.convertTemplate();
        } catch (Exception e) {
            String exceptionInfo = getExceptionInfo(e);
            e.printStackTrace();
            return "失败 \r\n" + exceptionInfo;
        }
    }

    public String getExceptionInfo(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        ex.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
