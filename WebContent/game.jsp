<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<title>Game</title>
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
          </button>
          <a class="navbar-brand" href="welcome.jsp"><span class="glyphicon glyphicon-headphones"></span> Listen, Listen!</a>
        </div>
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="welcome.jsp">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
        	<li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%=session.getAttribute("userFName")%> <b class="caret"></b></a>
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
	    <%
			if ((session.getAttribute("email") == null)
					|| (session.getAttribute("email") == "")) {
				response.sendRedirect("login.jsp");
			} 
			else {
		%>
		Welcome, <%=session.getAttribute("userFName")%>!
		<!--  <form id = "admin" method="get">
		<input type="submit" name="submit" value="Print users and games info" />
		</form> -->
		
		<div id="status">
		<img src="img/ajax-loader.gif" alt="Waiting for server..." style="display:none">
		<br/>
		</div>
		<form id="connect" method="post">
			<input type="submit" name="submit" value="Ready" />
			<input type="hidden" name="formType"
					value="connect">
			<input type="hidden" name="player"
					value=<%=session.getAttribute("email")%>>
		</form>
		<div id="gameForm" style="display: none">
			<form id="game" method="post">
				<input type="hidden" name="formType"
					value=sendResponse>
				<input type="hidden" name="player"
					value=<%=session.getAttribute("email")%>> 
					<label for="response">What did you hear?</LABEL> 
					<input type="text" id="response" name="response">
					<input type="submit" name="submit" value="Submit" />
			</form>
		</div>
		
		
		<div id="restartGame">
		
		</div>
		<br />
		<br />
		<%
			}
		%>
    </div> <!-- Container -->
	
	
	<!-- JavaScript -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script src="http://code.createjs.com/soundjs-0.5.2.min.js"></script>
	<script src="js/game.js"></script>
</body>
</html>