<%--
  Created by IntelliJ IDEA.
  User: Булат
  Date: 26.10.2015
  Time: 22:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/tem.css" type="text/css" />
<link rel="stylesheet" href="css/quake.css" type="text/css" />
<script src="js/jquery-1.11.3.min.js"></script>
<script src="js/tem.js"></script>
<html>
<head>
    <title>hOi!</title>
</head>
<body>
<text font-family="comic sans">Smtheng went roooong :c
Here's some info:
<%=request.getParameter("from")%></text>

<img class="shake-me" src="img/tem.png"/>
<audio loop>
  <source src="audio/dogsong.ogg">
</audio>
</body>
</html>
