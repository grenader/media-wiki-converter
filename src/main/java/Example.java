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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

@RestController
@EnableAutoConfiguration
public class Example {

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


    List<String> readKeywords(String keywordsResourceFileName) {
        InputStream inputStream = this.getClass().getResourceAsStream(keywordsResourceFileName);
        BufferedReader b = new BufferedReader(new InputStreamReader(inputStream));

        List<String> res = new ArrayList<>();
        String readLine = "";
        try {
            while ((readLine = b.readLine()) != null) {
                System.out.println(readLine);
                res.add(readLine.toLowerCase().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    List<Page> readExcelInputFile(String resourceFileName, int sheetNumber, int limitCount) {

        // Load keywords
        List<String> keywords = readKeywords("/keywords.txt");

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

            int startingPoint = 1;
            for (int r = startingPoint; r < rows && r <= limitCount; r++) {
                row = sheet.getRow(r);
                System.out.println("row = " + row);
                if (row != null) {

                    Page page = new Page(row.getCell(3).getStringCellValue(),
                            row.getCell(4).getStringCellValue(),
                            row.getCell(6).getStringCellValue(),
                            getNewCategoryNameByKeywords(row, keywords),
                            row.getCell(14).getStringCellValue());

                    if (row.getCell(12) != null &&
                            !StringUtils.isEmpty(row.getCell(12).getStringCellValue())
                            && !"-".equals(row.getCell(12).getStringCellValue()))
                    // Video Answers
                    {
                        page.setVideo(getVideoId(row.getCell(12).getStringCellValue()));
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

    String getCategoryNameBySource(HSSFRow row) {
        return getCategoryName(row.getCell(6).getStringCellValue());
    }

    String getNewCategoryNameByKeywords(HSSFRow row, List<String> keywords) {
        List<String> list = getListOfCategories(row.getCell(14).getStringCellValue());

        String res = "";
        for (String one : list) {
            // Skip some words
            if (!keywords.contains(one))
                continue;

            res += "[[Категория:" + one + "]]\n";
        }
        return res;
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

    List<String> getListOfCategories(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");

        List<String> res = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            res.add(tokenizer.nextToken().trim().toLowerCase());
        }
        return res;
    }

    public String generateQandAPage(Page page) {
        // Old format: one category
        //        Template t = ve.getTemplate("wiki-one-QandA-xml.vm", "UTF-8");

        // New format: several categories
        Template t = ve.getTemplate("wiki-one-QandA-milti-categories-xml.vm", "UTF-8");


        VelocityContext context = new VelocityContext();

        context.put("pageName", page.getPageName());
        context.put("pageContent", page.getPageContent());
        context.put("pageSource", page.getSource());
        context.put("pageKeywords", page.getKeywords());
        context.put("pageCategory", page.getPageCategory());

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }


    public String generateVideoPage(Page page) {
        Template t = ve.getTemplate("wiki-one-video-multi-categories-xml.vm", "UTF-8");

        VelocityContext context = new VelocityContext();

        context.put("pageName", page.getPageName());
        context.put("pageContent", page.getPageContent());
        context.put("pageSource", page.getSource());
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
}

