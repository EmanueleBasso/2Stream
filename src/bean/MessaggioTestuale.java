package bean;

public class MessaggioTestuale extends Messaggio {
	private String contenuto;
	private static int progressivo;
	
	public String getContenuto() {
		return contenuto;
	}
	
	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}
	
	public void setNextIdMessaggio() { 
	    this.idMessaggio = ++progressivo; 
	} 
	   
	public static void setProgressivo(int progressivoDB) { 
		progressivo = progressivoDB; 
	}
	
}
