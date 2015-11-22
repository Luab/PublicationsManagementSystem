package ru.pumas;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String description = request.getParameter("description");

		List<String> authors = new ArrayList<String>();
		authors.add(request.getParameter("authors"));

		List<String> subjects = new ArrayList<String>();
		subjects.add(request.getParameter("subjects"));

		String doi = request.getParameter("DOI");
		String venue = request.getParameter("venue");
		String dateCreated = request.getParameter("datecreated");
		String dateUpdated = request.getParameter("dateupdated");
		String link = request.getParameter("link");
		Integer id = 1;
		/*
		 * if
		 * (title.equals(null)||description.equals(null)||authors.equals(null)||
		 * subjects.equals(null)){ response.sendRedirect(
		 * "error.jsp?from=Please fill all required forms"); }
		 */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		try {
			id = DbHelper.makePublication(doi, link,
					new Date(format.parse(dateCreated).getTime()),
					new Date(format.parse(dateUpdated).getTime()), venue, title,
					description,
					authors, subjects);
			response.sendRedirect(
					request.getContextPath() + "/app/publication.jsp?id=" + id);
		} catch (SQLException e) {
			response.sendRedirect(request.getContextPath()
					+ "error.jsp?from=" + e.getMessage());
		} catch (ParseException e) {
			response.sendRedirect(request.getContextPath()
					+ "error.jsp?from=wrong date format");
			e.printStackTrace();
		}

	}

}
