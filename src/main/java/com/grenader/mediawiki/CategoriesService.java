package com.grenader.mediawiki;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class CategoriesService {

    @Autowired
    TemplateHandlingService templateService;

    TreeSet<String> getSortedCategories(List<Page> pages) {
        return pages.stream().filter(name -> name.getPageCategory() != null && name.getPageCategory().length() > 1).map(Page::getPageCategory).distinct().collect(Collectors.toCollection(TreeSet::new));
    }

    public String generateListOfContents(List<Page> pages) {

        Set<String> sortedCategories = getSortedCategories(pages);

        StringBuilder res = new StringBuilder();
        for (String category : sortedCategories) {
            res.append("\n" + "[[:Категория:").append(category).append("|").append(category).append("]]\n");
        }
        return res.toString();
    }


    public String generateCategoryPage(String pageName) {
        Template t = templateService.getTemplate("wiki-category-page-xml.vm");

        VelocityContext context = new VelocityContext();

        context.put("pageTitle", pageName);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }

    public String generateListOfContentsTemplatePage(String content) {
        Template t = templateService.getTemplate("wiki-ListOfContents-xml.vm");

        VelocityContext context = new VelocityContext();

        context.put("allCategories", content);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }

    public String generateAllCategoryPagesXML(List<Page> pages) {

        TreeSet<String> sortedCategories = getSortedCategories(pages);

        StringBuilder res = new StringBuilder();

        for (String categoryName : sortedCategories) {
            res.append(generateCategoryPage("Категория:"+categoryName)).append("\n");
        }
//        String allCategories = cats.stream().map(str -> "[[:Категория:" + str + "|" + str + "]]\n").collect(Collectors.joining("\n"));

        return templateService.generateFullXML("pages",  Collections.singletonList(res.toString()));
    }
}
