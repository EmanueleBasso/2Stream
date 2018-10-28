package beanDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import bean.Amministratore;
import connection.DriverManagerConnectionPool;

public class AmministratoreDAO {
	public static synchronized void doUpdatePassword(Amministratore amministratore) throws SQLException{	
		Connection con = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(AGGIORNA_AMMINISTRATORE_PASSWORD);
			
			ps.setString(1,amministratore.getPassword());
			ps.setString(2,amministratore.getUsername());
			
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
	
	public static synchronized Amministratore doRetrieveByUsernamePassword(String username,String password) throws SQLException{
		Connection con = null;
		Amministratore amministratore = null;

		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(CERCA_USERNAME);
			
			ps.setString(1,username);
			ps.setString(2,password);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				amministratore = new Amministratore();
				
				amministratore.setUsername(rs.getString("Username"));
				amministratore.setPassword(rs.getString("Password"));
				
				Date dataLetta = rs.getDate("DataCreazione");
				GregorianCalendar dataCreazione = new GregorianCalendar();
				dataCreazione.setTime(dataLetta);
				amministratore.setDataCreazione(dataCreazione);
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
		return amministratore;
	}
		
	private static final String NOME_TABELLA = " 2Stream.Amministratore ";
	
	private static final String AGGIORNA_AMMINISTRATORE_PASSWORD = "UPDATE" + NOME_TABELLA 
																 + "Set Password=? "
																 + "WHERE Username=?";
	
	private static final String CERCA_USERNAME = "SELECT * "
											   + "FROM" + NOME_TABELLA
											   + "WHERE Username=? AND Password=?";
}
