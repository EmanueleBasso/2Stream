package bean;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

public class Chat {
	private GregorianCalendar dataCreazione;
	private Timestamp ultimaModifica;
	private int idChat;
	private String interlocutore;
	private static int progressivo; 
	
	public GregorianCalendar getDataCreazione() {
		return dataCreazione;
	}
	
	public void setDataCreazione(GregorianCalendar dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
	public Timestamp getUltimaModifica() {
		return ultimaModifica;
	}
	
	public void setUltimaModifica(Timestamp ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}
	
	public int getIdChat() {
		return idChat;
	}
	
	public void setIdChat(int idChat) {
		this.idChat = idChat;
	}

	public void setNextIDChat() {
		this.idChat = ++progressivo;
	}
	
	
	public String getInterlocutore() {
		return interlocutore;
	}
	
	public void setInterlocutore(String interlocutore) {
		this.interlocutore = interlocutore;
	}
	
	public static void setProgressivo(int progressivoDB) {
		progressivo = progressivoDB;
	}
}
