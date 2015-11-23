/**
 * Created by Yuriy on 9/24/2015.
 */
package  parsing;
import bot.Bot;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Real {
    /**
     * This method downloads fresh updates for every category in arxiv "Recently edited" category into "updates.xml"
     * @throws IOException
     */
    public static void getUpdates(String path) throws IOException {
        String categories[] = {"astro-ph", "cond-mat", "cs", "gr-qc", "hep-ex", "hep-la", "hep-ph", "hep-th", "math", "math-ph", "nlin", "nucl-ex", "nucl-th", "physics", "q-bio", "q-fin", "quant-ph", "stat"};
        Document doc;
        String link = "http://export.arxiv.org/api/query?id_list=";
        ArrayList<String> linksArray = new ArrayList<>(); //Array of links with update files
        PrintWriter output = new PrintWriter(new File(path));
        //Next line append Correct XML header to the .xml to keep consistiency
        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
                "<feed xmlns=\"http://www.w3.org/2005/Atom\"> \n" +
                " <link href=\"Wou\" /> \n" +
                " <title type=\"html\">Wah</title> \n" +
                " <id>\n" +
                "  http://arxiv.org/api/maI028Z++BjnJXXo0GG8U+YbRHU\n" +
                " </id> \n" +
                " <updated>\n" +
                "  2015-11-22T00:00:00-05:00\n" +
                " </updated> \n" +
                " <opensearch:totalresults xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\">\n" +
                "  88\n" +
                " </opensearch:totalresults> \n" +
                " <opensearch:startindex xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\">\n" +
                "  0\n" +
                " </opensearch:startindex> \n" +
                " <opensearch:itemsperpage xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\">\n" +
                "  500\n" +
                " </opensearch:itemsperpage> ");

        for (int i = 0; i < categories.length; i++) {
            try {
                doc = Jsoup.connect("http://export.arxiv.org/rss/" + categories[i]).get();
                Elements links = doc.select("item link");

                for (int f = 0; f < links.size(); f++) {

                    //We grab ids from information given from arxiv servers
                    link += links.get(f).text().replaceAll("http://arxiv.org/abs/", "") + ",";

                    //To prevent error with too long URL we divide our queries into medium queries.
                    if (link.length() > 1000) {
                        linksArray.add(link);
                        link = "http://export.arxiv.org/api/query?id_list=";
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        for (int i = 0; i < linksArray.size(); i++) {
            linksArray.set(i, linksArray.get(i).substring(0, linksArray.get(i).length() - 1) + "&max_results=500");

            Bot.sendMessage("Starting Downloading " + (i) + " Element",1,47289384);

            Bot.sendMessage(linksArray.get(i),1,47289384);


            doc = Jsoup.connect(linksArray.get(i)).get(); //Download document by link using GET request;
            String xmlBody = doc.toString();
            xmlBody = xmlBody.substring(xmlBody.indexOf("<entry>"), xmlBody.indexOf("</feed>")); //To merge different XMLs to one we delete XML header and closing tag
            output.append(xmlBody);

        }
        output.append("</feed>"); //We insert closing tag to keep XML consistienscy
        output.flush();
    }
}
