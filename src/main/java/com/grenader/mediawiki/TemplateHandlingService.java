package com.grenader.mediawiki;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Service
public class TemplateHandlingService {

    private final VelocityEngine ve;

    public TemplateHandlingService() {
        ve = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init(p);
    }

    public Template getTemplate(String templateName) {
        return ve.getTemplate(templateName, "UTF-8");
    }


    public String generateFullXML(String keywordName, List<String> contentItems) {
        /*  first, get and initialize an engine  */
        /*  next, get the Template  */
        Template t = getTemplate("wiki-header-xml.vm");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();

        context.put(keywordName, String.join("\n", contentItems));

        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return writer.toString();
    }



}
