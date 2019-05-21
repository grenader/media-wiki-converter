import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ikanshyn on 2017-07-11.
 */
public class ExampleTest {

    Example service = new Example();

    @Before
    public void setUp() {
    }

    @Test
    public void testGenerateOnePageBlock() {
        Page testPage = new Page("Title", "Content", "Source", "Cat1", "Keywords");

        String generateXML = service.generateQandAPage(testPage);

        System.out.println("generateQandAPage = " + generateXML);

        assertEquals("<page>\n" +
                "    <title>Title</title>\n" +
                "    <revision>\n" +
                "        <contributor>\n" +
                "            <id>$adminUserId</id>\n" +
                "        </contributor>\n" +
                "        <text xml:space=\"preserve\" bytes=\"112\">Content\n" +
                "\n" +
                "            Source\n" +
                "\n" +
                "            [[Категория:Cat1]]</text>\n" +
                "        <format>text/x-wiki</format>\n" +
                "    </revision>\n" +
                "</page>\n", generateXML);
    }

    @Test
    public void testGenerateOnePageBlock_likeReal() {
        Page testPage = new Page("AutoTest Page Title", "Auto Test Page Content. I am not a velocity guru but maybe you need to see if\n" +
                "velocity has a classpath loader? I think the file\n" +
                "loader reads from the file system so you will need to\n" +
                "use the ExternalContext getRequest and get the full\n" +
                "path to the file on the filesystem.I am not a velocity guru but maybe you need to see if\n" +
                "velocity has a classpath loader? I think the file\n" +
                "loader reads from the file system so you will need to\n" +
                "use the ExternalContext getRequest and get the full\n" +
                "path to the file on the filesystem.",
                "Фрагмент лекции А.Г. Хакимова ~Образование. Благотворительность. Сотрудничество~, 11.07.2015, Красноярск, Россия.",
                "Слон", "Хакимов,Алматы,писатель,психолог,художник,философ,проповедник,РАМ,Часть лекции,Видеоцитата,Видеоцитаты,Razum.KZ");

        String generateXML = service.generateQandAPage(testPage);

        System.out.println("generateQandAPage = " + generateXML);
    }

    @Test
    public void testGenerateFullXML() {

        ArrayList<String> pages = new ArrayList<String>();
        pages.add("<page>page1</page>");
        pages.add("<page>page2</page>");
        pages.add("<page>page3</page>");
        String fullXML = service.generateFullXML(pages);

        assertEquals("<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.9/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "           xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.9/ http://www.mediawiki.org/xml/export-0.9.xsd\"\n" +
                "           version=\"0.9\" xml:lang=\"ru\">\n" +
                "<page>page1</page>\n" +
                "<page>page2</page>\n" +
                "<page>page3</page>", fullXML);
    }

    @Test
    public void testRealExcelInputFile() {

        service.readExcelInputFile("data-test.xls", 0, 20);

        String fullXML = "";

        assertEquals("", fullXML);
    }


    @Test
    public void testRealExcelInputFile_realData() {

        service.readExcelInputFile("bigData.xls", 0, 1200);

        String fullXML = "";

        assertEquals("", fullXML);
    }


    @Test
    public void testGetCategoryName_Ok() {
        String categoryName = service.getCategoryName("Фрагмент передачи ~Разумный диалог. Павел Лунгин и Александр Хакимов~, 17.11.2016, Москва, Россия.");
        assertEquals("Разумный диалог. Павел Лунгин и Александр Хакимов", categoryName);
    }

    @Test
    public void testGetVideoId_Ok() {
        String videoId = service.getVideoId("http://youtu.be/9Ys9FymfNMc");
        assertEquals("9Ys9FymfNMc", videoId);
    }

    @Test
    public void testGetCategoryName_Empty() {
        String categoryName = service.getCategoryName("");
        assertEquals("", categoryName);
    }

    @Test
    public void testGenerateXML() throws IOException {

        int maxNumberOfLines = 1200;

        List<Page> pages = service.readExcelInputFile("bigData.xls", 0, maxNumberOfLines);

        List<String> pagesXML = new ArrayList<>();
        for (Page page : pages) {
            String onePageBlock;
            if (page.getVideo() == null)
                onePageBlock = service.generateQandAPage(page);
            else
                onePageBlock = service.generateVideoPage(page);
            pagesXML.add(onePageBlock);
        }

        String fullXML = service.generateFullXML(pagesXML);

        FileUtils.writeStringToFile(new File("files/new-everything-" + maxNumberOfLines + ".xml"), fullXML, "UTF-8");

        System.out.println("\n\n");
        System.out.println(fullXML);
    }


    @Test
    public void testReadKeywords() throws IOException {

        List<String> strings = service.readKeywords("/keywords.txt");

        assertEquals(199, strings.size());

        assertEquals("абсолютная истина", strings.get(0));
        assertEquals("алкоголизм", strings.get(1));



    }

}