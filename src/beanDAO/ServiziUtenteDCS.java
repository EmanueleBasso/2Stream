package beanDAO;

import java.sql.SQLException;

import bean.Preferenza;
import bean.Utente;

public class ServiziUtenteDCS {

	public static synchronized void creaUtente(Utente utente) throws SQLException{	
		Preferenza preferenza = creaPreferenza(utente.getUsername());
		
		UtenteDAO.doSave(utente);
		PreferenzaDAO.doSave(preferenza);
	}
	
	private static Preferenza creaPreferenza(String username){
		Preferenza preferenza = new Preferenza();
		
		preferenza.setColoreTema(Preferenza.getCOLORE());
		preferenza.setFont(Preferenza.getFONT());
		preferenza.setLingua(Preferenza.getLINGUA());
		preferenza.setUsernameUtente(username);
		preferenza.setPrivacyAccesso(false);
		preferenza.setPrivacyImmagine(false);
		preferenza.setImmagineSfondo(Preferenza.getIMMAGINE_SFONDO_STREAM());
		
		return preferenza;
	}

}
