package parsing;

import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import ru.pumas.DbHelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Yuriy on 9/22/2015.
 */
public class ParseUpdates {
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;

    /**
     * Open a file from path. Also opens a BufferedInputStream from it in
     * ParseUpdates.java class
     *
     * @param path Is a path of file
     * @return True if opened without errors.
     */
    static public boolean openFile(String path) {
        boolean flag = false;
        try {
            File f = new File(path);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            flag = true;
        } catch (Exception e) {
        }
        return flag;
    }

    /**
     * Parsing data from BufferedInputStream in ParseUpdates.java Parse() get
     * all elements with tag "record" and
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    static public void Parse() throws ParserConfigurationException, IOException,
            SAXException, SQLException, ParseException {
        int id;
        int offsetSubjects = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        ArrayList<String> record_authors = new ArrayList<>();
        ArrayList<String> record_subjects = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(bis);
        NodeList nodes = doc.getElementsByTagName("entry");
        System.out.println(nodes.getLength() + " entries");
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                String doi = null;
                String link = null;
                String venue = null;
                record_authors.clear();
                record_subjects.clear();

                Element element = (Element) nodes.item(i);
                String title = parsebyTag(element, "title");
                String description = parsebyTag(element, "summary");

                Date dateUpdated;
                Date dateCreated;
                dateUpdated = new Date(df.parse(parsebyTag(element, "updated").trim().substring(0, 14)).getTime());
                dateCreated = new Date(df.parse(parsebyTag(element, "published").trim().substring(0, 14)).getTime());

                Element line;
                // Get list of all creators
                NodeList authors = element.getElementsByTagName("author");
                for (int f = 0; f < authors.getLength(); f++) {
                    NodeList names = element.getElementsByTagName("name");
                    line = (Element) names.item(f);
                    record_authors.add(getCharacterDataFromElement(line));
                }
                //Get list of all subjects
                NodeList subjects = element.getElementsByTagName("category");
                for (int f = 0; f < subjects.getLength(); f++) {
                    record_subjects.add(String.valueOf(subjects.item(f).getAttributes().getNamedItem("term")).replace("term=\"", "").replace("\"", ""));
                }
                //Get links to arxiv
                NodeList identifiers = element.getElementsByTagName("id");
                for (int f = 0; f < identifiers.getLength(); f++) {
                    line = (Element) identifiers.item(f);
                    link = getCharacterDataFromElement(line);
                }
                line = null;
                //Get doi
                NodeList doiList = element.getElementsByTagName("arxiv:doi");
                for (int f = 0; f < doiList.getLength(); f++) {
                    line = (Element) doiList.item(f);
                    doi = getCharacterDataFromElement(line);
                }
                line = null;
                //Get comments. We hope there will be venues.
                NodeList venueList = element.getElementsByTagName("arxiv:comment");
                for (int f = 0; f < venueList.getLength(); f++) {
                    line = (Element) venueList.item(f);
                    venue = getCharacterDataFromElement(line);
                }

                DbHelper.findOrMakePublication(doi,link, dateCreated, dateUpdated, venue, title, description, record_subjects, record_authors);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Returning
     *
     * @param element
     * @param tag
     * @return
     */
    public static String parsebyTag(Element element, String tag) {

        try {
            NodeList nl = element.getElementsByTagName(tag);

            for (int f = 0; f < nl.getLength(); f++) {
                Element line = (Element) nl.item(f);
                if (getCharacterDataFromElement(line) != null) {
                    return getCharacterDataFromElement(line);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Returns string from the first child of passed Element
     *
     * @param e
     * @return
     */
    public static String getCharacterDataFromElement(Element e) {
        try {
            Node child = e.getFirstChild();
            if (child instanceof CharacterData) {
                CharacterData cd = (CharacterData) child;
                return cd.getData();
            }
        } catch (Exception ex) {

        }
        return null;
    }
}
