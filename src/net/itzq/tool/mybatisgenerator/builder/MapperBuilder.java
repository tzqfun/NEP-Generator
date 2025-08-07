package net.itzq.tool.mybatisgenerator.builder;

import net.itzq.tool.mybatisgenerator.TemplateLoader;

import java.util.Map;

/**
 *
 * @discription
 *
 * @created 2022/11/22 21:14
 */
public class MapperBuilder extends AbstractBuilder {


    @Override
    public String templateFileName() {
        return "mapper.txt";
    }

    @Override
    public String convert() {
        Map<String, String> mapping = buildBaseMapping();

        String content = TemplateLoader.convertTemplate(getTemplateFileName(), mapping);

        return content;
    }

}
