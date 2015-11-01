package ru.pumas;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("login");
		String password = request.getParameter("password");
		if(name.equals("student@innopolis") && password.equals("student") ) {
			HttpSession session = request.getSession(true);
			session.setAttribute("user", name);
			response.sendRedirect("/app/Main.jsp");
		} else {
			response.sendRedirect("error.jsp?from=\"invalid login\"");
		}
	}

}
