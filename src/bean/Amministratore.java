package bean;

import java.util.GregorianCalendar;

public class Amministratore {
	private String username,password;
	private GregorianCalendar dataCreazione;
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public GregorianCalendar getDataCreazione() {
		return dataCreazione;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDataCreazione(GregorianCalendar dataCreazione) {
		this.dataCreazione = dataCreazione;
	}	
	
}
