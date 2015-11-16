package ru.pumas;

import bot.Bot;
import bot.Main;
import sun.util.calendar.BaseCalendar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/app/RunBot")
public class BotServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public BotServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("state");
        if (title == null) {
            response.sendRedirect("error.jsp?from=Something went with bot");
        }
        assert title != null;
        if (title.equals("true")) {
            Bot.run();
            response.sendRedirect("/app/bot.jsp?state=" + title);
        }
        if (title.equals("false")) {
            Bot.run();
        }
    }
}
