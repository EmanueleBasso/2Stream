package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DriverManagerConnectionPool {
	public DriverManagerConnectionPool(String connessione,String utente,String password,String driver) throws Exception{
		freeDbConnection = new LinkedList<>();
		
		CONNESSIONE = connessione;
		USER = utente;
		PASS = password;
		
		try{
			Class.forName(driver).newInstance();
		}catch (Exception e) {
			throw new Exception("Errore nel caricare i driver");
		}
		
		try{
			initializeConnections();
		}catch (SQLException e) {
			throw new Exception("Errore nella creazione del pool di connessioni");
		}
	}
	
	public static synchronized void initializeConnections() throws SQLException {
		for (int i = 0; i < 5; i++) {
			Connection con = createDbConnection();
			freeDbConnection.add(con);
		}
	}

	private static Connection createDbConnection() throws SQLException {
		Connection con = DriverManager.getConnection(CONNESSIONE, USER, PASS);

		return con;
	}

	public static synchronized Connection getConnection() throws SQLException {
		Connection con;

		if (!freeDbConnection.isEmpty()) {
			con = freeDbConnection.get(0);
			freeDbConnection.remove(0);

			try {
				if (con.isClosed())
					con = getConnection();
			} catch (SQLException e) {
				con.close();
				con = getConnection();
			}
		} else {
			con = createDbConnection();
		}

		modifyNumeroConnessioniOccupate(+1);
		return con;
	}

	public static synchronized void releaseConnection(Connection con) throws SQLException{
		if (con != null) {
			freeDbConnection.add(con);
			modifyNumeroConnessioniOccupate(-1);
			balanceConnection();
		}
	}

	private static synchronized void balanceConnection() throws SQLException{
		int size = freeDbConnection.size();
		if (size >= 100) {
			for (int i = 0; i < (size / 2); i++) {
				Connection con = freeDbConnection.remove(0);
				con.close();
			}
				
		}
	}
	
	public static synchronized int getNumeroConnessioniOccupate() {
		return numeroConnessioniOccupate;
	}
	
	public static synchronized void modifyNumeroConnessioniOccupate(int val){
		numeroConnessioniOccupate = numeroConnessioniOccupate + val;
	}

	private static List<Connection> freeDbConnection;
	private static String CONNESSIONE;
	private static String USER;
	private static String PASS;
	private static int numeroConnessioniOccupate = 0;
}
