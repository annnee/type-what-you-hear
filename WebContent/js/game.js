/**
 * 
 */
$(document).ready(function() {		
	var soundFiles;
	var queue = new createjs.LoadQueue();
	var roundNum = 1;
	
	var gameStartClock = $('.game-start').FlipClock(3.5, {
		clockFace: 'Counter',
	});
	
	
	
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
	
	$("#connect").submit(function(event) {
		event.preventDefault();
		var formData = JSON.stringify($('#connect').serializeObject());
		//show game form, hide ready button 
		$("#connect").hide();
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
				$("#status").append("Both players have connected! <br />");

				//let server know it is ready to receive the noisy tokens
				retrieveNoisyTokens(event);
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
		sendResponseToServer();
	});

	//retrieve list of sound files from the server
	function retrieveNoisyTokens(event){
		event.preventDefault();
		var formData = JSON.stringify($('#gameSetup').serializeObject());
		$.ajax({
			url: 'GameHandler',
			type: 'POST',
			data:  {json: formData},
			beforeSend: function() {
				//prevent player from submitting another response until 
				//the server is ready to accept another response
				$("#status").append("Loading sound files... <br />");

			},
			error: function(message) {
				$("img").hide();
				$("#status").append("Error encountered while loading sound files. <br />");

			},
			success: function(result) {
				//load sound files
				soundFiles = $.parseJSON(result);
				
				
				queue.installPlugin(createjs.Sound);
				queue.on("complete", handleComplete, this);
				queue.loadManifest([{id:"round1", src:"sound/"+soundFiles[0]},
				                    {id:"round2", src:"sound/"+soundFiles[1]},
				                    {id:"round3", src:"sound/"+soundFiles[2]},
				                    {id:"round4", src:"sound/"+soundFiles[3]},
				                    {id:"round5", src:"sound/"+soundFiles[4]}]);

				function handleComplete() {
					console.log("Sound files loaded!");
					informServer(event);
				}
			},
			statusCode: {
				500: function() {
					$("#status").append("An error has occurred in the servlet. <br />");
				}
			}
		});
		
	}

	//sends a message to the server informing that
	//it has finished preloading the noisy tokens
	function informServer(event) {
		event.preventDefault();
		$('#gameSetup input[name="message"]').val('Finished preloading');
		var formData = JSON.stringify($('#gameSetup').serializeObject());
		$.ajax({
		    url: 'GameHandler',
		    type: 'POST',
		    data:  {json: formData},
			beforeSend: function() {
				//prevent player from submitting another response until 
				//the server is ready to accept another response
				$("#status").append("Waiting for the other player to finish loading the sound files... <br />");
							
			},
			error: function(message) {
				$("img").hide();
				$("#status").append("Error encountered while waiting for the other player to load the sound files. <br />");	    	
			},
		    success: function(result) {
		    	$("img").hide();		
				$("#status").append("Both players have finished loading sound files! <br />");
				$("#status").append(result);
				
				setTimeout(function() {
					$("#gameForm").show();
					createjs.Sound.play("round1");
					roundNum++;
				}, 3000);
				
		    },
		    statusCode: {
		        500: function() {
		        	$("#status").append("An error has occurred in the servlet. <br />");
		    	}
		    }
		});
	}


	function sendResponseToServer() {
		var playerResponse = $('#game input[name="message"]').val();
		$('#game input[name="message"]').val(playerResponse.toUpperCase());
		var formData = JSON.stringify($('#game').serializeObject());
		$.ajax({
			url: 'GameHandler',
			type: 'POST',
			data:  {json: formData},
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

				//if no response from server for 20 seconds
				//i.e., the other player disconnected
				if (result.indexOf("timeout") >= 0) {
					$("#status").append("No response from server for 30 seconds. " +
					"Either the server is down or we haven't received a response from the other player. <br />");
					$("#gameForm").hide();
				}

				//game is still running
				else {	
					result = $.parseJSON(result);
					
					
					//check if game is over
					if (result["correctAnswer"].indexOf("LAST") >= 0) {
						$("#gameForm").hide();
						result["correctAnswer"] = result["correctAnswer"].replace("LAST ", "");
						$("#status").append("Correct Answer: " + result["correctAnswer"] + " <br />" 
								+ "This round's score: " + result["score"] + " <br />"
								+ "Total Score: " + result["totalScore"] + " <br />");
						//show replay button
					}
					
					else {
						$('#game')[0].reset();
						$("#game :input").prop("disabled", false);
						$("#status").append("Correct Answer: " + result["correctAnswer"] + " <br />" 
								+ "This round's score: " + result["score"] + " <br />"
								+ "Total Score: " + result["totalScore"] + " <br />");		
						//play next sound file
						setTimeout(function(){
							var id = "round"+roundNum;
							createjs.Sound.play(id);
							roundNum++;
						},500);
					}
					
				}		       	
			},
			statusCode: {
				500: function() {
					$("#status").append("An error has occurred in the servlet. <br />");
				}
			}
		});
	}
});

