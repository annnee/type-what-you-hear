/**
 * 
 */

$(document).ready(function() {
			
			$("#connect").submit(function(event) {
				event.preventDefault();
				var formData = $("#connect").serialize();
				
				//show game form, hide ready button 
				$("#connect").hide();
				$.ajax({
				    url: 'GameHandler',
				    type: 'POST',
				    data:  formData,
					beforeSend: function() {
						//prevent player from submitting another response until 
						//the server is ready to accept another response
						$("img").show();
									
					},
					error: function(message) {
						$("img").hide();
						$("#status").append("Error encountered while submitting response to game server. <br />");
				    	
					},
				    success: function(result) {  	
				    	$("img").hide();
				    	$("#gameForm").show();	
						$("#status").append("Both players have connected! <br />");						
				    },
				    statusCode: {
				        500: function() {
				        	$("#status").append("An error has occurred in the servlet. <br />");
				    	}
				    }
				});
			});
			
			$("#game").submit(function(event){
				event.preventDefault();
				var formData = $("#game").serialize();
				$.ajax({
				    url: 'GameHandler',
				    type: 'POST',
				    data:  formData,
					beforeSend: function() {
						//prevent player from submitting another response until 
						//the server is ready to accept another response
						$("#game :input").prop("disabled", true);
						$("img").show();
									
					},
					error: function(message) {
						$("img").hide();
						$("#status").append("Error encountered while submitting response to game server. <br />");
				    	
					},
				    success: function(result) {
				    	$("img").hide();
				    	
				    	//if game is over
				    	if (result.indexOf("Thank you for playing") >= 0) {
				    		$("#gameForm").hide();
				    		$("#status").append(result + " <br />");
				    		//show replay button
				    	}
				    	
				    	//if no response from server for 20 seconds
				    	//i.e., the other player disconnected
				    	else if (result.indexOf("timeout") >= 0) {
				    		
				    	}
				    	
				    	else {
				    		$('#game')[0].reset();
					       	$("#game :input").prop("disabled", false);
					       	$("#status").append(result + " <br />");
				    	}		       	
				    },
				    statusCode: {
				        500: function() {
				        	$("#status").append("An error has occurred in the servlet. <br />");
				    	}
				    }
				});
			});
			
			
		});