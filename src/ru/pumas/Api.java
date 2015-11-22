package ru.pumas;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Api
 */
@WebServlet("/Api")
public class Api extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Api() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String query = request.getParameter("query");
		String type = request.getParameter("type");
		String limit = request.getParameter("limit");
		String offset = request.getParameter("offset");
		PrintWriter out = new PrintWriter(response.getOutputStream());
		out.print(doRequest(query, type, limit, offset));
		out.close();
	}

	static final String ERROR = "error";
	static final String RESPONSE = "response";
	static final String NATURAL_REGEX = "^\\d+$";
	static final int MAX_LIMIT = 100;
	static final int DEFAULT_LIMIT = 10;

	String doRequest(String query, String type, String limit, String offset) {
		JSONObject ret = new JSONObject();
		int offsetNumber = 0;
		int limitNumber = DEFAULT_LIMIT;
		try {
			if (query == null) {
				ret.put(ERROR, "query not set");
				return ret.toString();
			}
			if (type == null) {
				ret.put(ERROR, "type not set");
				return ret.toString();
			}
			if (limit != null && !limit.matches(NATURAL_REGEX)) {
				ret.put(ERROR, "limit is not a number");
				return ret.toString();
			}
			if (offset != null) {
				try {
					offsetNumber = Integer.parseInt(offset);
				} catch (NumberFormatException e) {
					ret.put(ERROR, "offset is not a number");
					return ret.toString();
				}
			}
			if (limit != null) {
				try {
					limitNumber = Integer.parseInt(limit);
				} catch (NumberFormatException e) {
					ret.put(ERROR, "limit is not a number");
					return ret.toString();
				}
			}
			if (type.equals("smart")) {
				ret.put(RESPONSE,
						setToArray(DbHelper.searchPublicationSet(query,
								offsetNumber, limitNumber)));
				return ret.toString();
			}
			if (type.equals("author")) {
				ret.put(RESPONSE,
						setToArray(DbHelper.searchPublicationsByAuthorSubstring(
								query, offsetNumber, limitNumber)));
				return ret.toString();
			}
			if (type.equals("subject")) {
				ret.put(RESPONSE,
						setToArray(DbHelper.searchPublicationsByVenueSubstring(
								query, offsetNumber, limitNumber)));
				return ret.toString();
			}
			if (type.equals("venue")) {
				ret.put(RESPONSE,
						setToArray(DbHelper.searchPublicationsByVenueSubstring(
								query, offsetNumber, limitNumber)));
				return ret.toString();
			}
			ret.put(ERROR, "wrong type");
			return ret.toString();
		} catch (JSONException |

		SQLException e)

		{
			e.printStackTrace();
			return "{\"" + ERROR + "\":\"internal error\"}";
		}

	}

	JSONArray setToArray(PublicationSet set)
			throws SQLException, JSONException {
		JSONArray ar = new JSONArray();
		while (set.next()) {
			ar.put(set.getPublication().toJSONObject());
		}
		return ar;
	}

}
