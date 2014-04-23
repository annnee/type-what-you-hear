<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="css/flipclock.css">
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
	    <%
			if ((session.getAttribute("username") == null)
					|| (session.getAttribute("username") == "")) {
				response.sendRedirect("login.jsp");
			} 
			else {
		%>
		Welcome, <%=session.getAttribute("firstName")%>!
		
		<div id="status">
		<img src="img/ajax-loader.gif" alt="Waiting for server..." style="display:none">
		<br/>
		</div>
		
		<div class="game-start"></div>
		
		<form id="connect" method="post">
			<button type="submit" class="btn btn-primary">Find a Match</button>
			<input type="hidden" name="formType" value="connect">
			<input type="hidden" name="player"
					value=<%=session.getAttribute("username")%>>
		</form>
		
		<!-- Sending messages to the server to set up the game -->
		<form id="gameSetup" method="post">
			<input type="hidden" name="formType" value="sendMessage">		
			<input type="hidden" name="message" value="sound files">
		</form>
		
		<div id="gameForm" style="display: none">
			<form class="well form-horizontal" id="game" role="form" method="post">
				<input type="hidden" name="formType" value="sendResponse">
				<div class="form-group">
					<label for="message" class="col-sm-2 control-label">Response</label> 
					<div class="col-sm-4">
					<input type="text" class="form-control" name="message">
					</div>
				</div>				
				<button type="submit" class="btn btn-primary">Submit</button>
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
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script type="text/javascript" src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="http://code.createjs.com/createjs-2013.12.12.min.js"></script>
	<script type="text/javascript" src="js/flipclock.min.js"></script>
	<script type="text/javascript" src="js/game.js"></script>
</body>
</html>