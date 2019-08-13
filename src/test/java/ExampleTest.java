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

    @Test
    public void testRealExcelInputFile_oneLine() {
        List<Page> pages = service.readExcelInputFile("bigData20190912.xls", 0, 0, 1);

        assertEquals(1, pages.size());
        Page page = pages.get(0);
        assertEquals("Почему духовное мудрое знание может не проникать в сердце, не возникает желание следовать этому?", page.getPageName());
        assertEquals("Сначала нужно отрегулировать свою жизнь, очистить сердце. Когда мы начинаем это делать, духовное знание проникает в сердце. Как для получения информации радиоприёмник сначала нужно настроить на определённую волну. Так и нам нужно отрегулировать свой ум и чувства, чтобы мы воспринимали духовное знание глубже.",
                page.getPageContent());
        assertEquals("Развитие человека и самопознание", page.getPageCategory());
        assertEquals("сердце, желание", page.getKeywords());
        assertEquals("3O5BSKjUZRc", page.getVideo());
        assertEquals("Фрагмент лекции Александра Геннадьевича Хакимова ~Иллюзия и реальность~, 27.12.2015, г. Екатеринбург, Россия.", page.getSource());
    }

    @Test
    public void testRealExcelInputFile_realBigData() {
        service.readExcelInputFile("bigData20190912.xls", 0, 0, 10);
        String fullXML = "";
        assertEquals("", fullXML);
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
                "        <text xml:space=\"preserve\" bytes=\"112\">Вопрос: '''Title'''\n" +
                "\n" +
                "Ответ ''Александра Геннадьевича'':\n" +
                "\n" +
                "Content\n" +
                "\n" +
                "\n" +
                "Источник: '''Source'''\n" +
                "\n" +
                "Keywords\n" +
                "\n" +
                "            [[Категория:Cat1]]</text>\n" +
                "        <format>text/x-wiki</format>\n" +
                "    </revision>\n" +
                "</page>\n", generateXML);
    }

    @Test
    public void testGenerateVideoPage() {
        Page testPage = new Page("Title", "Content", "Source Text", "Cat1", "Keywords");
        testPage.setVideo("XXXaaXXX");

        String generateXML = service.generateVideoPage(testPage);

        System.out.println("generateVideoPage = " + generateXML);

        assertEquals("<page>\n" +
                "    <title>Title</title>\n" +
                "    <revision>\n" +
                "        <contributor>\n" +
                "            <id>$adminUserId</id>\n" +
                "        </contributor>\n" +
                "        <text xml:space=\"preserve\" bytes=\"112\">{{#widget:YouTube|id=XXXaaXXX}}\n" +
                "\n" +
                "Вопрос: '''Title'''\n" +
                "\n" +
                "\n" +
                "Ответ ''Александра Геннадьевича'':\n" +
                "\n" +
                "Content\n" +
                "\n" +
                "\n" +
                "Источник: '''[https://www.youtube.com/results?search_query=Source+Text Source Text]'''\n" +
                "\n" +
                "Keywords\n" +
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
                "<page>page3</page>\n" +
                "</mediawiki>", fullXML);
    }

    @Test
    public void testRealExcelInputFile() {

        service.readExcelInputFile("data-test.xls", 0, 0, 20);

        String fullXML = "";

        assertEquals("", fullXML);
    }


    @Test
    public void testRealExcelInputFile_realData() {

        service.readExcelInputFile("bigData.xls", 0, 0, 1200);

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

        int maxNumberOfLines = 10;

        List<Page> pages = service.readExcelInputFile("bigData20190912.xls", 0, 0, maxNumberOfLines);

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

        FileUtils.writeStringToFile(new File("files/new-simple-" + maxNumberOfLines + ".xml"), fullXML, "UTF-8");

        System.out.println("\n\n");
        System.out.println(fullXML);
    }

    @Test
    public void testGenerateHomePage() throws IOException {

        List<String> strings = MWFileUtils.readKeywords(this.getClass(), "categories.txt", false);

        String res = service.generateHomePage(strings);
        assertEquals("<h1>Разделы энциклопедии</h1>\n" +
                "\n" +
                "[[:Категория:Веды|Веды]]\n" +
                "\n" +
                "[[:Категория:Воспитание|Воспитание]]\n" +
                "\n" +
                "[[:Категория:Духовность|Духовность]]\n" +
                "\n" +
                "[[:Категория:Женщина|Женщина]]\n" +
                "\n" +
                "[[:Категория:Здоровье|Здоровье]]\n" +
                "\n" +
                "[[:Категория:Карма и реинкарнация|Карма и реинкарнация]]\n" +
                "\n" +
                "[[:Категория:Любовь|Любовь]]\n" +
                "\n" +
                "[[:Категория:Мужчина|Мужчина]]\n" +
                "\n" +
                "[[:Категория:Мужчины и женщины|Мужчины и женщины]]\n" +
                "\n" +
                "[[:Категория:Общество|Общество]]\n" +
                "\n" +
                "[[:Категория:Питание|Питание]]\n" +
                "\n" +
                "[[:Категория:Природа человека|Природа человека]]\n" +
                "\n" +
                "[[:Категория:Психология|Психология]]\n" +
                "\n" +
                "[[:Категория:Развитие человека и самопознание|Развитие человека и самопознание]]\n" +
                "\n" +
                "[[:Категория:Религия|Религия]]\n" +
                "\n" +
                "[[:Категория:Семья|Семья]]\n" +
                "\n" +
                "[[:Категория:Успех|Успех]]\n", res);
    }

}