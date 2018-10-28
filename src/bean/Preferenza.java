package bean;

import java.io.InputStream;

public class Preferenza {
	private String usernameUtente,lingua,font,coloreTema;
	private boolean privacyAccesso,privacyImmagine;
	private InputStream immagineSfondo;
	/*	Valori di default	*/
	private static String COLORE;
	private static String FONT;
	private static String LINGUA;
	private static InputStream IMMAGINE_SFONDO_STREAM;
	private static String IMMAGINE_SFONDO_STRING;
	
	public String getUsernameUtente() {
		return usernameUtente;
	}
	public String getLingua() {
		return lingua;
	}
	public String getFont() {
		return font;
	}
	public String getColoreTema() {
		return coloreTema;
	}
	public boolean isPrivacyAccesso() {
		return privacyAccesso;
	}
	public boolean isPrivacyImmagine() {
		return privacyImmagine;
	}
	public InputStream getImmagineSfondo() {
		return immagineSfondo;
	}
	public void setUsernameUtente(String usernameUtente) {
		this.usernameUtente = usernameUtente;
	}
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public void setColoreTema(String coloreTema) {
		this.coloreTema = coloreTema;
	}
	public void setPrivacyAccesso(boolean privacyAccesso) {
		this.privacyAccesso = privacyAccesso;
	}
	public void setPrivacyImmagine(boolean privacyImmagine) {
		this.privacyImmagine = privacyImmagine;
	}
	public void setImmagineSfondo(InputStream immagineSfondo) {
		this.immagineSfondo = immagineSfondo;
	}

	public static String getCOLORE() {
		return COLORE;
	}
	public static String getFONT() {
		return FONT;
	}
	public static String getLINGUA() {
		return LINGUA;
	}
	public static InputStream getIMMAGINE_SFONDO_STREAM() {
		return IMMAGINE_SFONDO_STREAM;
	}
	public static String getIMMAGINE_SFONDO_STRING() {
		return IMMAGINE_SFONDO_STRING;
	}

	public static void setCOLORE(String colore) {
		COLORE = colore;
	}
	public static void setFONT(String font) {
		FONT = font;
	}
	public static void setLINGUA(String lingua) {
		LINGUA = lingua;
	}
	public static void setIMMAGINE_SFONDO_STREAM(InputStream immagine) {
		IMMAGINE_SFONDO_STREAM = immagine;
	}
	public static void setIMMAGINE_SFONDO_STRING(String immagine) {
		IMMAGINE_SFONDO_STRING = immagine;
	}

}
