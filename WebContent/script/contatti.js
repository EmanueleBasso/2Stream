var timeoutAggiornamentoListaContatti = null

function search(campo) {
	$("#contact").css("display","none");
	if($("#clean").length == 0)
		$("#listSeachHead").prepend("<label id=\"clean\" onclick=\"clearResearch();\"><img src=\"ico/undo.svg\"/></label>");
	searchUser(campo);
}

function searchUser(campo) {
	var contattiStringa = "<ul class=\"ricerca\" id=\"ricercaContatti\"><li id=\"titoloRicerca\">Ricerca Contatti</li>";
	var nameContact = newAjax();

	if(nameContact!=false){
		nameContact.onreadystatechange = function(){
			if(nameContact.readyState==4 && nameContact.status==200){
				JSON.parse(nameContact.responseText).forEach(function(user) {
					contattiStringa += "<li><a class=\"contatto\" onclick=\"openChat(\'"+user.name+"\');\"><img src=\"data:image/jpeg;base64,"+user.image+"\"/><span>"+user.name+"</span></a></li>";
				});
				contattiStringa += "</ul>";

				var ricercaDiv = $("#ricercaContatti");
				if(ricercaDiv.length == 0){
					$("#chatList").append(contattiStringa);
					}
				else
					ricercaDiv.replaceWith(contattiStringa);
			}
		}
		nameContact.open("get","SearchChat?sequence="+campo.val());
		nameContact.send(null);
	}
	else
		redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
}

$(document).ready(function() {
	var notifica = [];
	
	var aggiornaLista = function() {
		var contattiSalvati = [];
		contattiSalvati[0] = '<ul id=\"contact\">';
		var chatAperta = $("#chatAperta");
		
		var nameContact = newAjax();
		if(nameContact != false){
			nameContact.onreadystatechange = function(){
				if(nameContact.readyState==4 && nameContact.status==200){
					var count=1;
					var contatti = JSON.parse(nameContact.responseText);
					if(notifica.length > 0){
						var contatto;
						notifica.forEach(function(not) {
							contatto = contatti.find(function(element) {
								return element.name == not.nome;
							});
							if(contatto != undefined && contatto.name!=chatAperta.text())
								if((contatto.ultimaModifica != not.data)){
									not.notifica = true;
									new Audio('sound/message.mp3').play();
								}
								else if(($(".notificaPush > span").text()==contatto.name)){
									not.notifica = true;
								}
						});
					}
					
					contatti.forEach(function(user) {
						contattiSalvati[count] ="<li><a class=\"contatto";
						
						if(notifica.length > 0){
							var contatto = notifica.find(function(element) {
								return element.nome == user.name;
							});
							if(contatto != undefined)
								if(contatto.notifica){
									contattiSalvati[count] += " notificaPush";
								}
						}
						
						contattiSalvati[count] += "\" onclick=\"openChat(\'"+user.name+"\');\"><img src=\"data:image/jpeg;base64,"+user.image+"\"/><span>"+user.name+"</span>";
						if(chatAperta.text() == user.name)
							contattiSalvati[count] += "<span id=\"chatAperta\">"+chatAperta.text()+"</span>";
						
						contattiSalvati[count] +="</a></li>";
						count++;
						
					});
					notifica = [];
					contatti.forEach(user => notifica.push({nome: user.name, data: user.ultimaModifica, notifica: false}));
					contattiSalvati[count]="</ul>";

					var listaContatti = $("#contact");
					if(listaContatti.css("display") != "none"){
						if(count != 1)
							listaContatti.html(contattiSalvati);
					}
					
					$(".contatto").click(function() {
						$("#chatAperta").remove()
						$(this).append('<span id="chatAperta">'+$(this).children()[1].innerText+'</span>');
					});
					
					timeoutAggiornamentoListaContatti = setTimeout(aggiornaLista,1000);
				}
			}
			nameContact.open("get","AggiornamentoContatti");
			nameContact.send(null);
		}
		else
			redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
	}

	aggiornaLista();
});
