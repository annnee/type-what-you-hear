<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">


<title>Register</title>

</head>
<body>
	 <div class="site-wrapper">

      <div class="site-wrapper-inner">

        <div class="cover-container">

          <div class="masthead clearfix">
            <div class="inner">
              <h3 class="masthead-brand"><span class="glyphicon glyphicon-headphones"></span> Listen, Listen!</h3>
              <ul class="nav masthead-nav">
                <li><a href="index.jsp">Home</a></li>
                <li><a href="#">Project Description</a></li>
                <li class="active"><a href="login.jsp">Login</a></li>
              </ul>
            </div>
          </div>

          <div class="inner cover">      
          </div>
          
          <form id="register" role="form" method="post" action="Servlet">
          	<input type="hidden" name="formType" value="register">
              <div class="form-group">
			    <label for="fName">First Name</label>
			    <input type="text" class="form-control" name="fName" placeholder="First Name">
			  </div>
			  
			  <div class="form-group">
			    <label for="lName">Last Name</label>
			    <input type="text" class="form-control" name="lName" placeholder="Last Name">
			  </div>
			  
              <div class="form-group">
			    <label for="birthday">Birthday</label>
			    <input type="text" class="form-control" name="birthday" placeholder="DD-MM-YYYY">
			  </div>
			  
			  <div class="form-group">
			  <label for="gender">Gender</label>
			  <select class="form-control" name="gender">
				<option value="male">Male</option>
			 	<option value="female">Female</option>
			  </select>
			  </div>
			  
			  <div class="form-group">
			    <label for="email">Email address</label>
			    <input type="email" class="form-control" name="email" placeholder="E-mail">
			  </div>
			  <div class="form-group">
			    <label for="password">Password</label>
			    <input type="password" class="form-control" name="password" placeholder="Password">
			  </div>
			  <div class="form-group">
			    <label for="confirmPassword">Confirm Password</label>
			    <input type="password" class="form-control" name="confirmPassword" placeholder="Confirm Password">
			  </div>
			  
			  <div class="checkbox">
			    <label>
			      <input type="checkbox" name="hearing"> Check if you have some degree of hearing impairment
			    </label>
			  </div>
			  <button type="submit" class="btn btn-default">Submit</button>
			</form>
			
			<p>${message}</p>

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
	<script>
	/*$(document).ready(function() {
		$("#register").submit(function(event){
			event.preventDefault();
			var formData = $( "#register" ).serialize();
			$.post('Servlet', formData, function(responseText){
				
			}).fail(function(){
				alert("error encountered");
			});
		});
	});*/
	</script>
</body>
</html>


