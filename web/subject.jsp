<%@ page import="ru.pumas.DbHelper" %>
<%@ page import="ru.pumas.Publication" %>
<%--
  Created by IntelliJ IDEA.
  User: Булат
  Date: 26.10.2015
  Time: 21:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
  Integer i=10;
  out.print(request.getParameter("id"));
  System.out.println(0);
  Publication publ = DbHelper.getPublicationById(i);
%>
<h1 class="page-header"><%=publ.getTitle()%></h1>
</body>
</html>
