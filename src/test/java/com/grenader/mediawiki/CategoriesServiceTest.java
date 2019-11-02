package com.grenader.mediawiki;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoriesServiceTest {

    @Autowired
    CategoriesService service;

    @Autowired
    MediaWikiProcessor parsingService;

    @Autowired
    TemplateHandlingService templateService;

    @Test
    public void testGenerateListOfContents_two() throws IOException {
        List<Page> pages = Arrays.asList(new Page("", "", "", "cat1", ""),
                new Page("", "", "", "cat2", ""));

        String res = service.generateListOfContents(pages);

        assertEquals("\n" +
                "[[:Категория:cat1|cat1]]\n" +
                "\n" +
                "[[:Категория:cat2|cat2]]\n", res);
    }

    @Test
    public void testGenerateListOfContents_one_is_short() throws IOException {
        List<Page> pages = Arrays.asList(new Page("", "", "", "x", ""),
                new Page("", "", "", "cat2", ""));

        String res = service.generateListOfContents(pages);

        assertEquals("\n" +
                "[[:Категория:cat2|cat2]]\n", res);
    }

    @Test
    public void testGenerateListOfContents_two_unsorded() throws IOException {
        List<Page> pages = Arrays.asList(new Page("", "", "", "bbb", ""),
                new Page("", "", "", "aaa", ""));

        String res = service.generateListOfContents(pages);

        assertEquals("\n" +
                "[[:Категория:aaa|aaa]]\n" +
                "\n" +
                "[[:Категория:bbb|bbb]]\n", res);
    }

    @Test
    public void testGenerateListOfContents_two_with_duplicate() throws IOException {
        List<Page> pages = Arrays.asList(new Page("", "", "", "cat1", ""),
                new Page("", "", "", "cat2", ""),
                new Page("", "", "", "cat1", ""));

        String res = service.generateListOfContents(pages);

        assertEquals("\n" +
                "[[:Категория:cat1|cat1]]\n" +
                "\n" +
                "[[:Категория:cat2|cat2]]\n", res);
    }



    @Test
    public void testGenerateCategoryPages_two() throws IOException {
        List<Page> pages = Arrays.asList(new Page("", "", "", "cat1", ""),
                new Page("", "", "", "cat2", ""));

        String fullXML = service.generateAllCategoryPagesXML(pages);

        assertEquals("<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.9/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "           xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.9/ http://www.mediawiki.org/xml/export-0.9.xsd\"\n" +
                "           version=\"0.9\" xml:lang=\"ru\">\n" +
                "<page>\n" +
                "    <title>Категория:cat1</title>\n" +
                "    <revision>\n" +
                "        <contributor>\n" +
                "            <id>$adminUserId</id>\n" +
                "        </contributor>\n" +
                "        <text xml:space=\"preserve\" bytes=\"0\"/>\n" +
                "        <format>text/x-wiki</format>\n" +
                "    </revision>\n" +
                "</page>\n" +
                "<page>\n" +
                "    <title>Категория:cat2</title>\n" +
                "    <revision>\n" +
                "        <contributor>\n" +
                "            <id>$adminUserId</id>\n" +
                "        </contributor>\n" +
                "        <text xml:space=\"preserve\" bytes=\"0\"/>\n" +
                "        <format>text/x-wiki</format>\n" +
                "    </revision>\n" +
                "</page>\n" +
                "\n" +
                "</mediawiki>", fullXML);
    }


    @Test
    public void testGGenerateCategoryPages_realCall() throws IOException {

        List<Page> pages = parsingService.readExcelInputFile("allData20190914.xls", 0, 0, 10000);
        String fullXML = service.generateAllCategoryPagesXML(pages);

        FileUtils.writeStringToFile(new File("files/new-allCategoryPages.xml"), fullXML, "UTF-8");

    }



    @Test
    public void testGenerateListOfContents() throws IOException {

        List<Page> pages = Arrays.asList(new Page("", "", "", "cat1", ""),
                new Page("", "", "", "cat2", ""));

        String categoriesContent = service.generateListOfContents(pages);
        System.out.println("\n\n");
        System.out.println(categoriesContent);

        String listOfContentsXML = service.generateListOfContentsTemplatePage(categoriesContent);
        String fullXML = templateService.generateFullXML("pages", Collections.singletonList(listOfContentsXML));

        assertEquals("<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.9/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "           xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.9/ http://www.mediawiki.org/xml/export-0.9.xsd\"\n" +
                "           version=\"0.9\" xml:lang=\"ru\">\n" +
                "<page>\n" +
                "    <title>Шаблон:ListOfContent</title>\n" +
                "    <revision>\n" +
                "        <contributor>\n" +
                "            <id>$adminUserId</id>\n" +
                "        </contributor>\n" +
                "        <text xml:space=\"preserve\" bytes=\"112\">&lt;h1&gt;Разделы энциклопедии&lt;/h1&gt;\n" +
                "[[:Категория:cat1|cat1]]\n" +
                "\n" +
                "[[:Категория:cat2|cat2]]\n" +
                "</text>\n" +
                "        <format>text/x-wiki</format>\n" +
                "    </revision>\n" +
                "</page>\n" +
                "</mediawiki>", fullXML);

    }

    @Test
    public void testGenerateListOfContents_realCall() throws IOException {

        List<Page> pages = parsingService.readExcelInputFile("allData20190914.xls", 0, 0, 10000);
        String categoriesContent = service.generateListOfContents(pages);
        System.out.println("\n\n");
        System.out.println(categoriesContent);

        String listOfContentsXML = service.generateListOfContentsTemplatePage(categoriesContent);
        String fullXML = templateService.generateFullXML("pages", Collections.singletonList(listOfContentsXML));

        FileUtils.writeStringToFile(new File("files/new-listOfContents.xml"), fullXML, "UTF-8");

    }



}