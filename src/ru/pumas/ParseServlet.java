package ru.pumas;

import bot.Bot;
import parsing.EverydayParser;

import javax.servlet.ServletContextEvent;
import java.util.logging.Logger;

/**
 * Created by Lua_b on 22.11.2015.
 */
public class ParseServlet {
    private static Logger logger = Logger.getLogger(BotServlet.class.toString());

    private Thread myThread = null;

    public void contextInitialized(ServletContextEvent sce) {
        if ((myThread == null) || (!myThread.isAlive())) {
            myThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    EverydayParser parser = new EverydayParser();
                    parser.run();
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
