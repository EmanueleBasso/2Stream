var corretto;

function error(isError,campo,error) {
	campo.siblings(".field").remove();
	if(!isError){
		return true;
	}
	else{
		campo.parent().append("<div class=\"field\"><span class=\"textError\">"+error+"</span></div>");
		return false;
	}
}

function checkNome(campo) {
	var regex=/^[a-zA-Z]+[0-9]*$/;
	return error(!regex.test(campo.val()),campo,"Username non valido");
}

function checkEmail(campo) {
	var regex=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	return error(!regex.test(campo.val()),campo,"Email non valida");
}

function checkPassword(campo) {
	var regex=/^\S*.{6}$/;
	return error(!regex.test(campo.val()),campo,"Minimo di 6 caratteri");
}
