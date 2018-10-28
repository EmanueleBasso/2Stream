function check(){
	var input = $(".fcampo");

	corretto = checkNome(input.eq(0));
	corretto &= checkPassword(input.eq(1));
		
	if(corretto)
		return true;
	else 
		return false;
}
