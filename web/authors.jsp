<%@page import="ru.pumas.DbHelper"%>
<%@page import="ru.pumas.Author"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		if (request.getParameter("authorname") != null) {
			System.err.println(DbHelper.addAuthor(new Author(0, request.getParameter("authorname"))));
		}
	%>
	<form method="get">
		<input type="text" name="authorname"> <input type="submit">
	</form>
	<table border="1">
		<tr>
			<th>id</th>
			<th>name</th>
		</tr>
		<%
			for (int i=0;i<10;i++)
			 out.print(i);
							//Author a : DbHelper.getAuthors()) {
		%>
		<%--<tr>--%>

			<%--&lt;%&ndash;<td><%=a.getId()%></td>--%>
			<%--<td><%=a.getName()%></td>&ndash;%&gt;--%>
		<%--</tr>--%>
	</table>
</body>
</html>