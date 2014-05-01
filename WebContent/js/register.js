/**
 * 
 */
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
					url: 'AccountsHandler',
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
						
						else if (result.indexOf("captcha") >= 0) {
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
			//check if the date is an existing date e.g. June 31 will throw an error 
			if ((bday>today || bday<minBday) || (day<0 || day>31) || (month<0 || month >12)
					|| (bday.getMonth()!=(month-1))){
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
			
			//check if username length is between 7 and 20 characters long
			if ($("#username").val().length < 7 || $("#username").val().length >21) {
				errorMessages += "Your <b>username</b> must be between 7 and 20 characters long.<br />";
				
				//add has-error to parent element
				$("#username").parents(".form-group").addClass("has-error");				
				isValid = false;
			}
			
			//check if password length is at least 7 characters
			if ($("#password").val().length <= 6) {
				errorMessages += "Your <b>password</b> must be at least 7 characters long.<br />";
				
				//add has-error to parent element
				$("#password").parents(".form-group").addClass("has-error");				
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
			
			//check if user has ticked the consent checkbox 
			if ($('#consent').is(":checked")==false) {
				errorMessages += "You must give your <b>consent</b> by ticking the checkbox.<br />";
				$("#consent").parents(".form-group").addClass("has-error");
			}
				
			//if there are validation errors, append error messages
			if (isValid==false) {
				$("#status").append("<div class=\"alert alert-danger\">"+
				errorMessages +"</div>");
			}
			
			return isValid;
		}		    	    
	});