package ru.pumas;

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
@WebServlet("/app/AddPublicationServlet")
public class AddPublicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddPublicationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String authors = request.getParameter("authors");
        String subjects = request.getParameter("subjects");
        String DOI = request.getParameter("DOI");
        String venue = request.getParameter("venue");
        String datecreated = request.getParameter("datecreated");
        String dateupdated = request.getParameter("dateupdated");
        Integer id=1;
        /*if (title.equals(null)||description.equals(null)||authors.equals(null)||subjects.equals(null)){
            response.sendRedirect("error.jsp?from=Please fill all required forms");
        }*/
        try {
            id = DbHelper.addPublication(DOI, null, java.sql.Date.valueOf(datecreated), java.sql.Date.valueOf(dateupdated), null, title, description);
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "error.jsp?from=Please fill all required forms");
        }
        response.sendRedirect("/app/publication.jsp?id="+id);
    }

}
