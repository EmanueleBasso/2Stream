var listaMessaggiDaInviare = new Array();
var timeoutAggiornamentoMessaggi = null;
var timeoutInvioMessaggi = null;
var semaforo = false;
var chatAttuale = null;

function messaggioDaInviare(contenuto,destinatario){
	    this.contenuto = contenuto;
	    this.destinatario = destinatario;
}

function aggiungiInCoda(){
	  var textArea = $("#text");
	  var contenuto = textArea.val();
	  textArea.val("");
	  var destinatario = $("#destinatario").val();
	  
	  var messaggio = new messaggioDaInviare(contenuto,destinatario);
	  listaMessaggiDaInviare.unshift(messaggio);
	  
	  if( !semaforo && (listaMessaggiDaInviare.length == 1)){
		  clearTimeout(timeoutInvioMessaggi);
		  inviaMessaggio();
	  }
}

function inviaMessaggio(){
	  if(listaMessaggiDaInviare.length == 0){
		  timeoutInvioMessaggi = setTimeout(inviaMessaggio,3000);
		  return;
	  }
	  semaforo = true;
		  
	  var messaggio = listaMessaggiDaInviare.pop();
	  
	  var ora = Date.now();
	  var timezone = new Date().getTimezoneOffset();
	  		  
	  if(messaggio.contenuto != ""){
	    var sendMessage = newAjax();
	    
	    if(sendMessage != null){
	      sendMessage.onreadystatechange = function(){
	    	  if(sendMessage.readyState==4 && sendMessage.status==200){
	    		var divMessaggi = $("#textBox");
	    		divMessaggi.scrollTop(divMessaggi[0].scrollHeight);
	    		  
			    if(listaMessaggiDaInviare.length != 0)
			    	inviaMessaggio();
			    else{
			    	timeoutInvioMessaggi = setTimeout(inviaMessaggio,3000);
			  	  	semaforo = false;
			    }
			  }
		  }
	      sendMessage.open("post","InvioMessaggi"); 
	      sendMessage.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	      sendMessage.send("messaggio="+filter(messaggio.contenuto)+"&ora="+ora+"&timezone="+timezone+"&destinatario="+messaggio.destinatario);
	    }
	    else
			redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
	  }
}

function filter(string) { 
	  return string.replace(/[^0-9a-zA-Z]/gim, function(character) {
	       return encodeURIComponent('&#'+character.charCodeAt(0)+';'); 
	  });
}

function insertMessaggio(messaggio,mittente,data) {
	var dataEora = convertDateToLocalTime(data);
	var dataMess = "<span class=\"data\">" + dataEora.data + "</span>";
	var oraMess = "<span class=\"ora\">" + dataEora.ora + "</span>";

	if(mittente)
		return "<div id=\"inviatoBox\"><div class=\"inviato\"><div class=\"testo\">" + messaggio + "</div>" + dataMess + "&nbsp;" + oraMess + "</div></div>";
	else
		return "<div id=\"ricevutoBox\"><div class=\"ricevuto\"><div class=\"testo\">" + messaggio + "</div>" + dataMess + "&nbsp;" + oraMess + "</div></div>";
}

function printMessage(array,user) {
	var messaggi = "";
	array.forEach(function(message) {
		if(message.contenutoMultimediale == null)
			messaggi += insertMessaggio(message.contenutoTestuale, message.mittente != user,message.dataInvio);
		else{
			var contenuto = "<img id=\"multimediaImage\" onclick=\"openImage($(this));\" src=\"data:image/jpeg;base64,"+message.contenutoMultimediale+"\"/>";
			messaggi += insertMessaggio(contenuto, message.mittente != user,message.dataInvio);
		}	
	});
	return messaggi;
}

function searchID(arr) {
	if(arr.length > 0){
		return {ultimo: arr[arr.length - 1].id, primo: arr[0].id};
	}else
		return {ultimo: 0, primo : 0};
}

function addOnlineDiv(objStatus) {
	if(objStatus.stato != "privacy")
		$("#rightHeader").append('<div id=rightInfo><div id="userStatus"><span id="onlineStatusText">'+objStatus.stato+'</span></div></div>');
	if(objStatus.stato == "offline"){
			var orario = $("#userStatus");
			var tempo = convertDateToLocalTime(objStatus.accesso); 
			orario.addClass("popup");
			orario.append(' <span class="popuptext" id="accesso">'+tempo.data+" "+new Date(objStatus.accesso).getFullYear()+" "+tempo.ora+'</span>');
	}
}

function online(user) {	
	var userOnline = newAjax();
	if(userOnline!=false){
		userOnline.onreadystatechange = function() {
			if(userOnline.readyState==4 && userOnline.status==200){
				var online = $("#rightInfo");
				var objStatus = JSON.parse(userOnline.responseText);
				if(online != null){
					if($("#onlineStatusText").text() != objStatus.stato){
						online.remove();
						addOnlineDiv(objStatus);
					}
				}else
					addOnlineDiv(objStatus);
			}
		}	
	}else
		redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
	
	userOnline.open("get","UserOnline?username="+user);
	userOnline.send(null);
}

function openChat(user) {
	var idTestuale = {ultimo: 0, primo : 0};
	var idMultimediale = {ultimo: 0, primo : 0};
	
	var chatJson = newAjax();
	if(chatJson != false){
		clearTimeout(timeoutAggiornamentoMessaggi);
		
		chatJson.onreadystatechange = function() {
			if(chatJson.readyState==4 && chatJson.status==200){
				clearRight();
				var jsonObject = JSON.parse(chatJson.responseText);
				var image = jsonObject.immagine;
				var user =  jsonObject.username;
				var tuttiMessaggi = jsonObject.messaggi;
				
				chatAttuale = user;
								
				$("#rightHeader").append("<div id=\"userBox\" class=\"popup\">" +
						"<img id=\"headerImage\" src=\"data:image/jpeg;base64,"+image+"\"/>" +
								"<h2 id=\"usernameHeader\">"+user+"</h2><" +
										"/div>");
				
				$("#userBox").hover(function() {
					$("#stato").remove();
					var stato= newAjax();
					if(stato!=false){
						stato.onreadystatechange = function() {
							if(stato.readyState==4 && stato.status==200)
								$("#userBox").append("<div class=\"popuptext\" id=\"stato\">"+JSON.parse(stato.responseText).stato+"<img src=\"data:image/jpeg;base64,"+JSON.parse(stato.responseText).immagine+"\" id=\"imageLargeContact\"></div>");
						}
					}else
						redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
					stato.open("get","Stato?username="+user);
					stato.send(null);
				});

				online(user);
				$("#destinatario").attr("value",user);

				var divMessaggi = $("#textBox");
				if(tuttiMessaggi != null){
					if(tuttiMessaggi.length > 0){
						
						var arr = tuttiMessaggi.filter(function(message) {
							return message.contenutoMultimediale == null;
						});
						idTestuale = searchID(arr);
						
						arr = tuttiMessaggi.filter(function(message) {
							return message.contenutoMultimediale != null;
						});
						idMultimediale = searchID(arr);
					}
					
					divMessaggi.append(printMessage(tuttiMessaggi,user));
				}
				
				showRight();				
				divMessaggi.scrollTop(divMessaggi[0].scrollHeight);
								
				setTimeout(aggiornaMessaggi,1000);	
			}
		}
		chatJson.open("get","NuovaChat?username="+user);
		chatJson.send(null);
	}else{
		redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
	}
	
	var aggiornaMessaggi = function() {
		
		var oldChatM = chatAttuale;
		
		var messageJson = newAjax();
		if(messageJson != false){	
			messageJson.onreadystatechange = function(){
				if(messageJson.readyState==4 && messageJson.status==200){
					var divMessaggi = $("#textBox");
					if(divMessaggi.scrollTop()<((divMessaggi[0].scrollHeight*25)/100))
						messaggiPrecedenti();
					
					if(oldChatM == chatAttuale){
					var goDown = false;
					online(user);

					if((Math.floor((divMessaggi.scrollTop()+divMessaggi.height())) > (divMessaggi[0].scrollHeight - 10)))
						goDown=true; 

					var arrayMessaggi = JSON.parse(messageJson.responseText).messaggi;						
					divMessaggi.append(printMessage(arrayMessaggi,user));
					if(arrayMessaggi.length > 0){
						if(arrayMessaggi[arrayMessaggi.length - 1].contenutoMultimediale == null){
							idTestuale.ultimo =  arrayMessaggi[arrayMessaggi.length - 1].id;
						}
						else{
							idMultimediale.ultimo = arrayMessaggi[arrayMessaggi.length - 1].id;
						}
					}
					
					if(goDown)
          				divMessaggi.scrollTop(divMessaggi[0].scrollHeight);
					
					timeoutAggiornamentoMessaggi = setTimeout(aggiornaMessaggi,1000);
				}
				}
			}
			messageJson.open("get","MessaggiSuccessivi?username="+user+"&primoTestuale="+idTestuale.ultimo+"&primoMultimediale="+idMultimediale.ultimo);
			messageJson.send(null);
		}else{
			redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
		}
	}
	
	var messaggiPrecedenti = function() {
		
		var oldChatP = chatAttuale;
		
		var messageJson = newAjax();
		if(messageJson != false){		
			messageJson.onreadystatechange = function() {
				if(messageJson.readyState==4 && messageJson.status==200){
					if(oldChatP == chatAttuale){
						var divMessaggi = $("#textBox");
						var arrayMessaggi = JSON.parse(messageJson.responseText).messaggi;
									
						if(arrayMessaggi.length > 0){
							var oldScrollHeight = divMessaggi[0].scrollHeight;
							
							divMessaggi.prepend(printMessage(arrayMessaggi,user));
							
							if(navigator.userAgent.match("Firefox") != null)
								divMessaggi.scrollTop(divMessaggi.scrollTop() - oldScrollHeight + divMessaggi[0].scrollHeight);
							
							if(arrayMessaggi[0].contenutoMultimediale == null){
								idTestuale.primo =  arrayMessaggi[0].id;
							}
							else{
								idMultimediale.primo = arrayMessaggi[0].id;
							}
						}
					}
				}
		}
		messageJson.open("get","MessaggiPrecedenti?username="+user+"&ultimoTestuale="+idTestuale.primo+"&ultimoMultimediale="+idMultimediale.primo);
		messageJson.send(null);
		}else
			redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
	}
	
}

function sendImage(image){
	var sendImage = newAjax();
	
	if(sendImage != false){
		var formData = new FormData();
		formData.append("contenuto", image[0].files[0]);
		formData.append("ora", Date.now());
		formData.append("timezone", new Date().getTimezoneOffset());
		formData.append("destinatario", $("#destinatario").val());
		sendImage.open("post","InvioMultimedia");
		sendImage.send(formData);
	}else
		redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
}

function openImage(img){
	var modal = $("#myModal");
	$('#img01').attr("src",img.attr("src"));
	modal.css("display","block");
}

function closeImage() {
	var modal = $("#myModal");
	modal.css("display","none");
}
