<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	    <meta name="viewport" content="width=device-width,initial-scale=1">
	    <link rel="icon" type="image/png" href="img/logo.png">
		<link rel="stylesheet" type="text/css" href="css/noScript.css">
		<link rel="stylesheet" type="text/css" href="css/centerFormStyle.css" >
		<link rel="stylesheet" type="text/css" href="css/adminPanelStyle.css" >
		<link rel="stylesheet" type="text/css" href="css/textAndcolor.css" >
	    <script src="script/jquery.js"></script>
		<script src="script/chart.js"></script>
		<script src="script/ajax.js"></script>
		<script src="script/regexControl.js"></script>
		<script src="script/adminPanel.js"></script>
		<title>2Stream</title>
	</head>
	<body>
		<div id="contentDiv">
			<nav id="headBar">
				<ul id="list">
					<li class="item">
						<a class="buttonActive" onclick="changeTab($(this),1);">
							<img src="ico/database.svg"/>
							<span>Statistiche Memorizzazione</span>
						</a>
					</li>
					<li class="item">
						<a onclick="changeTab($(this),2);">
							<img src="ico/prestation.svg"/>
							<span>Statistiche Prestazioni</span>
						</a>
					</li>
					<li class="item">
						<a onclick="changeTab($(this),3);">
							<img src="ico/settings.svg"/>
							<span>Settings</span>
						</a>
					</li>
					<li class="item">
						<a href="LogoutAdmin">
							<img src="ico/logout.svg"/>
							<span>Logout</span>
						</a>
					</li>
				</ul>
			</nav>

			<div id="tabs">
				<div id="database" class="tab tabActive">
					<div class="info">
						<p>Numero di utenti iscritti:&nbsp;<span class="valore"><%= request.getAttribute("iscritti") %></span></p>
						<p>Numero di chat:&nbsp;<span class="valore"><%= request.getAttribute("chat") %></span></p>
						<p>Numero messaggi testuali scambiati:&nbsp;<span class="valore"><%= request.getAttribute("testuali") %></span></p>
						<p>Numero messaggi multimediali scambiati:&nbsp;<span class="valore"><%= request.getAttribute("multimediali") %></span></p>
						<p>Numero messaggi totali scambiati:&nbsp;<span class="valore"><%= request.getAttribute("totaleMex") %></span></p>
					</div>
					<div id="graficoTorta">
						<input type="hidden" id="occupazione" value="<%= request.getAttribute("occupazione") %>">
						<input type="hidden" id="quota" value="<%= request.getAttribute("quota") %>">
						<br/>
						<canvas id="tortaCanvas"/>
					</div>
				</div>

				<div id="prestazioni" class="tab">
					<div class="info">
						<p>Numero iscritti giornalieri:&nbsp;<span class="valore"><%= request.getAttribute("iscrittiOggi") %></span></p>
						<p>Numero autenticazioni errate:&nbsp;<span class="valore"><%= request.getAttribute("autenticazioniErrate") %></span></p>
						<p>Numero sessioni invalidate:&nbsp;<span class="valore"><%= request.getAttribute("sessioniInvalidate") %></span></p>
					</div>
					<div class="graficiBarre">
						<input type="hidden" id="connessioni" value="<%= request.getAttribute("connessioni") %>">
						<input type="hidden" id="maxConnessioni" value="<%= request.getAttribute("maxConnessioni") %>">
						<canvas id="connessioniCanvas"/>
					</div>
					<div class="graficiBarre">
						<input type="hidden" id="online" value="<%= request.getAttribute("online") %>">
						<input type="hidden" id="maxOnline" value="<%= request.getAttribute("maxOnline") %>">
						<canvas id="utentiCanvas"/>
					</div>
				</div>

				<div id="settings" class="tab">
					<div id="updatePassword">
						<div class="blockInputElement">
							<span>Username:&nbsp;</span><span id="usernameCampo"><%= request.getAttribute("username") %></span>
						</div>
						<div class="blockInputElement">
							<span>&nbsp;Password:&nbsp;</span>
							<input id="password" name="password" type="password" placeholder="**************" onblur="checkPassword($(this));"/>
						</div>
						<input id="submit" class="submitButton" type="button" value="Salva" onclick="sendPassword();"/>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
