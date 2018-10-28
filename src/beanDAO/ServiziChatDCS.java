package beanDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import bean.Chat;
import bean.Utente;
import beanDAO.ChatDAO;
import connection.DriverManagerConnectionPool;

public class ServiziChatDCS {
	public static synchronized void nuovaConversazione(Utente utente1, Utente utente2) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			Chat chat = new Chat();
			chat.setNextIDChat();
			ChatDAO.doSave(chat);
			
			PreparedStatement ps1 = con.prepareStatement(NUOVA_RELAZIONE_UTILIZZARE_CHAT);
			
			ps1.setString(1, utente1.getUsername());
			ps1.setInt(2, chat.getIdChat());
			
			ps1.executeUpdate();
			
			ps1.close();
			
			PreparedStatement ps2 = con.prepareStatement(NUOVA_RELAZIONE_UTILIZZARE_CHAT);
			
			ps2.setString(1, utente2.getUsername());
			ps2.setInt(2, chat.getIdChat());
			
			ps2.executeUpdate();
			
			ps2.close();
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized Chat getChat(Utente utente1, Utente utente2) throws SQLException{
		Connection con = null;
		Chat chat = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps = con.prepareStatement(CERCA_CHAT_TRA_UTENTI);
			
			ps.setString(1, utente1.getUsername());
			ps.setString(2, utente2.getUsername());
	
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				chat = ChatDAO.doRetrieveByChatID(rs.getInt("Chat"));
				chat.setInterlocutore(utente2.getUsername());
			}
	
			rs.close();
			ps.close();
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
		
		return chat;
	}
	
	public static synchronized List<Chat> getChatsUtente(Utente utente) throws SQLException{
		Connection con = null;
		List<Chat> lista = new LinkedList<>();
		
		try {
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps = con.prepareStatement(CERCA_CHATS_UTENTE);
			
			ps.setString(1, utente.getUsername());
	
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				int idChat = rs.getInt("Chat");
				Timestamp dataUltimaModifica = rs.getTimestamp("UltimaModifica");
				String interlocutore = getInterlocutoreChat(idChat,utente.getUsername());
				
				Chat chat = new Chat();
				chat.setIdChat(idChat);
				chat.setUltimaModifica(dataUltimaModifica);
				chat.setInterlocutore(interlocutore);
						
				lista.add(chat);
			}
			
			rs.close();
			ps.close();			
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}		
		
		return lista;
	}
	
	private static synchronized String getInterlocutoreChat(int idChat, String utente)throws SQLException{
		Connection con = null;
		String interlocutore = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps = con.prepareStatement(CERCA_INTERLOCUTORE);
			
			ps.setInt(1, idChat);
			ps.setString(2, utente);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				interlocutore = rs.getString("Utente");
			}
			
			rs.close();
			ps.close();
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
		
		return interlocutore;
	}
	
	private final static String TABELLA_UTILIZZARE_CHAT = " 2Stream.UtilizzareChat ";
	
	private final static String NUOVA_RELAZIONE_UTILIZZARE_CHAT = "INSERT INTO" + TABELLA_UTILIZZARE_CHAT
																+ "VALUES (?,?)";
	
	private final static String CERCA_INTERLOCUTORE = "SELECT Utente "
													+ "FROM "+ TABELLA_UTILIZZARE_CHAT
													+ "WHERE Chat=? and Utente!=?";
	
	private final static String CERCA_CHATS_UTENTE = "SELECT * " 
												   + "FROM " + TABELLA_UTILIZZARE_CHAT
												   + "JOIN 2Stream.Chat ON Chat = IDChat "
												   + "WHERE Utente = ? " 
												   + "ORDER BY UltimaModifica DESC";
	
	private static final String CERCA_CHAT_TRA_UTENTI = "SELECT Chat "
													  + "FROM " + TABELLA_UTILIZZARE_CHAT
													  + "WHERE Utente=? AND Chat IN  (SELECT Chat "
											   		 							   + "FROM " +TABELLA_UTILIZZARE_CHAT
											   		 							   + "WHERE Utente=?)";						   		 							  
}