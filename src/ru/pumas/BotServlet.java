package ru.pumas;

import bot.Bot;
import bot.Main;
import sun.util.calendar.BaseCalendar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class to listen for application startup and shutdown
 *
 * @author HBR
 *
 */
public class BotServlet implements ServletContextListener {
    private static Logger logger = Logger.getLogger(BotServlet.class.toString());

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("class : context destroyed");
        Bot.turnOff();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        Bot.run();
        logger.info("myapp : context Initialized");
    }


}
