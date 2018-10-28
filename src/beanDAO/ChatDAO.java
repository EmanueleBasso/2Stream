package beanDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import bean.Chat;
import connection.DriverManagerConnectionPool;
import utility.OraUtility;

public class ChatDAO {
	
	public static synchronized void doSave(Chat chat) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();

			PreparedStatement ps = con.prepareStatement(SALVA_CHAT);
	
			long dataCreazioneGMTMillis = OraUtility.convertToGMT(new GregorianCalendar());
	
			ps.setInt(1,chat.getIdChat());
			ps.setDate(2,new Date(dataCreazioneGMTMillis));
			ps.setTimestamp(3,new Timestamp(dataCreazioneGMTMillis));
	
			ps.executeUpdate();
	
			ps.close();
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}

	public static synchronized Chat doRetrieveByChatID(int idChat) throws SQLException{
		Connection con = null;
		Chat chat = null;

		try {
			con = DriverManagerConnectionPool.getConnection();		
			PreparedStatement ps = con.prepareStatement(CERCA_CHAT);
			ps.setInt(1, idChat);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				chat = new Chat();
				
				chat.setIdChat(idChat);
				
				Date dataLetta = rs.getDate("DataCreazione");
				GregorianCalendar dataCreazione = new GregorianCalendar();
				dataCreazione.setTime(dataLetta);
				chat.setDataCreazione(dataCreazione);
				
				chat.setUltimaModifica(rs.getTimestamp("UltimaModifica"));
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
	
	public static synchronized void doUpadateUltimaModifica(Chat chat) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(AGGIORNA_ULTIMA_MODIFICA);
			
			ps.setTimestamp(1, chat.getUltimaModifica());
			ps.setInt(2, chat.getIdChat());
			
			ps.executeUpdate();
	
			ps.close();
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized void doDelete(Chat chat) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(CANCELLA_CHAT);
			
			ps.setInt(1, chat.getIdChat());
			
			ps.executeUpdate();
	
			ps.close();
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	private static final String NOME_TABELLA = " 2Stream.Chat ";
	
	private static final String SALVA_CHAT = "INSERT INTO" + NOME_TABELLA 
										   + "VALUES (?,?,?)";
	
	private static final String CERCA_CHAT = "SELECT * "
										   + "FROM" + NOME_TABELLA
										   + "WHERE IDChat=?";
	
	private static final String AGGIORNA_ULTIMA_MODIFICA = "UPDATE" + NOME_TABELLA 
														 + "SET UltimaModifica=? "
														 + "WHERE IDChat=?";
	
	private static final String CANCELLA_CHAT = "DELETE FROM" + NOME_TABELLA 
											  + "WHERE IDChat=?";
}
