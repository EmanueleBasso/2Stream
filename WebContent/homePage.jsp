<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	    <meta name="viewport" content="width=device-width,initial-scale=1">
	    <link rel="icon" type="image/png" href="img/logo.png">
	    <link rel="stylesheet" type="text/css" href="css/noScript.css">
		<link rel="stylesheet" type="text/css" href="css/homePageStyle.css" >	
		<link rel="stylesheet" type="text/css" href="css/chatStyle.css" >
		<link rel="stylesheet" type="text/css" href="css/textAndcolor.css" >
		<link rel="stylesheet" type="text/css" href="css/settingsStyle.css" >
	    <script src="script/jquery.js"></script>
		<script src="script/convertData.js"></script>
		<script src="script/homePage.js"></script>
		<script src="script/homePageResponsive.js"></script>
		<script src="script/ajax.js"></script>
		<script src="script/messaggi.js"></script>
		<script src="script/contatti.js"></script>
		<script src="script/settings.js"></script>
		<script>loadPreference();</script>
		<title>2Stream</title>
	</head>
	<body>
		<header id="headBar">
			<div id="leftHeader">
				<div class="settingsIco" onclick="showMenu($(this))">
					<span class="bar top"></span>
					<span class="bar middle"></span>
					<span class="bar bottom"></span>
				</div>
			</div>
			<div id="rightHeader">
				<div id="freccia" onclick="showLeft()">
						<span class="puntaUp"></span>
						<span class="asta"></span>
						<span class="puntaDown"></span>
				</div>
			</div>
		</header>
		<div id="contents">
			<div id="left">
				<nav id="settingsMenu">
					<ul id="settingsItem">
						<li>
							<a onclick="settingsLoad();">
								<img src="ico/settings.svg"/>
								<span>Settings</span>
							</a>
						</li>
						<li>
							<a onclick="infoLoad();">
								<img src="ico/info.svg"/>
								<span>Info</span>
							</a>
						</li>
						<li>
							<a onclick="logout();">
								<img src="ico/logout.svg"/>
								<span>Esci</span>
							</a>
						</li>
					</ul>
				</nav>
				<div id="chatList">
					<header id="listSeachHead">
						<input class="searchField" type="search" placeholder="Cerca.." onkeyup="search($(this));">
					</header>
						<ul id="contact"></ul>
				</div>
			</div>
			
			<div id="right">
				<div id="contenutiMenu"></div>
				<%@include file="chat.html" %>
			</div>
		</div>
		<%@include file="noScript.html" %>
	</body>
</html>
