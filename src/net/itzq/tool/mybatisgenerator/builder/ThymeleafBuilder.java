package net.itzq.tool.mybatisgenerator.builder;

import net.itzq.tool.mybatisgenerator.TemplateLoader;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

/**
 *  ThymeleafBuilder
 *
 *  @author tangzq
 */
public class ThymeleafBuilder extends AbstractBuilder {

    @Override
    public String templateFileName() {
        return null;
    }

    @Override
    public String convert() {

        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        templateEngine.setTemplateResolver(resolver);

        Context context = new Context();

        for (Map.Entry<Var, Object> entry : variable.entrySet()) {
            String key = entry.getKey().getKey();
            context.setVariable(key, entry.getValue());
        }

        String strTemplate = TemplateLoader.readTemplateFile(getTemplateFileName());

        String processedTemplate = templateEngine.process(strTemplate, context);
        return processedTemplate;
    }
}
