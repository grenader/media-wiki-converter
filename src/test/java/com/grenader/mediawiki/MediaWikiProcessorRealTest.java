package com.grenader.mediawiki;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ikanshyn on 2020-04-26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MediaWikiProcessorRealTest {
    @Autowired
    MediaWikiProcessor service;

    @Autowired
    TemplateHandlingService templateService;

    @Test
    public void testGenerateXML_real_all() throws IOException {
        generateAll(600, "/bigData201910152.xls", "");
    }

    @Test
    public void testGenerateXML_real_new1() throws IOException {
        generateAll(600, "/newData20200403.xls", "20200403");
    }

    @Test
    public void testGenerateXML_real_new2() throws IOException {
        generateAll(600, "/new-part-20201017-2283-2480.xls", "20201017");
    }

    private void generateAll(int stepSize, String resourceFileName, String prefix) throws IOException {
        int startFrom = 0;
        int gotRecords;
        do {
            List<Page> pages = service.readExcelInputFile(resourceFileName, 0, startFrom, stepSize);

            gotRecords = pages.size();
            if (gotRecords == 0)
                break;
            List<String> pagesXML = new ArrayList<>();
            for (Page page : pages) {
                String onePageBlock;
                if (page.getVideo() == null)
                    onePageBlock = service.generateQandAPage(page);
                else
                    onePageBlock = service.generateVideoPage(page);
                pagesXML.add(onePageBlock);
            }

            String fullXML = templateService.generateFullXML("pages", pagesXML);
            FileUtils.writeStringToFile(new File("files/mw-multi-"+ prefix+ "-" + startFrom + "-" + gotRecords + ".xml"), fullXML, "UTF-8");
            System.out.println("startFrom = " + startFrom+", gotRecords = " + gotRecords);
            startFrom += stepSize;
        } while (true);
    }


}
