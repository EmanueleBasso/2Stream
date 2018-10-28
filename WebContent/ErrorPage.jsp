<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
	<head>
	    <meta name="viewport" content="width=device-width,initial-scale=1">
	    <link rel="icon" type="image/png" href="img/logo.png">
		<link rel="stylesheet" type="text/css" href="css/errorPageStyle.css">
		<link rel="stylesheet" type="text/css" href="css/textAndcolor.css">
		<link rel="stylesheet" type="text/css" href="css/centerFormStyle.css">
		<title>Errore</title>
	</head>
<body>
	<section class="wallpaper"></section>
	<div>
		<div class="errorImage">
			<img src="img/error.svg" alt="Immagine errore">
		</div>
		<div class="errorString">
			Questo non era previsto...<br/>
			Prova a ricaricare la pagina o contattaci<br/>
			Errore:
 		<%
 			String error = request.getParameter("ErrorCode");
			if(error != null)
 				out.print(error);
 			else
 				out.print(response.getStatus());
 		%>
 		</div>
	</div>
</body>
</html>
