package beanDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.MessaggioTestuale;
import connection.DriverManagerConnectionPool;

public class MessaggioTestualeDAO {
	
	public static synchronized void doSave(MessaggioTestuale messaggio) throws SQLException{
		Connection con = null;
	
		try {
			con = DriverManagerConnectionPool.getConnection();
	
			PreparedStatement ps = con.prepareStatement(SALVA_MESSAGGIO);
			
			ps.setInt(1,messaggio.getIdMessaggio());
			ps.setString(2, messaggio.getMittente());
			ps.setTimestamp(3, messaggio.getDataInvio());
			ps.setString(4, messaggio.getContenuto());
			
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

	public static synchronized MessaggioTestuale doRetrieveByID(int idMessaggio) throws SQLException{
		Connection con = null;
		MessaggioTestuale messaggio = null;
		
		try {
			con = DriverManagerConnectionPool.getConnection();
			
			PreparedStatement ps = con.prepareStatement(CERCA_MESSAGGIO);
			ps.setInt(1, idMessaggio);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				messaggio = new MessaggioTestuale();
				
				messaggio.setIdMessaggio(idMessaggio);
				messaggio.setMittente(rs.getString("Mittente"));
				messaggio.setDataInvio(rs.getTimestamp("DataInvio"));
				messaggio.setContenuto(rs.getString("Testo"));
			}
		}
		catch (SQLException sqlException) {
			throw sqlException;
		}
		finally{
			DriverManagerConnectionPool.releaseConnection(con);
		}
	
		return messaggio;
	}
	
	private static final String NOME_TABELLA = " 2Stream.MessaggioTestuale ";
	
	private static final String SALVA_MESSAGGIO = "INSERT INTO" + NOME_TABELLA 
			   									+ "VALUES (?,?,?,?)";
	
	private static final String CERCA_MESSAGGIO = "SELECT * " 
												+ "FROM" + NOME_TABELLA
												+ "WHERE IDMessaggioTestuale=?";
	
}
