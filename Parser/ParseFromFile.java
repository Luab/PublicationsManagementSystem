import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

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
public class ParseFromFile {
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;


    /**
     * Open a file from path. Also opens a BufferedInputStream from it in ParseFromFile.java class
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
     * Parsing data from BufferedInputStream in ParseFromFile.java
     * Parse() get all elements with tag "record" and
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    static public void Parse() throws ParserConfigurationException, IOException, SAXException, SQLException, ParseException {
        int id;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        ArrayList<String> record_authors = new ArrayList<>();
        ArrayList<String> record_subjects = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(bis);
        NodeList nodes = doc.getElementsByTagName("record");
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                Publication pub = new Publication();
                record_authors.clear();
                record_subjects.clear();
                Element element = (Element) nodes.item(i);
                //Searching title and description in data
                pub.set_title(parsebyTag(element, "dc:title"));
                pub.set_description(parsebyTag(element, "dc:description"));

                pub.set_dateUpdated(new Date(df.parse(parsebyTag(element, "datestamp")).getTime()));
                try {
                    pub.set_dateCreated(new Date(df.parse(parsebyTag(element, "dc:date")).getTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Element line;
                //Get list of all creators
                NodeList authors = element.getElementsByTagName("dc:creator");
                for (int f = 0; f < authors.getLength(); f++) {
                    line = (Element) authors.item(f);
                    record_authors.add(getCharacterDataFromElement(line));

                }
                NodeList subjects = element.getElementsByTagName("dc:subject");
                for (int f = 0; f < subjects.getLength(); f++) {
                    line = (Element) subjects.item(f);
                    record_subjects.add(getCharacterDataFromElement(line));

                }

                //Get List of entries with Identifier tag
                NodeList identifiers = element.getElementsByTagName("dc:identifier");
                for (int f = 0; f < identifiers.getLength(); f++) {
                    line = (Element) identifiers.item(f);
                    if (getCharacterDataFromElement(line).contains("doi")) {
                        if (getCharacterDataFromElement(line).length() > 200) {
                            System.out.println(getCharacterDataFromElement(line));
                        }
                        pub.set_doi(getCharacterDataFromElement(line));
                    } else {
                        if ((getCharacterDataFromElement(line).contains("http")) || (getCharacterDataFromElement(line).contains("https"))) {
                            pub.set_link(getCharacterDataFromElement(line));
                        } else {
                            pub.set_venue(getCharacterDataFromElement(line));
                        }


                    }


                }


                String[] authArr = new String[record_authors.size()];
                String[] subArr = new String[record_subjects.size()];
                pub.set_authors(record_authors.toArray(authArr));
                pub.set_subjects(record_subjects.toArray(subArr));
                DbHelper.sendPublication(pub);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // DbHelper.sendPublication(doi, link,dateCreated,dateUpdated,venue,record_subjects.toArray(subArr),record_authors.toArray(authArr) );
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
