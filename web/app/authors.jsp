<%@page import="ru.pumas.DbHelper" %>
<%@page import="ru.pumas.Author" %>
<%@ page import="ru.pumas.PublicationSet" %>
<%@ page import="ru.pumas.AuthorSet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<jsp:useBean id="user" scope="session" class="ru.pumas.User" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>

<thead>
<tr>
    <th>#</th>
    <th>Name</th>
</tr>
</thead>
<tbody>
    <%
	int i = 0;
	AuthorSet rs = DbHelper.getAuthorSet();
                                    while (rs.next()) {
                                        Author author = rs.getAuthor();
                                        Integer aid = author.getId();
                                %>
<tr>
    <td><%=++i%>
    </td>
    <td><a href="author.jsp?id=<%=aid%>"><%=author.getName()%>
    </a></td>

</tr>
    <%
                                        }

                                %>

</body>
</html>