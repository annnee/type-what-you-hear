<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="http://getbootstrap.com/examples/cover/cover.css">
<Style type="text/css">
.form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
</Style>

<title>Login</title>
</head>
<%
if (session.getAttribute("username") != null) {
	response.sendRedirect("welcome.jsp");
} 
else {
%>
<body>
	 <div class="site-wrapper">

      <div class="site-wrapper-inner">

        <div class="cover-container">
		
          <div class="masthead clearfix">
            <div class="inner">
              <h3 class="masthead-brand"><span class="glyphicon glyphicon-headphones"></span> Listen, Listen!</h3>
              <ul class="nav masthead-nav">
                <li><a href="index.jsp">Home</a></li>
                <li class="active"><a href="login.jsp">Login</a></li>
              </ul>
            </div>
          </div>

          <div class="inner cover">
             <form class="form-signin" role="form" name="login" method="post" action="Servlet">
		        <h2 class="form-signin-heading">Log In</h2>
		        <input type="hidden" name="formType" value="login">
		        <input type="text" name="username" class="form-control" placeholder="Username" required>
		        <input type="password" name="password" class="form-control" placeholder="Password" required>
		        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
		        <p><a href="register.jsp">Register</a></p>
		      </form>
		      <p>${message}</p>
          </div>

          <div class="mastfoot">
            <div class="inner">
              <p>Built with <a href="http://getbootstrap.com">Bootstrap</a>, inspired by <a href="https://twitter.com/mdo">@mdo</a>.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
	<!--Javscript -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<%
	}
%>
</body>
</html>