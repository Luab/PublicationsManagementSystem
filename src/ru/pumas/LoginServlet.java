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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		User user = null;
		try {
			user = DbHelper.getUserByLoginPassword(login, password);
		} catch (SQLException e) {
			response.sendRedirect(request.getContextPath()
					+ "/error.jsp?from=\"internal error\"");
			e.printStackTrace();
			return;
		}
		if (user != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("user", user);
			response.sendRedirect(request.getContextPath() + "/app/search.jsp");
		} else {
			response.sendRedirect(request.getContextPath()
					+ "/error.jsp?from=\"invalid login\"");
		}
	}

}
