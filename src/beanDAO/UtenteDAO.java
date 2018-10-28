package beanDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import bean.Utente;
import connection.DriverManagerConnectionPool;
import utility.OraUtility;

public class UtenteDAO {
	public static synchronized void doSave(Utente utente) throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps = con.prepareStatement(SALVA_UTENTE);
	
			long dataCreazioneGMTMillis = OraUtility.convertToGMT(new GregorianCalendar());
	
			ps.setString(1,utente.getUsername());
			ps.setString(2,utente.getPassword());
			ps.setString(3,utente.getEmail());
			ps.setDate(4,new Date(dataCreazioneGMTMillis));
			ps.setTimestamp(5,new Timestamp(dataCreazioneGMTMillis));
			ps.setBinaryStream(6, utente.getImmagineProfilo());
			ps.setString(7,utente.getStato());
	
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

	public static synchronized void doUpadatePassword(Utente utente) throws SQLException{
		Connection con = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps = con.prepareStatement(AGGIORNA_UTENTE_PASSWORD);
	
			ps.setString(1,utente.getPassword());
			ps.setString(2,utente.getUsername());
	
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

	public static synchronized void doUpadateUltimoAccesso(Utente utente) throws SQLException{
		Connection con = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps = con.prepareStatement(AGGIORNA_UTENTE_ULTIMO_ACCESSO);
	
			ps.setTimestamp(1,utente.getUltimoAccesso());
			ps.setString(2,utente.getUsername());
	
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

	public static synchronized void doUpadateImmagineProfilo(Utente utente) throws SQLException{
		Connection con = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();
		
			PreparedStatement ps = con.prepareStatement(AGGIORNA_UTENTE_IMMAGINE_PROFILO);
	
			ps.setBinaryStream(1,utente.getImmagineProfilo());
			ps.setString(2,utente.getUsername());
	
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

	public static synchronized void doUpadateStato(Utente utente) throws SQLException{
		Connection con = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();

			PreparedStatement ps = con.prepareStatement(AGGIORNA_UTENTE_STATO);
	
			ps.setString(1,utente.getStato());
			ps.setString(2,utente.getUsername());
	
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

	public static synchronized void doDelete(Utente utente) throws SQLException{
		Connection con = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();

			PreparedStatement ps = con.prepareStatement(CANCELLA_UTENTE);
	
			ps.setString(1,utente.getUsername());
	
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

	public static synchronized boolean exists(String username) throws SQLException{
		Connection con = null;
		boolean esiste = false;
		
		try{
			con = DriverManagerConnectionPool.getConnection();

			PreparedStatement ps = con.prepareStatement(CERCA_USERNAME);
	
			ps.setString(1,username);
	
			ResultSet rs = ps.executeQuery();
	
			if(rs.next())
				esiste = true;
	
			rs.close();
			ps.close();
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}

		return esiste;
	}

	public static synchronized Utente doRetrieveByUsernamePassword(String username,String password) throws SQLException{
		Connection con = null;
		Utente utente = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();

			PreparedStatement ps = con.prepareStatement(CERCA_UTENTE);
	
			ps.setString(1,username);
			ps.setString(2,password);
	
			ResultSet rs = ps.executeQuery();
	
			if(rs.next()){
				utente = new Utente();
	
				utente.setUsername(rs.getString("Username"));
				utente.setPassword(rs.getString("Password"));
				utente.setEmail(rs.getString("Email"));
	
				Date dataLetta = rs.getDate("DataCreazione");
				GregorianCalendar dataCreazione = new GregorianCalendar();
				dataCreazione.setTime(dataLetta);
				utente.setDataCreazione(dataCreazione);
	
				utente.setUltimoAccesso(rs.getTimestamp("UltimoAccesso"));
				utente.setImmagineProfilo(rs.getBinaryStream("ImmagineProfilo"));
				utente.setStato(rs.getString("Stato"));
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

		return utente;
	}

	public static synchronized Utente doRetrieveByUsername(String username) throws SQLException{
		Connection con = null;
		Utente utente = null;
		
		try{
			con = DriverManagerConnectionPool.getConnection();

			PreparedStatement ps = con.prepareStatement(CERCA_USERNAME);
	
			ps.setString(1,username);
	
			ResultSet rs = ps.executeQuery();
	
			if(rs.next()){
				utente = new Utente();
	
				utente.setUsername(rs.getString("Username"));
				utente.setPassword(rs.getString("Password"));
				utente.setEmail(rs.getString("Email"));
	
				Date dataLetta = rs.getDate("DataCreazione");
				GregorianCalendar dataCreazione = new GregorianCalendar();
				dataCreazione.setTime(dataLetta);
				utente.setDataCreazione(dataCreazione);
	
				utente.setUltimoAccesso(rs.getTimestamp("UltimoAccesso"));
				utente.setImmagineProfilo(rs.getBinaryStream("ImmagineProfilo"));
				utente.setStato(rs.getString("Stato"));
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

		return utente;
	}
	
	public static synchronized List<Utente> doRetrieveAll() throws SQLException{
		Connection con = null;
		List<Utente> lista = new LinkedList<>();
		
		try{
			con = DriverManagerConnectionPool.getConnection();

			PreparedStatement ps = con.prepareStatement(CERCA_TUTTI);
	
			ResultSet rs = ps.executeQuery();
	
			while(rs.next()){
				Utente utente = new Utente();
	
				utente.setUsername(rs.getString("Username"));
				utente.setPassword(rs.getString("Password"));
				utente.setEmail(rs.getString("Email"));
	
				Date dataLetta = rs.getDate("DataCreazione");
				GregorianCalendar dataCreazione = new GregorianCalendar();
				dataCreazione.setTime(dataLetta);
				utente.setDataCreazione(dataCreazione);
	
				utente.setUltimoAccesso(rs.getTimestamp("UltimoAccesso"));
				utente.setImmagineProfilo(rs.getBinaryStream("ImmagineProfilo"));
				utente.setStato(rs.getString("Stato"));
	
				lista.add(utente);
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

	
	public static synchronized List<String> doRetrieveUsernamesBySequence(Utente utente, String sequence) throws SQLException{
		Connection con = null;
		List<String> lista = new LinkedList<>();

		try {
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps = con.prepareStatement(CERCA_UTENTE_SEQUENZA);
			
			ps.setString(1, "%" + sequence + "%");
			ps.setString(2, utente.getUsername());
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				lista.add(rs.getString("Username"));
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
	
	private static final String NOME_TABELLA = " 2Stream.Utente ";

	private static final String SALVA_UTENTE = "INSERT INTO" + NOME_TABELLA 
											 + "VALUES (?,?,?,?,?,?,?)";

	private static final String AGGIORNA_UTENTE_PASSWORD = "UPDATE" + NOME_TABELLA 
														 + "SET Password=? "
														 + "WHERE Username=?";
	
	private static final String AGGIORNA_UTENTE_ULTIMO_ACCESSO = "UPDATE" + NOME_TABELLA 
															   + "SET UltimoAccesso=? "
															   + "WHERE Username=?";
	
	private static final String AGGIORNA_UTENTE_IMMAGINE_PROFILO = "UPDATE" + NOME_TABELLA 
																 + "SET ImmagineProfilo=? "
																 + "WHERE Username=?";
	
	private static final String AGGIORNA_UTENTE_STATO = "UPDATE" + NOME_TABELLA 
													  + "SET Stato=? "
													  + "WHERE Username=?";
	
	private static final String CANCELLA_UTENTE = "DELETE FROM" + NOME_TABELLA 
												+ "WHERE Username=?";
	
	private static final String CERCA_USERNAME = "SELECT * "
											   + "FROM" + NOME_TABELLA 
											   + "WHERE Username=?";

	private static final String CERCA_UTENTE = "SELECT * "
											 + "FROM" + NOME_TABELLA 
											 + "WHERE Username=? AND Password=?";

	private static final String CERCA_UTENTE_SEQUENZA = "SELECT Username "
													  + "FROM" + NOME_TABELLA 
												  	  + "WHERE Username LIKE ? and Username!=?";
	
	private static final String CERCA_TUTTI = "SELECT * "
											+ "FROM" + NOME_TABELLA;
	
}
