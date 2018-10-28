package bean;

import java.sql.Timestamp;

public abstract class Messaggio {
	int idMessaggio;
	private String mittente;
	private Timestamp dataInvio;

	public int getIdMessaggio() {
		return idMessaggio;
	}
	
	public void setIdMessaggio(int idMessaggio) {
		this.idMessaggio = idMessaggio;
	}
	
	public String getMittente() {
		return mittente;
	}
	
	public void setMittente(String mittente) {
		this.mittente = mittente;
	}
	
	public Timestamp getDataInvio() {
		return dataInvio;
	}
	
	public void setDataInvio(Timestamp dataInvio) {
		this.dataInvio = dataInvio;
	}

}
