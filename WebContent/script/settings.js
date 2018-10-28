var preferenzeObj = {
	immagine:"immagine",
	sfondo:"sfondo",
	password:"password",
	font:"font",
	stato:"stato",
	privacyAccesso: false,
	privacyImmagine: false,
	tema : "#7e1412"
};

// prende le preferenze dell'utente dal database al caricamento della homepage e cambia il css
function loadPreference() {
	var preferenze = newAjax();
	if(preferenze!=null){
		preferenze.onreadystatechange = function(){
			if(preferenze.readyState==4 && preferenze.status==200){
				var settings = JSON.parse(preferenze.responseText);
			
				preferenzeObj.immagine = settings.immagine;
				preferenzeObj.sfondo = settings.sfondo;
				preferenzeObj.stato = settings.stato;
				preferenzeObj.privacyAccesso = settings.privacyAccesso;
				preferenzeObj.privacyImmagine = settings.privacyImmagine;
				preferenzeObj.tema = settings.tema;
				preferenzeObj.font = settings.font;
				
				changeCSS();
			}
		}
		preferenze.open("get","Preferenza");
		preferenze.send(null);
	}
	else
        redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
};

//setta le preferenze attuali prendendole dall'oggetto preferenzeObj
function setPreference() {
	$("#immagineUtente").attr("src","data:image/jpeg;base64,"+preferenzeObj.immagine);
	$("#sfondoUtente").attr("src","data:image/jpeg;base64,"+preferenzeObj.sfondo);
	$("#status").attr("placeholder", preferenzeObj.stato);
	$("#checkPhoto").prop("checked", preferenzeObj.privacyAccesso);
	$("#checkAccess").prop("checked", preferenzeObj.privacyImmagine);
	$("#colore").val(preferenzeObj.tema);
}

//cambia le preferenze dopo che si è premuto su "salva" con quelle impostate
function changePreference(){
	
	preferenzeObj.immagine = $("#immagineUtente").attr("src").substring(23);
	preferenzeObj.sfondo = $("#sfondoUtente").attr("src").substring(23);
	
	var pass = $("#password").val();
	if(pass != "")
		preferenzeObj.password = pass;
	
	var stato =  $("#status").val();
	if(stato != "")
		preferenzeObj.stato = stato;

	preferenzeObj.privacyAccesso = $("#checkAccess").is(':checked');

	preferenzeObj.privacyImmagine = $("#checkPhoto").is(':checked');
	
	var tema =  $("#colore").val();
	if(tema != "")
		preferenzeObj.tema = tema;
	

	preferenzeObj.font = $("#font").find(":selected").text();
	
	changeCSS();
	sendPreference();
}


function sendPreference() {
	var preferenze = newAjax();
    if(preferenze != false){
        var formData = new FormData();

        var immagine = $("#sendpicProfile")[0].files[0];
        if(immagine != undefined)
        	formData.append("immagineProfilo", immagine);
       
        immagine = $("#senddisplayProfile")[0].files[0];
        if(immagine != undefined)
        	formData.append("immagineSfondo", immagine);
        
        formData.append("coloreTema", preferenzeObj.tema)
        formData.append("privacyAccesso", preferenzeObj.privacyAccesso);
        formData.append("privacyImmagine",preferenzeObj.privacyImmagine);
        formData.append("font",preferenzeObj.font);
        formData.append("password",preferenzeObj.password);
        formData.append("stato",preferenzeObj.stato);
       
        preferenze.open("post","SetPreferenza");
        preferenze.send(formData);
    }else
        redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
}

function changePhotoPic(input) {
    if (input[0].files && input[0].files[0]) {
        var fReader = new FileReader();
        fReader.readAsDataURL(input[0].files[0]);
 
        fReader.onloadend = function(event){
            $('#immagineUtente').attr("src", event.target.result);
        }
    }
}
 
function changePhotoWallpaper(input) {
    if (input[0].files && input[0].files[0]) {
        var fReader = new FileReader();
        fReader.readAsDataURL(input[0].files[0]);
 
        fReader.onloadend = function(event){
            $('#sfondoUtente').attr("src", event.target.result);
        }
    }
}

//cambia il css in base ai valori contenuti nell'oggetto preferenzeObj
function changeCSS(){
	 var limite = document.styleSheets.length;
	 
	 var stileCercato;
	    for(i = 0;i < limite;i++){
	    	var refStile = document.styleSheets[i].href;
	        if( refStile != null ) {
	        	if(refStile.includes("textAndcolor.css")){
		            stileCercato = document.styleSheets[i];	 
	        	}
	        }
	    }
	    if(stileCercato == null)
	    	return false;
	    
	    var prop = stileCercato.cssRules;
	    
        var stile = prop[1].style;
        stile.setProperty("font-family",preferenzeObj.font);
        
        stile = prop[2].style;
        stile.setProperty("background-color",preferenzeObj.tema);
	    
        stile = prop[3].style;
        stile.setProperty("border-color",preferenzeObj.tema + " transparent transparent transparent");
        
        stile = prop[4].style;
        stile.setProperty("border-color"," transparent transparent transparent " + preferenzeObj.tema );

        stile = prop[5].style;
        stile.setProperty("border-color"," transparent " + preferenzeObj.tema + " transparent transparent ");
        
        stile = prop[6].style;
        stile.setProperty("border-bottom-color", preferenzeObj.tema );

        stile = prop[7].style;
        stile.setProperty("border-right-color", preferenzeObj.tema );

        stile = prop[8].style;
        stile.setProperty("outline-color", preferenzeObj.tema );

        stile = prop[9].style;
        stile.setProperty("border", "2px dashed" + preferenzeObj.tema );
        
        var coloreSecondario = increase_brightness(preferenzeObj.tema , 25);
        stile = prop[10].style;
        stile.setProperty("background-color", coloreSecondario );
        
       if(navigator.userAgent.match("Firefox") == null){
	        stile = prop[13].style;
	        stile.setProperty("background",coloreSecondario );

	        stile = prop[14].style;
	        stile.setProperty("background",preferenzeObj.tema );
	        
	        stile = prop[15].style;
	        stile.setProperty("border-color"," transparent transparent "  + preferenzeObj.tema + " transparent ");

	        stile = prop[16].style;
	        stile.setProperty("border-color",preferenzeObj.tema );
        }else{
	        stile = prop[11].style;
	        stile.setProperty("border-color"," transparent transparent "  + preferenzeObj.tema + " transparent ");

	        stile = prop[12].style;
	        stile.setProperty("border-color",preferenzeObj.tema );
        }
       	
       	
	    for(i = 0;i < limite;i++){
	    	var refStile = document.styleSheets[i].href;
	        if( refStile != null ) {
	        	if(refStile.includes("chatStyle.css")){
		            stileCercato = document.styleSheets[i];	 
	        	}
	        }
	    }
	    if(stileCercato == null)
	    	return false;
	    
	    prop = stileCercato.cssRules;
        
	    stile = prop[2].style;
	    stile.setProperty("background-image","url(data:image/jpeg;base64," + preferenzeObj.sfondo);    
}

//funzione per ottenere il colore primario pecent chiaro
function increase_brightness(hex, percent){
    // toglie # se c'è
	hex = hex.substring(1);

    var r = parseInt(hex.substr(0, 2), 16),
        g = parseInt(hex.substr(2, 2), 16),
        b = parseInt(hex.substr(4, 2), 16);

    return '#' +
       ((0|(1<<8) + r + (256 - r) * percent / 100).toString(16)).substr(1) +
       ((0|(1<<8) + g + (256 - g) * percent / 100).toString(16)).substr(1) +
       ((0|(1<<8) + b + (256 - b) * percent / 100).toString(16)).substr(1);
}
