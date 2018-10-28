package config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import bean.Chat;
import bean.MessaggioMultimediale;
import bean.MessaggioTestuale;
import connection.DriverManagerConnectionPool;

@WebListener
public class SynchronizeDataBase implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			setIDChat();
			setIDMessaggiTestuali();
			setIDMessaggiMultimediali();
		} catch (SQLException e) {
			throw new RuntimeException("Errore nel settare gli ID dal database");
		}
	}

	private void setIDChat() throws SQLException {
		Connection con = DriverManagerConnectionPool.getConnection();
		
		PreparedStatement ps = con.prepareStatement(SET_CHAT);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
			Chat.setProgressivo(rs.getInt("max(IdChat)"));
		else
			Chat.setProgressivo(0);
	}
	
	private void setIDMessaggiTestuali() throws SQLException{
		Connection con = DriverManagerConnectionPool.getConnection();
		
		PreparedStatement ps = con.prepareStatement(SET_TESTUALE);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
			MessaggioTestuale.setProgressivo(rs.getInt("max(IDMessaggioTestuale)"));
		else
			MessaggioTestuale.setProgressivo(0);
	}
	
	private void setIDMessaggiMultimediali() throws SQLException{
		Connection con = DriverManagerConnectionPool.getConnection();
		
		PreparedStatement ps = con.prepareStatement(SET_MULTIMEDIALE);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
			MessaggioMultimediale.setProgressivo(rs.getInt("max(IDMessaggioMultimediale)"));
		else
			MessaggioMultimediale.setProgressivo(0);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//VUOTO
	}
	
	private final static String SET_CHAT = "SELECT max(IdChat) " 
										 + "FROM 2Stream.Chat "; 
	
	private final static String SET_TESTUALE = "SELECT max(IDMessaggioTestuale) " 
			 								 + "FROM 2Stream.MessaggioTestuale "; 
	
	private final static String SET_MULTIMEDIALE = "SELECT max(IDMessaggioMultimediale) " 
												 + "FROM 2Stream.MessaggioMultimediale ";
												
}
