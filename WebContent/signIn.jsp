<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width,initial-scale=1">
	    <link rel="icon" type="image/png" href="img/logo.png">
		<title>Accesso</title>
		<link rel="stylesheet" type="text/css" href="css/signInStyle.css">
		<link rel="stylesheet" type="text/css" href="css/centerFormStyle.css">
		<link rel="stylesheet" type="text/css" href="css/noScript.css">
		<link rel="stylesheet" type="text/css" href="css/textAndcolor.css">
	    <script src="script/jquery.js"></script>
	    <script src="script/regexControl.js"></script>
		<script src="script/signIn.js"></script>
	</head>
	<body>
		<section class="wallpaper"></section>
		<div class="centerForm">
			<h1 class="titleSign">
					Accedi a 2Stream
			</h1>
			<form id="signForm" action="Accesso" method="post" onsubmit="return check();">
				<div class="blockInputElement">
					<input name="username" class="fcampo" type="text" placeholder="Username" onblur="checkNome($(this));"/>
				</div>
				<div class="blockInputElement">
					<input name="password" class="fcampo" type="password" placeholder="Password" onblur="checkPassword($(this));"/>
				</div>
				<input id="submit" class="submitButton" type="submit" value="Accedi"/>
				<p>Non hai un account 2Stream?</p>
				<a href="signUp.jsp" class="linkReg">Registrati!</a>
			</form>
			<%
					String error=request.getParameter("err");
					if(error!=null){
						if(error.equals("paramsErr")){%>
							<script type="text/javascript">check();</script>
					<%}%>
						<%if(error.equals("notReg")) {%>
							<script type="text/javascript">error(true,$(".fcampo").eq(0),"Username o password errata");</script>
					<%}
						}%>
		</div>
		
		<%@include file="noScript.html" %>
	</body>
</html>