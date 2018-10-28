package beanDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import bean.Chat;
import bean.Messaggio;
import bean.MessaggioMultimediale;
import bean.MessaggioTestuale;
import connection.DriverManagerConnectionPool;

public class ServiziMessaggiDCS {

	public static synchronized void nuovoMessaggioTestuale(Chat chat, MessaggioTestuale messaggioTestuale) throws SQLException {	
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			messaggioTestuale.setNextIdMessaggio();
			MessaggioTestualeDAO.doSave(messaggioTestuale);
		
			PreparedStatement ps = con.prepareStatement(NUOVA_RELAZIONE_CONTENERE_MESSAGGIO_TESTUALE);
			
			ps.setInt(1,chat.getIdChat());
			ps.setInt(2,messaggioTestuale.getIdMessaggio());
			
			ps.executeUpdate();
			ps.close();
			
			chat.setUltimaModifica(messaggioTestuale.getDataInvio());
			ChatDAO.doUpadateUltimaModifica(chat);
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}

	public static synchronized void nuovoMessaggioMultimediale(Chat chat, MessaggioMultimediale messaggioMultimediale) throws SQLException {		
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			messaggioMultimediale.setNextIdMessaggio();
			MessaggioMultimedialeDAO.doSave(messaggioMultimediale);
		
			PreparedStatement ps = con.prepareStatement(NUOVA_RELAZIONE_CONTENERE_MESSAGGIO_MULTIMEDIALE);
			
			ps.setInt(1,chat.getIdChat());
			ps.setInt(2,messaggioMultimediale.getIdMessaggio());
			
			ps.executeUpdate();
			ps.close();
			
			chat.setUltimaModifica(messaggioMultimediale.getDataInvio());
			ChatDAO.doUpadateUltimaModifica(chat);
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized List<Messaggio> getMessaggiChat(int idChat) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps1 = con.prepareStatement(VISTA_MESSAGGI_TESTUALI);
			ps1.setInt(1, idChat);
			ps1.executeUpdate();
			ps1.close();
	
			PreparedStatement ps2 = con.prepareStatement(VISTA_MESSAGGI_MULTIMEDIALI);
			ps2.setInt(1, idChat);
			ps2.executeUpdate();
			ps2.close();
			
			PreparedStatement ps3 = con.prepareStatement(ULTIMI_MESSAGGI_TESTUALI_CHAT);
			ps3.executeUpdate();
			ps3.close();
			
			PreparedStatement ps4 = con.prepareStatement(ULTIMI_MESSAGGI_MULTIMEDIALI_CHAT);
			ps4.executeUpdate();
			ps4.close();
			
			PreparedStatement ps5 = con.prepareStatement(CERCA_MESSAGGI_TESTUALI_CHAT);
			ResultSet rsTestuali = ps5.executeQuery();
			
			PreparedStatement ps6 = con.prepareStatement(CERCA_MESSAGGI_MULTIMEDIALI_CHAT);
			ResultSet rsMultimediali = ps6.executeQuery();
			
			List<Messaggio> messaggi = aggiungiMessaggiAllaLista(rsTestuali,rsMultimediali,MESSAGGI_DA_CARICARE);				
			
			rsTestuali.close();
			ps3.close();
			rsMultimediali.close();
			ps4.close();
	
			PreparedStatement ps7 = con.prepareStatement(DROP_VIEW_MESSAGGI_TESTUALI_CHAT);
			ps7.executeUpdate();
			ps7.close();
	
			PreparedStatement ps8 = con.prepareStatement(DROP_VIEW_MESSAGGI_MULTIMEDIALI_CHAT);
			ps8.executeUpdate();
			ps8.close();
			
			PreparedStatement ps9 = con.prepareStatement(DROP_VIEW_ULTIMI_TESTUALI_CHAT);
			ps9.executeUpdate();
			ps9.close();
	
			PreparedStatement ps10 = con.prepareStatement(DROP_VIEW_ULTIMI_MULTIMEDIALI_CHAT);
			ps10.executeUpdate();
			ps10.close();
			
			return messaggi;
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized List<Messaggio> getMessaggiPrecedentiChat(int idChat, int ultimoTestuale, int ultimoMultimediale) throws SQLException{
		Connection con = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps1 = con.prepareStatement(VISTA_PRECEDENTI_TESTUALI);
			ps1.setInt(1, idChat);
			ps1.setInt(2, ultimoTestuale);
			ps1.executeUpdate();
			ps1.close();
	
			PreparedStatement ps2 = con.prepareStatement(VISTA_PRECEDENTI_MULTIMEDIALI);
			ps2.setInt(1, idChat);
			ps2.setInt(2, ultimoMultimediale);
			ps2.executeUpdate();
			ps2.close();
			
			PreparedStatement ps3 = con.prepareStatement(CERCA_PRECEDENTI_TESTUALI);
			ResultSet rsTestuali = ps3.executeQuery();
			
			PreparedStatement ps4 = con.prepareStatement(CERCA_PRECEDENTI_MULTIMEDIALI);
			ResultSet rsMultimediali = ps4.executeQuery();
			
			List<Messaggio> messaggi = aggiungiMessaggiAllaLista(rsTestuali,rsMultimediali,MESSAGGI_DA_CARICARE);		
			
			rsTestuali.close();
			ps3.close();
			
			rsMultimediali.close();
			ps4.close();
			
			PreparedStatement ps5 = con.prepareStatement(DROP_VIEW_PRECEDENTI_TESTUALI);
			ps5.executeUpdate();
			ps5.close();
	
			PreparedStatement ps6 = con.prepareStatement(DROP_VIEW_PRECEDENTI_MULTIMEDIALI);
			ps6.executeUpdate();
			ps6.close();
			
			return messaggi;
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}

	public static synchronized List<Messaggio> getMessaggiSuccessiviChat(int idChat, int ultimoTestuale, int ultimoMultimediale) throws SQLException{
		Connection con = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();				
	
			PreparedStatement ps1 = con.prepareStatement(VISTA_SUCCESSIVI_TESTUALI);
			ps1.setInt(1, idChat);
			ps1.setInt(2, ultimoTestuale);
			ps1.executeUpdate();
			ps1.close();
	
			PreparedStatement ps2 = con.prepareStatement(VISTA_SUCCESSIVI_MULTIMEDIALI);
			ps2.setInt(1, idChat);
			ps2.setInt(2, ultimoMultimediale);
			ps2.executeUpdate();
			ps2.close();
			
			PreparedStatement ps3 = con.prepareStatement(CERCA_SUCCESSIVI_TESTUALI);
			ResultSet rsTestuali = ps3.executeQuery();
			
			PreparedStatement ps4 = con.prepareStatement(CERCA_SUCCESSIVI_MULTIMEDIALI);
			ResultSet rsMultimediali = ps4.executeQuery();
			
			List<Messaggio> messaggi = aggiungiMessaggiAllaLista(rsTestuali,rsMultimediali,Integer.MAX_VALUE);		
			
			rsTestuali.close();
			ps3.close();
			
			rsMultimediali.close();
			ps4.close();
			
			PreparedStatement ps5 = con.prepareStatement(DROP_VIEW_SUCCESSIVI_TESTUALI);
			ps5.executeUpdate();
			ps5.close();
	
			PreparedStatement ps6 = con.prepareStatement(DROP_VIEW_SUCCESSIVI_MULTIMEDIALI);
			ps6.executeUpdate();
			ps6.close();
			
			return messaggi;
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	private static MessaggioTestuale createMessaggioTestuale(ResultSet riga) throws SQLException{
		MessaggioTestuale messaggioTestuale = new MessaggioTestuale();
		
		messaggioTestuale.setIdMessaggio(riga.getInt("IDMessaggioTestuale"));
		messaggioTestuale.setContenuto(riga.getString("Testo"));
		messaggioTestuale.setMittente(riga.getString("Mittente"));
		messaggioTestuale.setDataInvio(riga.getTimestamp("DataInvio"));
		
		return messaggioTestuale;
	}
	
	private static MessaggioMultimediale createMessaggioMultimediale(ResultSet riga) throws SQLException{
		MessaggioMultimediale messaggioMultimediale = new MessaggioMultimediale();
		
		messaggioMultimediale.setIdMessaggio(riga.getInt("IDMessaggioMultimediale"));
		messaggioMultimediale.setContenuto(riga.getBinaryStream("Contenuto"));
		messaggioMultimediale.setMittente(riga.getString("Mittente"));
		messaggioMultimediale.setDataInvio(riga.getTimestamp("DataInvio"));
		
		return messaggioMultimediale;
	}
	
	private static List<Messaggio> aggiungiMessaggiAllaLista(ResultSet rsTestuali,ResultSet rsMultimediali,int messaggiDaCaricare) throws SQLException{
		List<Messaggio> messaggi = new LinkedList<>();
		int contatore = 0;
		boolean testualePresente = rsTestuali.next();
		boolean multimedialePresente = rsMultimediali.next();
		
		while((testualePresente || multimedialePresente) && contatore < messaggiDaCaricare) {
			if(!multimedialePresente){
				messaggi.add(createMessaggioTestuale(rsTestuali));			
				testualePresente = rsTestuali.next();
			}else if(!testualePresente){
				messaggi.add(createMessaggioMultimediale(rsMultimediali));				
				multimedialePresente = rsMultimediali.next();
			}else{
				if(rsTestuali.getTimestamp("DataInvio").before(rsMultimediali.getTimestamp("DataInvio"))) {
					messaggi.add(createMessaggioTestuale(rsTestuali));					
					testualePresente = rsTestuali.next();
				}else{
					messaggi.add(createMessaggioMultimediale(rsMultimediali));				
					multimedialePresente = rsMultimediali.next();
				}
			}

			contatore++;
		}
		
		return messaggi;
	}

	private final static int MESSAGGI_DA_CARICARE = 20;
	
	private final static String TABELLA_CONTENERE_CHAT_TESTUALE = " 2Stream.ContenereChatTestuale ";
	
	private final static String TABELLA_CONTENERE_CHAT_MULTIMEDIALE = " 2Stream.ContenereChatMultimediale ";
	
	private final static String NUOVA_RELAZIONE_CONTENERE_MESSAGGIO_TESTUALE = "INSERT INTO "
																			 + TABELLA_CONTENERE_CHAT_TESTUALE
																			 + "VALUES (?,?)";

	private final static String NUOVA_RELAZIONE_CONTENERE_MESSAGGIO_MULTIMEDIALE = "INSERT INTO "
																				 + TABELLA_CONTENERE_CHAT_MULTIMEDIALE
																				 + "VALUES (?,?)";
	
	private final static String VISTA_MESSAGGI_TESTUALI = "CREATE VIEW 2Stream.MessaggiTestualiChat AS "  
														+ "SELECT * " 
														+ "FROM " + TABELLA_CONTENERE_CHAT_TESTUALE + " JOIN 2Stream.MessaggioTestuale " 
														+ "ON MessaggioTestuale=IDMessaggioTestuale " 
														+ "WHERE Chat=?";
		
	private final static String VISTA_MESSAGGI_MULTIMEDIALI = "CREATE VIEW 2Stream.MessaggiMultimedialiChat AS " 
															+ "SELECT * " 
															+ "FROM " + TABELLA_CONTENERE_CHAT_MULTIMEDIALE + " JOIN 2Stream.MessaggioMultimediale " 
															+ "ON MessaggioMultimediale=IDMessaggioMultimediale " 
															+ "WHERE Chat=?";

	private final static String ULTIMI_MESSAGGI_TESTUALI_CHAT = "CREATE VIEW 2Stream.UltimiMessaggiTestualiChat AS "
															  + "SELECT * " 
															  + "FROM 2Stream.MessaggiTestualiChat " 
															  + "ORDER BY DataInvio DESC "
															  + "LIMIT 20 ";

	private final static String ULTIMI_MESSAGGI_MULTIMEDIALI_CHAT = "CREATE VIEW 2Stream.UltimiMessaggiMultimedialiChat AS "
																  + "SELECT * "
																  + "FROM 2Stream.MessaggiMultimedialiChat "
																  + "ORDER BY DataInvio DESC "
																  + "LIMIT 20 ";
	
	private final static String CERCA_MESSAGGI_TESTUALI_CHAT = "SELECT * "
															 + "FROM 2Stream.UltimiMessaggiTestualiChat "
															 + "ORDER BY DataInvio ASC";
	
	private final static String CERCA_MESSAGGI_MULTIMEDIALI_CHAT = "SELECT * "
																 + "FROM 2Stream.UltimiMessaggiMultimedialiChat "
																 + "ORDER BY DataInvio ASC";

	private final static String DROP_VIEW_MESSAGGI_TESTUALI_CHAT = "DROP VIEW 2Stream.MessaggiTestualiChat";
	
	private final static String DROP_VIEW_MESSAGGI_MULTIMEDIALI_CHAT = "DROP VIEW 2Stream.MessaggiMultimedialiChat";
	
	private final static String DROP_VIEW_ULTIMI_TESTUALI_CHAT = "DROP VIEW 2Stream.UltimiMessaggiTestualiChat";

	private final static String DROP_VIEW_ULTIMI_MULTIMEDIALI_CHAT = "DROP VIEW 2Stream.UltimiMessaggiMultimedialiChat";

	
	private final static String VISTA_PRECEDENTI_TESTUALI = "CREATE VIEW 2Stream.MessaggiTestualiChatPrecedenti AS " 
														  + "SELECT * " 
														  + "FROM 2Stream.ContenereChatTestuale " 
														  + "JOIN " 
														  + "2Stream.MessaggioTestuale ON MessaggioTestuale = IDMessaggioTestuale " 
														  + "WHERE Chat = ? AND MessaggioTestuale < ?";

	private final static String VISTA_PRECEDENTI_MULTIMEDIALI = "CREATE VIEW 2Stream.MessaggiMultimedialiChatPrecedenti AS " 
					  										  + "SELECT * " 
					  										  + "FROM 2Stream.ContenereChatMultimediale " 
					  										  + "JOIN " 
					  										  + "2Stream.MessaggioMultimediale ON MessaggioMultimediale = IDMessaggioMultimediale " 
					  										  + "WHERE Chat = ? AND MessaggioMultimediale < ?";
	
	private final static String CERCA_PRECEDENTI_TESTUALI = "SELECT * " 
				  										  + "FROM 2Stream.MessaggiTestualiChatPrecedenti " 
				  										  + "ORDER BY DataInvio " 
				  										  + "LIMIT 0 , 20";
	
	private final static String CERCA_PRECEDENTI_MULTIMEDIALI = "SELECT * " 
															  + "FROM 2Stream.MessaggiMultimedialiChatPrecedenti " 
															  + "ORDER BY DataInvio " 
															  + "LIMIT 0 , 20";
	
	private final static String DROP_VIEW_PRECEDENTI_TESTUALI = "DROP VIEW 2Stream.MessaggiTestualiChatPrecedenti";
	
	private final static String DROP_VIEW_PRECEDENTI_MULTIMEDIALI = "DROP VIEW 2Stream.MessaggiMultimedialiChatPrecedenti";
	
	private final static String VISTA_SUCCESSIVI_TESTUALI = "CREATE VIEW 2Stream.MessaggiTestualiChatSuccessivi AS " 
														  + "SELECT * " 
														  + "FROM 2Stream.ContenereChatTestuale " 
														  + "JOIN " 
														  + "2Stream.MessaggioTestuale ON MessaggioTestuale = IDMessaggioTestuale " 
														  + "WHERE Chat = ? AND MessaggioTestuale > ?";
	
	private final static String VISTA_SUCCESSIVI_MULTIMEDIALI = "CREATE VIEW 2Stream.MessaggiMultimedialiChatSuccessivi AS " 
															  + "SELECT * " 
															  + "FROM 2Stream.ContenereChatMultimediale " 
															  + "JOIN " 
															  + "2Stream.MessaggioMultimediale ON MessaggioMultimediale = IDMessaggioMultimediale " 
															  + "WHERE Chat = ? AND MessaggioMultimediale > ?";
	
	private final static String CERCA_SUCCESSIVI_TESTUALI = "SELECT * " 
														  + "FROM 2Stream.MessaggiTestualiChatSuccessivi " 
														  + "ORDER BY DataInvio"; 
	
	private final static String CERCA_SUCCESSIVI_MULTIMEDIALI = "SELECT * " 
					  										  + "FROM 2Stream.MessaggiMultimedialiChatSuccessivi " 
					  										  + "ORDER BY DataInvio";
	
	private final static String DROP_VIEW_SUCCESSIVI_TESTUALI = "DROP VIEW 2Stream.MessaggiTestualiChatSuccessivi";
	
	private final static String DROP_VIEW_SUCCESSIVI_MULTIMEDIALI = "DROP VIEW 2Stream.MessaggiMultimedialiChatSuccessivi";
}
