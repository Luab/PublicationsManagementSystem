<%@ page import="ru.pumas.DbHelper" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="ru.pumas.Publication" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.sql.SQLException"%>

<%@page import="ru.pumas.Author"%>
<%@page import="ru.pumas.Subject"%>
<jsp:useBean id="user" scope="session" class="ru.pumas.User" />
<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Publication Managment System</title>

  <!-- Bootstrap Core CSS -->
  <link href="../css/bootstrap.min.css" rel="stylesheet">

  <!-- MetisMenu CSS -->
  <link href="../css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

  <!-- DataTables CSS -->
  <link href="../css/plugins/dataTables.bootstrap.css" rel="stylesheet">

  <!-- Custom CSS -->
  <link href="../css/sb-admin-2.css" rel="stylesheet">

  <!-- Custom Fonts -->
  <link href="../font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="../css/tem.css" type="text/css" />
  <link rel="stylesheet" href="../css/quake.css" type="text/css" />
  <script src="../js/jquery-1.11.3.min.js"></script>
  <script src="../js/tem.js"></script>
  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
  <![endif]-->
  <script type="text/x-mathjax-config">
    MathJax.Hub.Config({
      tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}
    });
</script>
  <script type="text/javascript"
          src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
  </script>


</head>

<body>

<div id="wrapper">

  <!-- /.navbar-top-links -->


  <!-- Navigation -->
  <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="search.jsp">Publication Managment System</a>
    </div>
    <!-- /.navbar-header -->
    <!-- /.navbar-top-links -->

    <div class="navbar-default sidebar" role="navigation">
      <div class="sidebar-nav navbar-collapse">
        <ul class="nav" id="side-menu">
          <!-- /input-group -->
          <li>
            <a href="search.jsp"><i class="fa fa-search fa-fw"></i>Publication search</a>
          </li>
          <li>
            <a href="authors.jsp"><i class="fa fa-users fa-fw"></i>Authors</a>
          </li>
          <li>
            <a  href="subjects.jsp"><i class="fa fa-folder-open-o fa-fw"></i>Subjects</a>
          </li>
          <li>
<a  href="venues.jsp"><i class="fa fa-book fa-fw"></i>Venues</a>
          </li>

            <%if(user.getIsSuper()){%>           <li><a href="addPublication.jsp"><i class="fa  fa-plus-square fa-fw">Add Publication</i> </a> </li>           <%}%>
          <!-- /.nav-second-level -->
        </ul>
      </div>
      <!-- /.sidebar-collapse -->
    </div>
    <!-- /.navbar-static-side -->
  </nav>

  <%
    Integer i=0;
  %>
  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12">
        <h1 class="page-header">Add publication</h1>
      </div>
  </div><div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        Basic Form Elements
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-lg-6">
            <form role="form" action="AddPublicationServlet">
              <div class="form-group">
                <label>Publication Title*</label>
                <input name="title" class="form-control" placeholder="Enter title">
              </div>
              <div class="form-group">
                <label>Description</label>
                <textarea name="description" class="form-control" rows="3"></textarea>
              </div>
              <div class="123">
                <label>Authors*</label>
                <input name="authors" class="form-control" placeholder="Enter author name">
              </div>
              <div class="123">
                <label>Date updated* yyyy-mm-dd</label>
                <input name="dateupdated" class="form-control" placeholder="Enter date">
              </div>
              <div class="123">
                <label>Date created* yyyy-mm-dd</label>
                <input name="datecreated" class="form-control" placeholder="Enter date">
              </div>
              <div class="form-group">
                <label>Subjects</label>
                <input name="subjects" class="form-control" placeholder="Enter subject">
              </div>
              <div class="form-group">
                <label>DOI</label>
                <input name="DOI" class="form-control" placeholder="Enter doi">
              </div>
              <div class="form-group">
                <label>Venue</label>
                <input name="venue" class="form-control" placeholder="Enter venue">
              </div>
              <button type="submit" class="btn btn-default">Submit Button</button>
            </form>
          </div>
          </div>
        <!-- /.row (nested) -->
      </div>
      <!-- /.panel-body -->
    </div>
    <!-- /.panel -->
  </div>
  <!-- /.col-lg-12 -->
</div>
  <!-- /.row -->
</div>
<!-- /#page-wrapper -->

</div>
<!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<!-- jQuery Version 1.11.0 -->
<script src="../js/jquery-1.11.0.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="../js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="../js/plugins/metisMenu/metisMenu.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="../js/sb-admin-2.js"></script>

<!-- Page-Level Demo Scripts - Tables - Use for reference -->
</body>

</html>
