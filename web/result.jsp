<%@ page import="ru.pumas.DbHelper" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="ru.pumas.Publication" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.sql.SQLException"%>

<%@page import="ru.pumas.Author"%>
<%@page import="ru.pumas.Subject"%>
<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>SB Admin 2 - Bootstrap Admin Theme</title>

  <!-- Bootstrap Core CSS -->
  <link href="css/bootstrap.min.css" rel="stylesheet">

  <!-- MetisMenu CSS -->
  <link href="css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

  <!-- DataTables CSS -->
  <link href="css/plugins/dataTables.bootstrap.css" rel="stylesheet">

  <!-- Custom CSS -->
  <link href="css/sb-admin-2.css" rel="stylesheet">

  <!-- Custom Fonts -->
  <link href="font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="css/tem.css" type="text/css" />
  <link rel="stylesheet" href="css/quake.css" type="text/css" />
  <script src="js/jquery-1.11.3.min.js"></script>
  <script src="js/tem.js"></script>
  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
  <![endif]-->

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
      <a class="navbar-brand" href="index.html">SB Admin v2.0</a>
    </div>
    <!-- /.navbar-header -->
    <!-- /.navbar-top-links -->

    <div class="navbar-default sidebar" role="navigation">
      <div class="sidebar-nav navbar-collapse">
        <ul class="nav" id="side-menu">
          <!-- /input-group -->
          <li>
            <a href="main.jsp"><i class="fa fa-dashboard fa-fw"></i>Publication search</a>
          </li>
          <li>
            <a href="authors.jsp"><i class="fa fa-table fa-fw"></i>Authors</a>
          </li>
          <li>
            <a class="active" href="subject.jsp"><i class="fa fa-edit fa-fw"></i> Subjects</a>
          </li>
          <!-- /.nav-second-level -->
        </ul>
      </div>
      <!-- /.sidebar-collapse -->
    </div>
    <!-- /.navbar-static-side -->
  </nav>


  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12">
        <h1 class="page-header">Publications</h1>
      </div>
      <!-- /.col-lg-12 -->
    </div>
    <div class="row">
      <div class="col-lg-6">
        <div class="panel panel-default">
          <div class="panel-heading">
            Publications
          </div>
          <!-- /.panel-heading -->
          <div class="panel-body">
            <div class="table-responsive">
              <table class="table table-hover">
                <thead>
                <tr>
                  <th>#</th>
                  <th>Password</th>
                  <th>Username</th>
                </tr>
                </thead>
                <tbody>
                <%
                  int i=0;

                  ResultSet rs = DbHelper.getPublicationsSet();
                  while(rs.next())
        {
          Publication publ = Publication.from(rs);
          i++;
                %>
                <tr data-href="Publication.jsp?id">
                  <td><%=publ.getId()%></td>
                  <td><%=publ.getTitle()%></td>
                  <td>
                    <img class="shake-me" src="img/tem.png"/>
                    <audio loop>
                      <source src="audio/dogsong.ogg" />
                    </audio></td>
                </tr>
<%
    i++;
  }
  %>
                </tbody>
              </table>
            </div>
            <!-- /.table-responsive -->
          </div>
          <!-- /.panel-body -->
        </div>
        <!-- /.panel -->
      </div>
      <!-- /.col-lg-6 -->

    </div>
    <!-- /.row -->
  </div>
  <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<!-- jQuery Version 1.11.0 -->
<script src="js/jquery-1.11.0.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="js/plugins/metisMenu/metisMenu.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="js/sb-admin-2.js"></script>

<!-- Page-Level Demo Scripts - Tables - Use for reference -->
<script>
  $(document).ready(function() {
    $('#dataTables-example').dataTable();
  });
  $('.table > td').click(function() {
    alert("OH MY GOD THAT'S SOME FANCY SHIT!")
  });
</script>

</body>

</html>
