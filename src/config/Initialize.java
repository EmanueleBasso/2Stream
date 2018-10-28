package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import connection.DriverManagerConnectionPool;
import utility.MailUtility;

@WebListener
public class Initialize implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String path = sce.getServletContext().getRealPath("/settings");
	
		inizializzazioneDatabase(path);
		inizializzazioneEmail(path);
		
		ServletContext context = sce.getServletContext();
		HashSet<String> sessioni = new HashSet<>(512);
		context.setAttribute("sessioni", sessioni);
		
		long accessiErrati = 0;
		context.setAttribute("accessiErrati", accessiErrati);
		
		long sessioniInvalidate = 0;
		context.setAttribute("sessioniInvalidate", sessioniInvalidate);
		
		int maxAttivi = 0;
		context.setAttribute("maxAttivi", maxAttivi);
	}
	
	private void inizializzazioneDatabase(String path) throws RuntimeException{
		String connessione,utente,password,driver;
		InputStream io = apriFile(path,"/DatabaseSettings.json","del database");

		JsonReader jsonReader = Json.createReader(io);
		JsonObject obj = jsonReader.readObject();

		connessione = obj.getString("connessione");
		utente = obj.getString("user");
		password = obj.getString("password");
		driver = obj.getString("driver");

		jsonReader.close();
		try{
			io.close();
		}catch (IOException e) {
			throw new RuntimeException("Errore nel chiudere il file");
		}
		
		try{
			new DriverManagerConnectionPool(connessione,utente,password,driver);
		}catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void inizializzazioneEmail(String path) throws RuntimeException{
		String email,password,porta,host,url;
		InputStream io = apriFile(path,"/MailSettings.json","dell'email");
		
		JsonReader jsonReader = Json.createReader(io);
		JsonObject obj = jsonReader.readObject();
		
		email = obj.getString("email");
		password = obj.getString("password");
		porta = obj.getString("porta");
		host = obj.getString("host");
		url = obj.getString("url");
		
		jsonReader.close();
		try{
			io.close();
		}catch (IOException e) {
			throw new RuntimeException("Errore nel chiudere il file");
		}
		
		new MailUtility(email,password,porta,host,url);
	}
	
	private InputStream apriFile(String path,String file,String errore) throws RuntimeException{
		InputStream io = null;
		
		try {
			File f = new File(path + file);
			io = new FileInputStream(f);
			return io;
		} catch (IOException e) {
			throw new RuntimeException("Errore nell'aprire il file dei settaggi " + errore);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//	VUOTO
	}
}
