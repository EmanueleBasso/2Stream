function newAjax() {
    var xmlHttp;
    try
    {
        xmlHttp=new XMLHttpRequest();
    }
    catch (exc)
    {
        try
        {
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        catch (exc)
        {
            try
            {
                xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
            }
            catch (exc)
            {
                return false;
            }
        }
    }
    return xmlHttp;
}

function redirect(pagina,queryString) {
	var path = window.location.pathname.substring(1);
	var subPath = path.substring(0,path.lastIndexOf("\/"));
	
	window.location.replace( "\/" + subPath + "\/" + pagina + queryString);
}
