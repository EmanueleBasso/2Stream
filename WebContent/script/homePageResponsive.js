$(window).on('resize', function() {
    	if (window.matchMedia("(min-width: 481px)").matches) {
    		$("#right").css("display","block");
    		$("#left").css("display","block"); 
    		$("#rightHeader").css("display","flex");
    		$("#leftHeader").css("display","block");
    	}
    	else
    		showLeft();	
});

function showRight(){
	if (!window.matchMedia("(min-width: 481px)").matches) {
		$("#right").css("display","block");
		$("#left").css("display","none");	
		$(".settingsIco").css("display","none");
		$("#freccia").css("display","block");
		$("#leftHeader").css("display","none");
		$("#rightHeader").css("display","flex");
	}
}

function showLeft(){
	$("#right").css("display","none");
	$("#left").css("display","block");
	$(".settingsIco").css("display","block");
	$("#freccia").css("display","none");
	$("#leftHeader").css("display","block");
	$("#rightHeader").css("display","none");
}