import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MWFileUtils {

    static public List<String> readKeywords(Class currentClass, String fileName, boolean lowerCase) {
        InputStream inputStream = currentClass.getResourceAsStream(fileName);
        BufferedReader b = new BufferedReader(new InputStreamReader(inputStream));

        List<String> res = new ArrayList<>();
        String readLine = "";
        try {
            while ((readLine = b.readLine()) != null) {
//                System.out.println(readLine);
                String str = lowerCase ? readLine.toLowerCase() : readLine;
                res.add(str.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
