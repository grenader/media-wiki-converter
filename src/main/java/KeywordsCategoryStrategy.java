import org.apache.poi.hssf.usermodel.HSSFRow;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class KeywordsCategoryStrategy implements CategoryStrategy {

    private final List<String> keywords;

    public KeywordsCategoryStrategy() {

        // Load keywords
        keywords = MWFileUtils.readKeywords(KeywordsCategoryStrategy.class, "/keywords.txt", true);
    }

    public String getNewCategoryNameByKeywords(HSSFRow row) {
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

    private List<String> getListOfCategories(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");

        List<String> res = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            res.add(tokenizer.nextToken().trim().toLowerCase());
        }
        return res;
    }


}
