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
.popover {
 	width: 330px;
}

p.reg-link {
	text-align: center;
	
}

a.register {
	color:#333;
}

</Style>
<title>Listen, Listen!</title>

</head>
<body>
	 <div class="site-wrapper">

      <div class="site-wrapper-inner">

        <div class="cover-container">

          <div class="masthead clearfix">
            <div class="inner">
              <h3 class="masthead-brand"><span class="glyphicon glyphicon-headphones"></span> Listen, Listen!</h3>
              <ul class="nav masthead-nav">
                <li><a href="#" class="toggle-login">Login</a>
	                <div class="login-popover">              	
	                	<div class="login-form hide">
		                	<form class="form-signin" role="form" name="login" method="post" action="Servlet"> 
					        <input type="hidden" name="formType" value="login">
					        <input type="text" name="username" class="form-control" placeholder="Username" required>
					        <input type="password" name="password" class="form-control" placeholder="Password" required>
					        <button class="btn btn-primary btn-block" type="submit">Sign in</button>
					        <p class="reg-link"><a href="register.jsp" class="register">Register</a></p>
				      		</form>
			      		</div>			      		
	                </div>
                </li>
              </ul>
            </div>
          </div>
          
          				
		  

          <div class="inner cover">
            <h1 class="cover-heading">Listen, Listen! : The Type-What-You-Hear Game</h1>
            <p class="lead">A project by Ann Nee Lau, supervised by Dr. Jon Barker</p>
            <p class="lead">
              <a href="#" id="lm" class="btn btn-lg btn-default">Learn More</a>
              
              
            </p>
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
	<script src="http://code.createjs.com/createjs-2013.12.12.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script>
		$('.toggle-login').popover({
			html: true,
			content: function () {
				return $(this).siblings().find('.login-form').html();
			},
			'placement': 'bottom'
		});
	</script>
</body>
</html>