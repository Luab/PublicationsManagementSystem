package ru.pumas;

import bot.Bot;
import parsing.EverydayParser;

import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.Part;

/**
 * Class to listen for application startup and shutdown
 *
 * @author HBR
 *
 */
public class BotServlet implements ServletContextListener {
    private static Logger logger = Logger.getLogger(BotServlet.class.toString());

    private Thread myThread = null;

    public void contextInitialized(ServletContextEvent sce) {
        if ((myThread == null) || (!myThread.isAlive())) {
            myThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Bot bot = new Bot();
                    Bot.run();
                }
            });
            myThread.start();
        }
    }

    public void contextDestroyed(ServletContextEvent sce){
        try {
            Bot.turnOff();
            myThread.interrupt();
        } catch (Exception ex) {
        }
    }


}
