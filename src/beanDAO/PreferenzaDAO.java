package beanDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Preferenza;
import connection.DriverManagerConnectionPool;

public class PreferenzaDAO {
	public static synchronized void doSave(Preferenza preferenza) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(SALVA_PREFERENZA);
			
			ps.setString(1,preferenza.getUsernameUtente());
			ps.setString(2,preferenza.getColoreTema());				
			ps.setBoolean(3,preferenza.isPrivacyAccesso());
			ps.setBoolean(4,preferenza.isPrivacyImmagine());
			ps.setBinaryStream(5,preferenza.getImmagineSfondo());
			ps.setString(6,preferenza.getLingua());
			ps.setString(7,preferenza.getFont());
			
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
	
	public static synchronized void doUpdateColoreTema(Preferenza preferenza) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(AGGIORNA_COLORE_TEMA);
			
			ps.setString(1,preferenza.getColoreTema());					
			ps.setString(2,preferenza.getUsernameUtente());
			
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
	
	public static synchronized void doUpdatePrivacyAccesso(Preferenza preferenza) throws SQLException{
		Connection con = null;

		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(AGGIORNA_PRIVACY_ACCESSO);
			
			ps.setBoolean(1,preferenza.isPrivacyAccesso());	
			ps.setString(2,preferenza.getUsernameUtente());
			
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
	
	public static synchronized void doUpdatePrivacyImmagine(Preferenza preferenza) throws SQLException{
		Connection con = null;
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(AGGIORNA_PRIVACY_IMMAGINE);
			
			ps.setBoolean(1,preferenza.isPrivacyImmagine());
			ps.setString(2,preferenza.getUsernameUtente());
			
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
	
	public static synchronized void doUpdateImmagineSfondo(Preferenza preferenza) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(AGGIORNA_IMMAGINE_SFONDO);
			
			ps.setBinaryStream(1,preferenza.getImmagineSfondo());
			ps.setString(2,preferenza.getUsernameUtente());
			
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
	
	public static synchronized void doUpdateLingua(Preferenza preferenza) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(AGGIORNA_LINGUA);
			
			ps.setString(1,preferenza.getLingua());	
			ps.setString(2,preferenza.getUsernameUtente());
			
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
	
	public static synchronized void doUpdateFont(Preferenza preferenza) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(AGGIORNA_FONT);
			
			ps.setString(1,preferenza.getFont());
			ps.setString(2,preferenza.getUsernameUtente());
			
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
	
	public static synchronized void doDelete(Preferenza preferenza) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(CANCELLA_PREFERENZA);
			
			ps.setString(1,preferenza.getUsernameUtente());
			
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
	
	public static synchronized Preferenza doRetrieveByUsername(String username) throws SQLException{
		Connection con = null;
		Preferenza preferenza = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(CERCA_PREFERENZA);
			
			ps.setString(1,username);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				preferenza = new Preferenza();
				
				preferenza.setUsernameUtente(rs.getString("Utente"));
				preferenza.setColoreTema(rs.getString("ColoreTema"));			
				preferenza.setPrivacyAccesso(rs.getBoolean("PrivacyAccesso"));
				preferenza.setPrivacyImmagine(rs.getBoolean("PrivacyImmagine"));
				preferenza.setImmagineSfondo(rs.getBinaryStream("ImmagineSfondo"));
				preferenza.setLingua(rs.getString("Lingua"));
				preferenza.setFont(rs.getString("Font"));
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
		
		return preferenza;
	}
	
	private static final String NOME_TABELLA = " 2Stream.Preferenza ";
	
	private static final String SALVA_PREFERENZA = "INSERT INTO" + NOME_TABELLA
												 + "VALUES (?,?,?,?,?,?,?)";

	private static final String AGGIORNA_COLORE_TEMA = "UPDATE" + NOME_TABELLA
													 + "SET ColoreTema=? "
													 + "WHERE Utente=?";
														   
	private static final String AGGIORNA_PRIVACY_ACCESSO = "UPDATE" + NOME_TABELLA
														 + "SET PrivacyAccesso=? "
														 + "WHERE Utente=?";											   
													   
	private static final String AGGIORNA_PRIVACY_IMMAGINE = "UPDATE" + NOME_TABELLA
														  + "SET PrivacyImmagine=? "
														  + "WHERE Utente=?";													   
													   
	private static final String AGGIORNA_IMMAGINE_SFONDO = "UPDATE" + NOME_TABELLA
														 + "SET ImmagineSfondo=? "
														 + "WHERE Utente=?";												   
	
	private static final String AGGIORNA_LINGUA = "UPDATE" + NOME_TABELLA
												+ "SET Lingua=? "
												+ "WHERE Utente=?";
	
	private static final String AGGIORNA_FONT = "UPDATE" + NOME_TABELLA
											  + "SET Font=? "
											  + "WHERE Utente=?";
	
	private static final String CANCELLA_PREFERENZA = "DELETE FROM" + NOME_TABELLA
													+ "WHERE Utente=?";
	
	private static final String CERCA_PREFERENZA = "SELECT * "
												 + "FROM" + NOME_TABELLA
												 + "WHERE Utente=?";
}
