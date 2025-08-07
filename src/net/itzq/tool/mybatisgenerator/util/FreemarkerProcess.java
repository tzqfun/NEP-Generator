package net.itzq.tool.mybatisgenerator.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.itzq.tool.mybatisgenerator.TemplateLoader;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 *  PtLoader
 *
 *  @author tangzq
 */
public class FreemarkerProcess {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Template createTemplate(String templateContent) {
        return createTemplate(uuid(), templateContent);
    }

    public static Template createTemplate(String name, String templateContent) {
        Configuration config = new Configuration(Configuration.VERSION_2_3_30);
        config.setTemplateLoader(new StringTemplateLoader());

        try {
            return new Template(name, templateContent, config);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String process(String path) {
        return process(path, null);
    }

    public static String process(String path, Map params) {
        String templateString = TemplateLoader.readTemplateFile(path);
        Template template = FreemarkerProcess.createTemplate(templateString);
        if (params == null) {
            params = new LinkedHashMap();
        }
        return processTemplate(template, params);
    }

    public static String processTemplate(Template template, Map params) {
        if (template == null) {
            System.out.println("template is null");
            return "";
        }

        if (params == null) {
            params = new LinkedHashMap();
        }

        StringWriter stringWriter = new StringWriter();
        try {
            template.process(params, stringWriter);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return stringWriter.toString();
    }
}
