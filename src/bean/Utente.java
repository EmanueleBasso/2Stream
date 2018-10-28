package bean;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

public class Utente {
	private String username,password,email,stato;
	private GregorianCalendar dataCreazione;
	private Timestamp ultimoAccesso;
	private InputStream immagineProfilo;
	/*	Valore di default dell'immagine di profilo*/
	private static InputStream IMMAGINE_PROFILO_STREAM;
	private static String STATO, IMMAGINE_PROFILO_BASE64;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public GregorianCalendar getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(GregorianCalendar dateCreazione) {
		this.dataCreazione = dateCreazione;
	}
	public Timestamp getUltimoAccesso() {
		return ultimoAccesso;
	}
	public void setUltimoAccesso(Timestamp ultimoAccesso) {
		this.ultimoAccesso = ultimoAccesso;
	}
	public InputStream getImmagineProfilo() {
		return immagineProfilo;
	}
	public void setImmagineProfilo(InputStream immagineProfilo) {
		this.immagineProfilo = immagineProfilo;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public static String getSTATO() {
		return STATO;
	}
	public static void setSTATO(String stato) {
		STATO = stato;
	}
	public static InputStream getIMMAGINE_PROFILO_STREAM() {
		return IMMAGINE_PROFILO_STREAM;
	}
	public static void setIMMAGINE_PROFILO_STREAM(InputStream immagine) {
		IMMAGINE_PROFILO_STREAM = immagine;
	}
	public static String getIMMAGINE_PROFILO_BASE64() {
		return IMMAGINE_PROFILO_BASE64;
	}
	public static void setIMMAGINE_PROFILO_BASE64(String immagine) {
		IMMAGINE_PROFILO_BASE64 = immagine;
	}
	
}
