package ru.pumas;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/app/DeletePublicationServlet")
public class DeletePublicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeletePublicationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        try {
            DbHelper.deletePublicationById(id);
            response.sendRedirect("search.jsp");
        } catch (SQLException e) {
            response.sendRedirect("error.jsp?from=No such publication");
        }
    }

}
