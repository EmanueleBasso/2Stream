package beanDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.DriverManagerConnectionPool;

public class ServiziAmministratoreDCS {
	public static synchronized int numeroIscritti() throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(NUMERO_ISCRITTI);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int val = rs.getInt(1);

			rs.close();
			ps.close();

			return val;
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized int numeroChat() throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(NUMERO_CHAT);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int val = rs.getInt(1);

			rs.close();
			ps.close();

			return val;
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized int numeroMexTestuali() throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(NUMERO_TESTUALI);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int val = rs.getInt(1);

			rs.close();
			ps.close();

			return val;
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized int numeroMexMultimediali() throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(NUMERO_MULTIMEDIALI);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int val = rs.getInt(1);

			rs.close();
			ps.close();

			return val;
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized String utilizzoDatbase() throws SQLException{
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(OCCUPAZIONE_DATABASE);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String val = rs.getString(1);

			rs.close();
			ps.close();

			return val;
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	}
	
	public static synchronized int utentiIscrittiInData(Date data) throws SQLException{
		Connection con = null;
		int iscritti = -1;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(UTENTI_DATA);
			ps.setDate(1, data);
			ResultSet rs = ps.executeQuery();
			rs.next();
			iscritti = rs.getInt(1);

			rs.close();
			ps.close();

		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
		
		return iscritti;
	}
	
	
	public final static int QUOTA = 16;
	
	public final static int MAX_CONNESSIONI = 151;
	
	private final static String NUMERO_ISCRITTI = "SELECT count(*) "
												+ "FROM 2Stream.Utente";

	private final static String NUMERO_CHAT = "SELECT max(IDChat) "
												+ "FROM 2Stream.Chat";
	
	private final static String NUMERO_TESTUALI = "SELECT max(IDMessaggioTestuale) "
												+ "FROM 2Stream.MessaggioTestuale";
	
	private final static String NUMERO_MULTIMEDIALI = "SELECT max(IDMessaggioMultimediale) "
													+ "FROM 2Stream.MessaggioMultimediale";
	
	private final static String OCCUPAZIONE_DATABASE = "SELECT sum(round(((data_length+index_length)/1024/1024),2)) "
											         + "FROM information_schema.tables "
											         + "WHERE table_schema = \"2Stream\"";
	
	private final static String UTENTI_DATA = "SELECT count(Username) "
											+ "FROM 2Stream.Utente "
											+ "WHERE DataCreazione = ?";

}
