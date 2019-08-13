import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.Collectors;

@RestController
@EnableAutoConfiguration
public class Example {

    public static final int SKIP_FIRST_LINES = 1;
    private final VelocityEngine ve;

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);

        perform();

    }

    public Example() {

        ve = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init(p);

    }

    public static void perform() {
        String file = "/Users/ikanshyn/IdeaProjects/SpringBoot/src/main/resources/bigData.xls";

        // readExcelInputFile(file);

    }

    List<Page> readExcelInputFile(String resourceFileName, int sheetNumber, int startingCount, int limitCount) {

        CategoryStrategy strategy = new KeywordsCategoryStrategy();


        List<Page> pages = new ArrayList();
        try {
            POIFSFileSystem fs = new POIFSFileSystem(this.getClass().getResourceAsStream(resourceFileName));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(sheetNumber);
            HSSFRow row;
            HSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) cols = tmp;
                }
            }

            int startingPoint = SKIP_FIRST_LINES + startingCount;
            for (int r = startingPoint; r < rows && r <= startingCount + limitCount; r++) {
                row = sheet.getRow(r);
//                System.out.println("row = " + row);
                if (row != null) {

                    Page page = new Page(row.getCell(1).getStringCellValue(),
                            row.getCell(2).getStringCellValue(),
                            row.getCell(3).getStringCellValue(),
                            row.getCell(7).getStringCellValue(),
                            removeExtraCommas(row.getCell(6).getStringCellValue()));

                    if (row.getCell(5) != null &&
                            !StringUtils.isEmpty(row.getCell(5).getStringCellValue())
                            && !"-".equals(row.getCell(5).getStringCellValue()))
                    // Video Answers
                    {
                        page.setVideo(getVideoId(row.getCell(5).getStringCellValue()));
                    }

                    pages.add(page);
/*
                    for (int c = 0; c < cols; c++) {
                        cell = row.getCell(c);
                        if (cell != null) {

                            System.out.println("TYPE = " + cell.getCellTypeEnum());
                            if (cell.getCellTypeEnum() == CellType.NUMERIC)
                                System.out.println(r + ":" + c + " = " + cell.getNumericCellValue());
                            else if (cell.getCellTypeEnum() == CellType.STRING)
                                System.out.println(r + ":" + c + " = " + cell.getStringCellValue());
                            else if (cell.getCellTypeEnum() == CellType.FORMULA)
                                System.out.println(r + ":" + c + " = " + cell.getCellFormula());
                            // Your code here

                    }
                      }*/
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return pages;
    }

    private String removeExtraCommas(String str) {
        if (StringUtils.isEmpty(str))
            return "";
        str = str.trim();
        if (str.startsWith(","))
            str = str.substring(1, str.length());
        if (str.endsWith(","))
            str = str.substring(0, str.length() - 1);
        return str.trim();
    }

    String getCategoryNameBySource(HSSFRow row) {
        return getCategoryName(row.getCell(6).getStringCellValue());
    }

    String getVideoId(String videoURL) {
        return videoURL.substring(videoURL.lastIndexOf("/") + 1, videoURL.length());
    }

    String getCategoryName(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, "~");
        if (tokenizer.countTokens() != 3)
            return "";
        tokenizer.nextToken();
        return tokenizer.nextToken(); // getting the second element
    }

    public String generateQandAPage(Page page) {
        // Old format: one category
        //        Template t = ve.getTemplate("wiki-one-QandA-xml.vm", "UTF-8");

        // New format: several categories
        Template t = ve.getTemplate("wiki-one-QandA-xml.vm", "UTF-8");


        VelocityContext context = new VelocityContext();

        context.put("pageName", page.getPageName());
        context.put("pageContent", page.getPageContent());
        context.put("pageSource", page.getSource());
        try {
            context.put("pageSourceEncrypted", URLEncoder.encode(page.getSource(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        context.put("pageKeywords", page.getKeywords());
        context.put("pageCategory", page.getPageCategory());

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }


    public String generateVideoPage(Page page) {
        Template t = ve.getTemplate("wiki-one-video-xml.vm", "UTF-8");

        VelocityContext context = new VelocityContext();

        context.put("pageName", page.getPageName());
        context.put("pageContent", page.getPageContent());
        context.put("pageSource", page.getSource());
        try {
            context.put("pageSourceEncrypted", URLEncoder.encode(page.getSource(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        context.put("pageKeywords", page.getKeywords());
        context.put("pageCategory", page.getPageCategory());

        context.put("pageVideoId", page.getVideo());

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }

    public String generateFullXML(List<String> pages) {
        /*  first, get and initialize an engine  */
        /*  next, get the Template  */
        Template t = ve.getTemplate("wiki-header-xml.vm", "UTF-8");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();

        context.put("pages", String.join("\n", pages));

        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return writer.toString();
    }


    public String generateHomePage(List<String> strings) {
        Set<String> cats = new TreeSet<>(strings);

        String allCategories = cats.stream().map(str -> "[[:Категория:" + str + "|" + str + "]]\n").collect(Collectors.joining("\n"));

        return "<h1>Разделы энциклопедии</h1>\n\n" + allCategories;
    }
}

