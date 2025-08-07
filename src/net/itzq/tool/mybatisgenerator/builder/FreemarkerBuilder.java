package net.itzq.tool.mybatisgenerator.builder;

import net.itzq.tool.mybatisgenerator.util.FreemarkerProcess;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  ThymeleafBuilder
 *
 *  @author tangzq
 */
public class FreemarkerBuilder extends AbstractBuilder {

    @Override
    public String templateFileName() {
        return null;
    }

    @Override
    public String convert() {

        Map context = new LinkedHashMap();

        for (Map.Entry<Var, Object> entry : variable.entrySet()) {
            String key = entry.getKey().getKey();
            context.put(key, entry.getValue());
        }

        String processedTemplate = FreemarkerProcess.process(getTemplateFileName(), context);
        return processedTemplate;
    }
}
