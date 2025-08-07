package net.itzq.tool.mybatisgenerator.builder;

import net.itzq.tool.mybatisgenerator.TemplateLoader;

import java.util.Map;

/**
 *  MallControllerBuilder
 *
 *  @author tangzq
 */
public class SimpleTextBuilder extends AbstractBuilder {

    @Override
    public String templateFileName() {
        return null;
    }

    @Override
    public String convert() {
        Map<String, String> mapping = buildBaseMapping();

        String content = TemplateLoader.convertTemplate(getTemplateFileName(), mapping);

        return content;
    }

}
