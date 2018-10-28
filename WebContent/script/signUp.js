function checkDatabase(campo) {
	var nameExists = newAjax();
    if(nameExists!=false){
            nameExists.onreadystatechange = function(){
                    if(nameExists.readyState==4 && nameExists.status==200)
                    		corretto = error(JSON.parse(nameExists.responseText).errore==0,campo,"Username già scelto");
            }
            nameExists.open("GET", "UserExists?username="+campo.val(), false);
            nameExists.send(null);
    }
    else
		redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
}

function check() {
	var input = $(".fcampo");

	corretto = checkNome(input.eq(0));
	corretto &= checkEmail(input.eq(1));
	corretto &= checkPassword(input.eq(2));
	
	if(corretto)
		corretto = error(!input.eq(3).prop("checked"),input.eq(3),"Campo obbligatorio");
	
	if(corretto)
		checkDatabase(input.eq(0));
	
	if(corretto){
		$("#firstScreen").css("display","none");
		$("#secondScreen").css("display","block");
		$("#freccia").css("display","block");
	}
}

function goBack() {
	$(".titleSign").html("Registrati");
	$("#secondScreen").css("display","none");
	$("#termAndCondDisplay").css("display","none");
	$("#freccia").css("display","none");
	$("#firstScreen").css("display","block");
}

function changePhoto(input) {
    if (input[0].files && input[0].files[0]) {
    	var fReader = new FileReader();
    	fReader.readAsDataURL(input[0].files[0]);

    	fReader.onloadend = function(event){
            $('#image').attr("src", event.target.result);
    	}
    }
}

function showTerms() {
	$(".titleSign").html("Termini e Informative");
	$("#firstScreen").css("display","none");
	$("#termAndCondDisplay").css("display","block");
	$("#freccia").css("display","block");
}

$(document).keypress(
	    function(event){
	    	if (event.which == '13') {
		        event.preventDefault();
		        if($("#termAndCondDisplay").css("display") == "none"){
			        if($("#firstScreen").css("display") != "none")
			        	$("#nextFields").click();
			        else
			        	$("#submit").click();
		        }
	    	}
	});
