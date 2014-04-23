<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<Style type="text/css">
@media (min-width: 1000px) {
	#register {
		width: 75%;
	}
}
</Style>
 <script type="text/javascript">
 var RecaptchaOptions = {
    theme : 'blackglass'
 };
 </script>
<title>Register</title>
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
          <a class="navbar-brand" href="index.jsp"><span class="glyphicon glyphicon-headphones"></span> Listen, Listen!</a>
        </div>
        <div class="collapse navbar-collapse">
        	<ul class="nav navbar-nav">
	            <li><a href="#about">About</a></li>
	            <li><a href="leaderboards.jsp">Leaderboards</a></li>
	            <li><a href="#contact">Contact</a></li>
	        </ul>
          	<ul class="nav navbar-nav navbar-right">
          	<li><a href="index.jsp">Log In</a></li>     
      		</ul>
        </div><!--/.nav-collapse -->      
      </div><!-- .container -->
    </div><!-- navbar-default -->

    <div class="container">  	
    	<br />
    	<form class="well form-horizontal" id="register" role="form" method="post">
          	<input type="hidden" name="formType" value="register">
          	<h2>Register</h2>
          	<br />
              <div class="form-group">
			    <label for="fName" class="col-sm-2 control-label">First Name</label>
			    <div class="col-sm-10">
			    	<input type="text" class="form-control" id="fName" name="fName" placeholder="First Name">
			  		<span class="help-block">Only letters, spaces, apostrophes and hyphens are allowed e.g. Ann Nee</span>
			  	</div>
			  </div>
			  
			  <div class="form-group">
			    <label for="lName" class="col-sm-2 control-label">Last Name</label>
			    <div class="col-sm-10">
			    	<input type="text" class="form-control" id="lName" name="lName" placeholder="Last Name">
			  		<span class="help-block">Only letters, spaces, apostrophes and hyphens are allowed e.g. O'Neil</span>
			  	</div>
			  </div>
			  
              <div class="form-group">
			    <label for="birthday" class="col-sm-2 control-label">Birthday</label>
			    <div class="col-sm-10">
			    	<input type="text" class="form-control" id="birthday" name="birthday" placeholder="DD-MM-YYYY">
			  	</div>
			  </div>
			  
			  <div class="form-group">
				  <label for="gender" class="col-sm-2 control-label">Gender</label>
				  <div class="col-sm-10">
				  <select class="form-control" name="gender">
				  	  <option value="female">Female</option>
			  		  <option value="male">Male</option>
				  </select>
				  </div>
			  </div>
			  
			  <div class="form-group">
			    <label for="username" class="col-sm-2 control-label">Username</label>
			    <div class="col-sm-10">
			    <input type="text" class="form-control" id="username" name="username" placeholder="Username">
			  	<span class="help-block">Only alphanumeric characters (including underscores) are allowed e.g. example123</span>
			  	</div>
			  </div>
			  
			  <div class="form-group">
			    <label for="password" class="col-sm-2 control-label">Password</label>
			    <div class="col-sm-10">
			    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
			  	</div>
			  </div>
			  
			  <div class="form-group">
			    <label for="confirmPassword" class="col-sm-2 control-label">Confirm Password</label>
			    <div class="col-sm-10">
			    <input type="password" class="form-control " id="confirmPassword" name="confirmPassword" placeholder="Confirm Password">
			  	</div>
			  </div>
			  
			  <div class="form-group">
			  	<label for="hearingImpaired" class="col-sm-2 control-label">Hearing Impaired</label>
			  	<div class="col-sm-10">
			  		<select class="form-control" name="hearingImpaired">
					  	<option value="no">No</option>
						<option value="yes">Yes</option>
				  	</select>
			  	</div>
			  </div>  
			  
			  <div class="form-group">
				  <label for="captcha" class="col-sm-2 control-label">reCAPTCHA</label>
				  <div class="col-sm-5" id="captcha">
				  <%
			          ReCaptcha c = ReCaptchaFactory.newReCaptcha("6Le_L_ISAAAAAE7qJ5Ndr0pptp_Izlc1tywlbXLN",
			          			"6Le_L_ISAAAAAOWmsi0Ll_diOHeDaS8TtaJA4TUw", false);
			          out.print(c.createRecaptchaHtml(null, null));
			      %>
			      </div>
			  </div>
			  
			  <div class="form-group">
			 	  <div class="col-sm-offset-2 col-sm-10">
				  	<div id="status"></div>
				  </div>
			  </div>
			   	
			  <div class="form-group">
			    <div class="col-sm-offset-2 col-sm-10">
			      <button type="submit" class="btn btn-primary">Submit</button>
			    </div>
			  </div>	
			  
			   
		</form>
			
    </div>      
                
	<!--Javscript -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script src="js/jquery.maskedinput.min.js" type="text/javascript"></script>
	<script>
	$(document).ready(function() {
		
		//attach an input mask to the birthday field
		$('#birthday').mask("99-99-9999");
		
		$.fn.serializeObject = function()
		{
		    var o = {};
		    var a = this.serializeArray();
		    $.each(a, function() {
		        if (o[this.name] !== undefined) {
		            if (!o[this.name].push) {
		                o[this.name] = [o[this.name]];
		            }
		            o[this.name].push(this.value || '');
		        } else {
		            o[this.name] = this.value || '';
		        }
		    });
		    return o;
		};
		
		$('#register').submit(function(event) {
			event.preventDefault();

	      	//clear any messages in the status div
			$("#status").empty();
			
			//remove any css from input fields
			var formElements = $("#register").find(":input");
			$.each(formElements, function() {
				$(this).parents(".form-group").removeClass("has-error");		
			});

			//validate input
			var valid = validateInfo();
			
	      	//if there aren't any validation errors, proceed with request
	      	//if not, don't send ajax request to server
	        if (valid==true) {
	        	var formData = JSON.stringify($('#register').serializeObject());
	        	
	       		$.ajax({
					url: 'Servlet',
					type: 'POST',
					data: {json: formData},
					beforeSend: function() {
						$("#status").append("<div class=\"alert alert-info reg-info\">"+
						"Processing request...</div>");	
					},
					error: function(xhr, status, error) {
						$(".reg-info").remove();
						Recaptcha.reload();
						$("#status").append("<div class=\"alert alert-danger\">"+
								"Error encountered while sending data to the server. Please try again.</div>");	
					},
					success: function(result) {
						$(".reg-info").remove();
						
						//if there are no validation errors
						if (result.indexOf("SUCCESS") >=0) {
							$("#register :input").prop("disabled", true);
							$("button").prop("disabled", true);
							$("#status").append("<div class=\"alert alert-success\">" 
									+ "Your account has been successfully created!<br />"
									+ "<a href=\"login.jsp\" class=\"alert-link\">Click here</a> to login.");
							
							//redirect to the login page in 3 seconds
							setTimeout(function(){
								document.location.href="login.jsp";
							},5000);
						}
						
						//if there are validation errors (i.e. username already exists)
						else if (result.indexOf("username") >=0){
							Recaptcha.reload();
							$("#username").parents(".form-group").addClass("has-error");
							$("#status").append("<div class=\"alert alert-danger\">"
									+result+"</div>");
						}
						
						else {
							Recaptcha.reload();
							$("#captcha").parents(".form-group").addClass("has-error");
							$("#status").append("<div class=\"alert alert-danger\">"
									+result+"</div>");
						}
					},
					statusCode: {
						500: function() {
							Recaptcha.reload();
							$("#status").append("<div class=\"alert alert-danger\">"+
									"An error has occurred in the servlet.</div>");
						}
					}
			    }); 
	       	}	        
	    });
		
		//return true if data is valid
		//return false if data is invalid
		function validateInfo() {
			var isValid = true;
			var errorMessages = "Error(s): <br />";
			
			//check if mandatory fields are empty
			var formElements = $("#register").find(":input");
			$.each(formElements, function() {
				var curElem = $(this).attr("placeholder");
				//ignore the select field and checkbox
				if (typeof curElem !== 'undefined' && curElem !== false) {
					//check if the current field has been filled out
					if ($(this).val()=="") {
						$(this).parents(".form-group").addClass("has-error");
						if (curElem=="DD-MM-YYYY") {
							curElem = "Birthday";	
						}
						else if (curElem=="Type the text") {
							curElem = "reCAPTCHA";
						}
						
						errorMessages += "<b>" + curElem + "</b> is required. <br />";
						isValid = false;
					}
				}		
			});
			
			//check if first & last name only contains letters and certain symbols
			var letterPattern = /[^A-Za-z-'\s]/;
			
			//if we find something that isn't a letter, then the input is invalid
			if ($("#fName").val().match(letterPattern)) {
				errorMessages += "Your <b>first name</b> is invalid. <br />";
				$("#fName").parents(".form-group").addClass("has-error");
				isValid = false;
			}
			
			if ($("#lName").val().match(letterPattern)) {
				errorMessages += "Your <b>last name</b> is invalid. <br />";
				$("#lName").parents(".form-group").addClass("has-error");
				isValid = false;
			}
			
			//check if birthday is valid
			var today = new Date();
			var day = $("#birthday").val().substring(0,2);
			var month = $("#birthday").val().substring(3,5); 
			var year = $("#birthday").val().substring(6);
			var bday = new Date();
			bday.setFullYear(year, (month-1), day);
			
			//earliest birthday logic-wise: January 1, 1900.
			var minBday =  new Date();
			minBday.setFullYear(1900, 0, 1);
			
			//if input birthday is after today's date or before the earliest birthday
			if (bday>today || bday<minBday) {
				errorMessages += "Your <b>birthday</b> is invalid. <br />";
				$("#birthday").parents(".form-group").addClass("has-error");
				valid = false;
			}	
			
			//check if username only contains alphanumeric characters
			var alphanumericPattern = /[^\w]/;
			if ($("#username").val().match(alphanumericPattern)) {
				errorMessages += "Your <b>username</b> is invalid. <br />";
				$("#username").parents(".form-group").addClass("has-error");
				isValid = false;
			}
			
			//check if password length is at least 7 characters
			if ($("#password").val().length <= 6) {
				errorMessages += "Your <b>password</b> must be at least 7 characters long.<br />";
				isValid = false;
			}
			
			//check if the password & confirm password fields are the same
			if ($("#password").val()!=$("#confirmPassword").val()) {
				errorMessages += "Your <b>passwords</b> do not match. Please ensure that they are the same.<br />";
				
				//add has-error to parent element
				$("#password").parents(".form-group").addClass("has-error");
				$("#confirmPassword").parents(".form-group").addClass("has-error");
				
				isValid = false;
			}
				
			//if there are validation errors, append error messages
			if (isValid==false) {
				$("#status").append("<div class=\"alert alert-danger\">"+
				errorMessages +"</div>");
			}
			
			return isValid;
		}		    	    
	});
	</script>
</body>
</html>


