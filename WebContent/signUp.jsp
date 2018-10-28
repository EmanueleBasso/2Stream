<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
        <head>
            <meta name="viewport" content="width=device-width,initial-scale=1">
	  		<link rel="icon" type="image/png" href="img/logo.png">
        	<link rel="stylesheet" type="text/css" href="css/signUpStyle.css">
        	<link rel="stylesheet" type="text/css" href="css/centerFormStyle.css">
       	 	<link rel="stylesheet" type="text/css" href="css/noScript.css">
        	<link rel="stylesheet" type="text/css" href="css/textAndcolor.css">
        	<title>Registrazione</title>
      		<script src="script/jquery.js"></script>
      		<script src="script/ajax.js"></script>
      		<script src="script/regexControl.js"></script>
			<script src="script/signUp.js"></script>
        </head>
        <body>
            <section class="wallpaper"></section>
			<div class="centerForm">
				<div id="freccia" onclick="goBack()">
					<span class="asta"></span>
					<span class="puntaDown"></span>
					<span class="puntaUp"></span>
				</div>
				<h1 class="titleSign">
					Registrati
				</h1>
				<form  id="signForm" action="Registrazione" method="post" enctype="multipart/form-data">
					<div id="firstScreen">
						<div class="blockInputElement">
							<input name="username" class="fcampo" type="text" placeholder="Username" onblur="checkNome($(this));"/>
						</div>
						<div class="blockInputElement">
							<input name="email" class="fcampo" type="email" placeholder="Email" onblur="checkEmail($(this));"/>
						</div>
						<div class="blockInputElement">
							<input name="password" class="fcampo" type="password" placeholder="Password" onblur="checkPassword($(this));"/>
						</div>
						<div class="blockInputElement">
							<input name="termAndCond" class="fcampo" type="checkbox" value="Accetto"/>&nbsp;
							<span id="termAndCondLink" onclick="showTerms();">Accetto i Termini e le Condizioni</span>
						</div>
						<input id="nextFields" class="submitButton" onclick="check();" type="button" value="Avanti"/>
					</div>
					<div id="termAndCondDisplay">
						<p id="termText">
							<%@include file="TermAndConditions.html" %>
						<p> 
					</div>
					<div id="secondScreen">
						<div class="blockInputElement">
							<input name="immagine" class="scampo" id="file" type="file" accept="image/*" onchange="changePhoto($(this));"/>
						</div>
						<div class="blockInputElement">
							<label for="file" id="selectPhoto">
								<img src="img/profile.jpg" id="image" alt="Immagine profilo"/>
								<span class="middleText">
									<span class="text">Carica foto</span>
								</span>
							</label>
						</div>
						<div class="blockInputElement">
							<input name="stato" class="scampo" type="text" placeholder="Stato"/>
						</div>
						<input id="submit" class="submitButton" type="submit" value="Registrati"/>
					</div>
				</form>
				<%
					String error=request.getParameter("err");
					if(error!=null)
						if(error.equals("paramsErr")){%>
							<script type="text/javascript">check();</script>
					<%}%>
			</div>
			
        	<%@include file="noScript.html" %>
		</body>
</html>
