<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<title>Home</title>
</head>
<body>
	<%
		if ((session.getAttribute("username") == null)
				|| (session.getAttribute("username") == "")) {
			response.sendRedirect("index.jsp");
		} 
		else {
	%>
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
	            <li class="active"><a href="welcome.jsp">Home</a></li>
	            <li><a href="#about">About</a></li>
	            <li><a href="leaderboards.jsp">Leaderboards</a></li>
	            <li><a href="#contact">Contact</a></li>
          	</ul>
	        <ul class="nav navbar-nav navbar-right">
	        	<li class="dropdown">
		        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
		        <span class="glyphicon glyphicon-user"></span>  <%=session.getAttribute("firstName")%> 
		        <b class="caret"></b>
		        </a>
		        <ul class="dropdown-menu">
		            <li><a href="#">Profile</a></li>
		            <li class="divider"></li>
		            <li><a href="logout.jsp">Log out</a></li>
	          	</ul>
        		</li>
	      	</ul>
        </div><!--/.nav-collapse -->      
      </div><!-- .container -->
    </div><!-- navbar-default -->
  	<div class="container">		
		<div class="page-header">
		  <h1>Hello <%=session.getAttribute("firstName")%>!</h1>
		</div>

		<div class="bs-example">
	    <div class="row">
	    	<div class="col-xs-6 col-md-3">
				<a href="game.jsp" class="thumbnail">
		          <img data-src="holder.js/100%x180" alt="Play Game" src="img/controller.png" style="height: 180px; width: 100; display: block;">
		        </a>   
			</div>
	      
	      <div class="col-xs-6 col-md-3">
	        <a href="leaderboards.jsp" class="thumbnail">
	          <img data-src="holder.js/100%x180" alt="Test Sound" src="img/controller.png" style="height: 180px; width: 100; display: block;">
	        </a>
	      </div>
	      <div class="col-xs-6 col-md-3">
	        <a href="logout.jsp" class="thumbnail">
	          <img data-src="holder.js/100%x180" alt="Log Out" src="img/controller.png" style="height: 180px; width: 100; display: block;">
	        </a>
	      </div>
	    </div>
	  </div>
	<%
		}
	%>
	</div>  
	<!-- JavaScript -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

</body>
</html>