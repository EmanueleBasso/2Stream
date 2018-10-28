function showMenu(menu){
	var settings = $("#settingsMenu");
	var chatList = $("#chatList");
	
	if(settings.css("display") == "none"){
		chatList.css("display","none");
		settings.css("display","block");
	}
	else{
		settings.css("display","none");
		chatList.css("display","block");
		changeRightDisplay("chat");
	}
	
	menu.toggleClass("changeToX");
}

function clearRight() {
	$("#textBox").empty();
	
	var userImageHeader = $("#userBox");
	if(userImageHeader != null)
		userImageHeader.remove();

	var online = $("#rightInfo");
	if(online != null)
		online.remove();
	
	if (timeoutAggiornamentoMessaggi != null)
		clearTimeout(timeoutAggiornamentoMessaggi);
}

function changeRightDisplay(action) {
	var contenutiMenu = $("#contenutiMenu");
	var messageContent = $("#MessageContent");
	
	if(action == "menu"){
		messageContent.css("display","none");
		contenutiMenu.css("display","block");
	}else if(action == "chat"){
		contenutiMenu.empty();
		contenutiMenu.css("display","none");		
		messageContent.css("display","block");
	}
}

function infoLoad() {
	clearRight();
	changeRightDisplay("menu");
    $("#contenutiMenu").load("info.html");
    showRight();
}

function settingsLoad() {
	clearRight();
	changeRightDisplay("menu");
    $("#contenutiMenu").load("settings.html");
    showRight();
}

function clearResearch() {
	$(".ricerca").remove();
	$("#contact").css("display","block");
	$(".searchField").val("");
	$("#clean").remove();
}

function openMenu(){
	$("#menuBox").toggle();
}

$(function() {
    $("#text").keypress(function (e) {
        var code = e.charCode || e.keyCode; 
        if(code == 13 && !e.shiftKey){ 
          e.preventDefault(); 
          aggiungiInCoda();
        }
    }); 
});

function loadingExit(){
	$("body").children().each(function() {
		$(this).fadeTo("fast",0.25);
		$(this).css("z-index",1);
	});
    $("body").append("<img id='spinner' src='ico/spinner.svg' />");
    $("#spinner").css("z-index",2);
}

function logout() {
	loadingExit();

	clearTimeout(timeoutAggiornamentoMessaggi);
	clearTimeout(timeoutAggiornamentoListaContatti);
	
	listaVuota();
}

function listaVuota(){
	if(listaMessaggiDaInviare.length == 0)
		redirect("Logout","");
	else
		setTimeout(listaVuota,500);
}
