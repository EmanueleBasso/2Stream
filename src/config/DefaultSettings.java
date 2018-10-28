package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import bean.Preferenza;
import bean.Utente;
import utility.Base64Utility;

@WebListener
public class DefaultSettings implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		InputStream sfondoChatInput;
		InputStream immagineProfiloInput;
		
		InputStream sfondoChatString;
		InputStream immagineProfiloString;
		
		File fileSfondo = new File(sce.getServletContext().getRealPath("/img") + "/logo.png");
		File fileImmagine = new File(sce.getServletContext().getRealPath("/img")+"/profile.jpg");

		try {
			sfondoChatInput = new FileInputStream(fileSfondo);
			sfondoChatString = new FileInputStream(fileSfondo);

			immagineProfiloInput = new FileInputStream(fileImmagine);
			immagineProfiloString = new FileInputStream(fileImmagine);

		} 
		catch (FileNotFoundException e) {
			throw new RuntimeException("Errore nel caricare l'immagine di sfondo/profilo di default");
		}
		
		setDefaultPreferenze(sfondoChatInput, sfondoChatString);
		setDefaultUtente(immagineProfiloInput, immagineProfiloString);
	}
	
	private void setDefaultUtente(InputStream immagineProfiloInput, InputStream immagineProfiloString){
		String stato="I'm using 2Stream!";
		
		String immagine;
		try {
			immagine = Base64Utility.encodeFileToBase64(immagineProfiloString);
		} catch (IOException e) {
			throw new RuntimeException("Errore nell'ottenere il base64 dell'immagine profilo");
		}
		
		Utente.setIMMAGINE_PROFILO_STREAM(immagineProfiloInput);
		Utente.setIMMAGINE_PROFILO_BASE64(immagine);
		Utente.setSTATO(stato);
	}
	
	private void setDefaultPreferenze(InputStream immagineSfondoInput, InputStream immagineSfondoString){
		String colore = "#7e1412";
		String font = "verdana";
		String lingua = "italiano";
		
		String immagine;
		try {
			immagine = Base64Utility.encodeFileToBase64(immagineSfondoString);
		} catch (IOException e) {
			throw new RuntimeException("Errore nell'ottenere il base64 dell'immagine di sfondo");
		}
		
		Preferenza.setCOLORE(colore);
		Preferenza.setFONT(font);
		Preferenza.setLINGUA(lingua);
		Preferenza.setIMMAGINE_SFONDO_STREAM(immagineSfondoInput);
		Preferenza.setIMMAGINE_SFONDO_STRING(immagine);
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//	VUOTO
	}
}
