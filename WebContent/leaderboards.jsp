<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import ="java.sql.*" %>
<%@ include file="loginDetails.jsp" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<title>Leaderboards</title>
</head>
<body>
	<div class="navbar navbar-default navbar-inverse" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="welcome.jsp"><span class="glyphicon glyphicon-headphones"></span> Listen, Listen!</a>
        </div>
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li><a href="welcome.jsp">Home</a></li>
            <li><a href="#about">About</a></li>
            <li class="active"><a href="leaderboards.jsp">Leaderboards</a></li>
            <li><a href="#contact">Contact</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
          		<%
				if ((session.getAttribute("username") == null)
						|| (session.getAttribute("username") == "")) {					
				%>
					<li><a href="login.jsp">Login</a></li>
				<%
				} 
          		
				else {
				%>
	       		  <li class="dropdown">
		          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%=session.getAttribute("firstName")%> <b class="caret"></b></a>
		          <ul class="dropdown-menu">
		            <li><a href="#">Profile</a></li>
		            <li class="divider"></li>
		            <li><a href="logout.jsp">Log out</a></li>
		          </ul>
	        	</li> 
	        	<%
	        		} 
	        	%>    
      	</ul>
        </div><!--/.nav-collapse -->      
      </div><!-- .container -->
    </div><!-- navbar-default -->
  	<div class="container">		
		<div class="page-header">
		  <h1>Leaderboards</h1>
		</div>
		
		<!-- Nav tabs -->
		<ul class="nav nav-tabs">
		  <li class="active"><a href="#score" data-toggle="tab">Highest Score</a></li>
		  <li><a href="#time" data-toggle="tab">Fastest Time</a></li>
		  <li><a href="#level" data-toggle="tab">Highest Level</a></li>
		</ul>
		
		<!-- Tab panes -->
		<div class="tab-content">
		  <div class="tab-pane fade in active" id="score">
		  	<table class="table table-hover table-responsive">
			<thead>
				<tr>
					<th>#</th>
					<th>Username</th>
					<th>Score</th>
				</tr>
			</thead>
			<%
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database, username, password);
		    Statement st = con.createStatement();
		    ResultSet rs = st.executeQuery("SELECT * FROM Leaderboards ORDER BY score DESC;");
		    int rank = 1;
		    %>
		    <tbody>
		    <% while (rs.next()) { %>
		    	<tr>
		    		<td><% out.print(rank); %></td>
		    		<td><% out.print(rs.getString("username")); %></td>
		    		<td><% out.print(rs.getInt("score")); %></td>
		    	</tr>
		    <% 
		    	rank++;
		    }
		    st.close();
		    con.close();
		    %>
			</tbody>
			</table>

		  </div>
		  
		  <div class="tab-pane fade" id="time">
		  	<div class="tab-pane fade in active" id="score">
		  	<table class="table table-hover table-responsive">
			<thead>
				<tr>
					<th>#</th>
					<th>Username</th>
					<th>Score</th>
				</tr>
			</thead>
			<%
		    Class.forName("com.mysql.jdbc.Driver");
		    con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database, username, password);
		    st = con.createStatement();
		    rs = st.executeQuery("SELECT * FROM Leaderboards ORDER BY score DESC;");
		    rank = 1;
		    %>
		    <tbody>
		    <% while (rs.next()) { %>
		    	<tr>
		    		<td><% out.print(rank); %></td>
		    		<td><% out.print(rs.getString("username")); %></td>
		    		<td><% out.print(rs.getInt("score")); %></td>
		    	</tr>
		    <% 
		    	rank++;
		    }
		    st.close();
		    con.close();
		    %>
			</tbody>
			</table>

		 	</div>
		  </div>
		  
		  <div class="tab-pane fade" id="level">
		  	<div class="tab-pane fade in active" id="score">
		  	<table class="table table-hover table-responsive">
			<thead>
				<tr>
					<th>#</th>
					<th>Username</th>
					<th>First Name</th>
					<th>Score</th>
				</tr>
			</thead>
			<%
		    Class.forName("com.mysql.jdbc.Driver");
		    con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database, username, password);
		    st = con.createStatement();
		    rs = st.executeQuery("SELECT * FROM Leaderboards ORDER BY score DESC;");
		    rank = 1;
		    %>
		    <tbody>
		    <% while (rs.next()) { %>
		    	<tr>
		    		<td><% out.print(rank); %></td>
		    		<td><% out.print(rs.getString("username")); %></td>
		    		<td><% out.print(rs.getString("fName")); %></td>
		    		<td><% out.print(rs.getInt("score")); %></td>
		    	</tr>
		    <% 
		    	rank++;
		    }
		    st.close();
		    con.close();
		    %>
			</tbody>
			</table>

		 	</div>
		  </div>
		</div>
		

		
	</div>  
	<!-- JavaScript -->
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script type="text/javascript" src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script>
	$(document).ready(function() {
		$('#myTab a').click(function (e) {
		  e.preventDefault();
		  $(this).tab('show');
		});
	});
	</script>
</body>
</html>