package com.grenader.mediawiki;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class KeywordsCategoryStrategyTest {

    private KeywordsCategoryStrategy strategy = new KeywordsCategoryStrategy();

    @Test
    public void testReadKeywords() throws IOException {

        List<String> strings = MWFileUtils.readFileLines(this.getClass(), "/keywords.txt", true);

        assertEquals(199, strings.size());

        assertEquals("абсолютная истина", strings.get(0));
        assertEquals("алкоголизм", strings.get(1));
    }

}