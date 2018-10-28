function convertDateToLocalTime(dataGMTInMillisecondi){
        dataGMTInMillisecondi -= new Date().getTimezoneOffset() * 60000;
        var data = new Date(dataGMTInMillisecondi);

        var giorno = data.getDate();
        var mese = data.getMonth() + 1;
        switch(mese){
            case 1: mese = "Gen";
                break;
            case 2: mese = "Feb";
                break;
            case 3: mese = "Mar";
                break;
            case 4: mese = "Apr";
                break;
            case 5: mese = "Mag";
                break;
            case 6: mese = "Giu";
                break;
            case 7: mese = "Lug";
                break;
            case 8: mese = "Ago";
                break;
            case 9: mese = "Set";
                break;
            case 10: mese = "Ott";
                break;
            case 11: mese = "Nov";
                break;
            case 12: mese = "Dic";
                break;
        }
        
        var ora = data.getHours();
        var minuti = data.getMinutes();
        if(minuti <= 9)
        	minuti = "0" + minuti;

        var dataEora = new Object();
        dataEora.data = giorno + " " + mese;
        dataEora.ora = ora + ":" + minuti;
                
        return dataEora;
}
