<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="http://getbootstrap.com/examples/cover/cover.css">
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
                <li class="active"><a href="index.jsp">Home</a></li>
                <li><a href="#">Project Description</a></li>
                <li><a href="login.jsp">Login</a></li>
              </ul>
            </div>
          </div>

          <div class="inner cover">
            <h1 class="cover-heading">Listen, Listen! : The Type-What-You-Hear Game</h1>
            <p class="lead">A project by Ann Nee Lau, supervised by Dr. Jon Barker</p>
            <p class="lead">
              <a href="#" id="lm" class="btn btn-lg btn-default">Learn More</a>
              <a href="#" id="sound" class="btn btn-lg btn-default">Click me</a>
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
	<script>
	$(document).ready(function() {
		$("#lm").click(function(){
			var queue = new createjs.LoadQueue();
			 queue.installPlugin(createjs.Sound);
			 queue.on("complete", handleComplete, this);
			 queue.loadManifest([{id:"round1", src:"sound/from_s1.wavEN_bmn3.wav"},
					 {id:"round2", src:"sound/prove_s1.wavEN_bab3.wav"}]);
			 
			 function handleComplete() {
				 console.log("Sound files loaded!");
			     		     
			 }
		});
		
		$("#sound").click(function(){
			createjs.Sound.play("round2");
			setTimeout(function(){createjs.Sound.play("round1")},3000);
		
		});
	});
	</script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
</body>
</html>