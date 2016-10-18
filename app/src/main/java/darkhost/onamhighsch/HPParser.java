package darkhost.onamhighsch;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

/**
 * Created by 민재 on 2016-10-09.
 */
public class HPParser {
    /**
     *  Parsing Onam High School Board Page
     *
     * @param url School Board Page URL
     * @return Notice Title, URL, Writer, Date
     */
    static public ArrayList<String> SchoolHPParser(String url)
    {
        ArrayList<String> Notice = new ArrayList<>();
        try {
            // Get Page
            Document doc = Jsoup.connect(url).get();
            // Get Table
            Element table = doc.select("table[class=boardList]").first();
            // Get Notice
            Elements rows = table.select("tr");

            for (int i=1; i < rows.size(); i++) {
                // Get Pages Table
                Element row = rows.get(i);
                // Get Title
                Elements titlecols = row.select("td[class=title]");
                // Get URL
                Elements titleurl = titlecols.select("a[href]");
                // Get Writer
                Elements writers = row.select("a[href=#none]");
                // Get Date (Use Pattern)
                Elements dates = row.select("td:matches([12][0-9]{3}.[0-9]{2}.[0-9]{2})");
                // Save in ArrayList
                String text = titlecols.text();
                String link = titleurl.attr("href");
                String writer = writers.text();
                String date = dates.text();
                Notice.add(text + ";" + "http://onam.hs.kr" + link + ";" + writer + ";" + date);
            }
        }
        // ERROR
        catch (Exception e)
        {
            Log.d("HPParser", "NOTICE PARSING ERROR!");
            e.printStackTrace();
            return null;
        }
        return Notice;
    }
}
