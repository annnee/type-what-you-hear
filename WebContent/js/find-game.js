/**
	 * 
	 */
	$(document).ready(function() {	    
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
		
		function connectToGame() {
			var formData = JSON.stringify($('#connect').serializeObject());	
			$.ajax({
				url: 'GameHandler',
				type: 'POST',
				data:  {json: formData},
				beforeSend: function() {
					//prevent player from submitting another response until 
					//the server is ready to accept another response
					$("img").show();
					$("#status").append("Finding a match...<br />");

				},
				error: function(message) {
					$("img").hide();
					$("#status").append("Error encountered while submitting response to game server. <br />");

				},
				success: function(result) {  	
					//if there aren't any available ports
					if (result.indexOf("Sorry") >= 0) {
						$("#status").removeClass("alert-info").addClass("alert-danger");
						$("#status").append("<strong>"+result+"</strong>");
						$("#loadingSign").hide();
					}
					else {
						$("#status").append("Both players have connected! <br />");

						//let server know it is ready to receive the noisy tokens
						retrieveNoisyTokens();
					}
					
				},
				statusCode: {
					500: function() {
						$("#status").append("An error has occurred in the servlet. <br />");
					}
				}
			});
		}

		//retrieve list of sound files from the server
		function retrieveNoisyTokens() {
			var formData = JSON.stringify($('#gameSetup').serializeObject());
			$.ajax({
				url: 'GameHandler',
				type: 'POST',
				data:  {json: formData},
				beforeSend: function() {
					$("#status").append("Retrieving sound files from the server... <br />");

				},
				error: function(message) {
					$("img").hide();
					$("#status").append("Error encountered while retrieving sound files from the server. <br />");
					
				},
				success: function(result) {	
					var date = new Date();
					date.setTime(date.getTime()+(60*1000));
					//this cookie expires in a minute
					document.cookie = "soundFiles="+result+ "; expires=" + date.toGMTString() + "; path=/";					
					location.replace("game.jsp");
				},
				statusCode: {
					500: function() {
						$("#status").append("An error has occurred in the servlet. <br />");
					}
				}
			});
			
		}
		connectToGame();
	});