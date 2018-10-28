package bean;

import java.io.InputStream;

public class MessaggioMultimediale extends Messaggio {
	private InputStream contenuto;
	private static int progressivo;

	public InputStream getContenuto() {
		return contenuto;
	}
	
	public void setContenuto(InputStream contenuto) {
		this.contenuto = contenuto;
	}
		
	public void setNextIdMessaggio() {
		this.idMessaggio = ++progressivo;
	}
	
	public static void setProgressivo(int progressivoDB) {
		progressivo = progressivoDB;
	}
}
