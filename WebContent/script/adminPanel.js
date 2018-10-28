var graficoTorta = null;
var graficoUtenti = null;
var graficoConnessioni = null;

function changeTab(button,tab){
    $("#list a").removeClass("buttonActive");
    button.toggleClass("buttonActive");

    $(".tab").removeClass("tabActive");
    if(tab == 1){
        $("#database").toggleClass("tabActive");
        graficoUtilizzoDatabase();
    }
    else if(tab == 2){
        $("#prestazioni").toggleClass("tabActive");
        graficoUtentiOnline();
        graficoConnessioniAttive();
    }
    else
        $("#settings").toggleClass("tabActive");
}

$(document).ready(graficoUtilizzoDatabase);
$(document).ready(graficoUtentiOnline);
$(document).ready(graficoConnessioniAttive);

function graficoUtilizzoDatabase(){
    var contesto = $("#tortaCanvas")[0].getContext("2d");

    var occupazione = $("#occupazione").val();
    var quota = $("#quota").val();
    
    if(graficoTorta != null)
        graficoTorta.destroy();

    var data = {
        type: 'pie',
        data: {
            datasets: [{
                data: [
                    occupazione,
                    quota
                ],
                backgroundColor: [
                    "#cc0000",
                    "#00b300"
                ],
            }],
            labels: [
                "Usato",
                "Disponibile"
            ]
        },
        options: {
            responsive: true,
            legend: {
                position: "bottom",
            },
            title: {
                display: true,
                text: "Utilizzo Database GB"
            }
        }
    };

    graficoTorta = new Chart(contesto,data);
}

function graficoUtentiOnline(){
    var contesto = $("#utentiCanvas")[0].getContext("2d");

    var online = $("#online").val();
    var maxOnline = $("#maxOnline").val();
    
    if(graficoUtenti != null)
        graficoUtenti.destroy();

    var data = {
        type: 'bar',
        data: {
            datasets: [{
                backgroundColor: "#9932cc",
                data: [online,maxOnline]
            }],
        },
        options: {
            maintainAspectRatio: false,
            responsive: true,
            legend: {
                display: false,
            },
            title: {
                display: true,
                text: "Utenti online"
            },
            scales: {
                xAxes: [{
                    barPercentage: 0.3
                }],
                yAxes: [{
                    stacked: true
                }]
            }
        }
    };

    graficoUtenti = new Chart(contesto,data);
}

function graficoConnessioniAttive(){
    var contesto = $("#connessioniCanvas")[0].getContext("2d");

    var connessioni = $("#connessioni").val();
    var maxConnessioni = $("#maxConnessioni").val();
    
    if(graficoConnessioni != null)
        graficoConnessioni.destroy();

    var data = {
        type: 'bar',
        data: {
            datasets: [{
                backgroundColor: "#669900",
                data: [connessioni,maxConnessioni]
            }],
        },
        options: {
            maintainAspectRatio: false,
            responsive: true,
            legend: {
                display: false,
            },
            title: {
                display: true,
                text: "Connessioni attive"
            },
            scales: {
                xAxes: [{
                    barPercentage: 0.3
                }],
                yAxes: [{
                    stacked: true
                }]
            }
        }
    };

    graficoConnessioni = new Chart(contesto,data);
}

function sendPassword() {
    var campoPassword = $('#password');
    if(!checkPassword(campoPassword))
        return;

    var passwordAjax = newAjax();

    if(passwordAjax != null){
        var password = campoPassword.val();
        
        passwordAjax.onreadystatechange = function(){
            if(passwordAjax.readyState==4 && passwordAjax.status==200){
                var jsonObject = JSON.parse(passwordAjax.responseText);

                if(jsonObject.password == "nuova")
                    error(true,campoPassword,"Password modificata");
                else
                    error(true,campoPassword,"Password non modificata");
            }
        };
        passwordAjax.open("post","SetAdminPassword");
        passwordAjax.setRequestHeader("Content-type","application/x-www-form-urlencoded"); 
        passwordAjax.send("password="+password);
    }else
        redirect("ErrorPage.jsp","?ErrorCode=Ajax non Supportato");
}
