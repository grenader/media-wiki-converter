import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class KeywordsCategoryStrategyTest {

    private KeywordsCategoryStrategy strategy = new KeywordsCategoryStrategy();

    @Test
    public void testReadKeywords() throws IOException {

        List<String> strings = MWFileUtils.readKeywords(this.getClass(), "/keywords.txt", true);

        assertEquals(199, strings.size());

        assertEquals("абсолютная истина", strings.get(0));
        assertEquals("алкоголизм", strings.get(1));
    }

}