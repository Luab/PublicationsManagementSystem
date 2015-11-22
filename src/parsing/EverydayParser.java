package  parsing;
import bot.Bot;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by Yuriy on 9/22/2015.
 */

public class EverydayParser {
    private Boolean isOn;

    public void run() {
        try {
            Bot.sendMessage("runned", 1, 47289384);
            isOn = true;
            while (isOn) {

                String path;
                path = "update.xml";
                Bot.sendMessage("Getting links.", 1, 47289384);

                Real.getUpdates("update.xml");

                Bot.sendMessage("Trying to parse.", 1, 47289384);

                if (ParseUpdates.openFile(path)) {

                    Bot.sendMessage("Parsing.", 1, 47289384);
                    ParseUpdates.Parse();


                    Bot.sendMessage("Finished.", 1, 47289384);
                    Thread.sleep(86400000);

                }
            }
        }
                 catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }




