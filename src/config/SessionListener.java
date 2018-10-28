package config;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import bean.Utente;
import beanDAO.UtenteDAO;
import utility.OraUtility;

@WebListener
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
			//VUOTO
	}

	@Override
	public synchronized void sessionDestroyed(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		
		Utente utente = ((Utente) session.getAttribute("user"));
		String username = utente.getUsername();
		
		ServletContext context = session.getServletContext();
		synchronized (context) {
			HashSet<String> sessioni = (HashSet<String>) context.getAttribute("sessioni");
			incrementaSessioniInvalidate(context);
			
			sessioni.remove(username);
	
			utente.setUltimoAccesso(new Timestamp(OraUtility.convertToGMT(new GregorianCalendar())));
		}
		try {
			UtenteDAO.doUpadateUltimoAccesso(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static synchronized void incrementaSessioniInvalidate(ServletContext contesto){
		long sessioniInvalidate = (long) contesto.getAttribute("sessioniInvalidate");
		sessioniInvalidate = sessioniInvalidate + 1;
		contesto.setAttribute("sessioniInvalidate", sessioniInvalidate);
	}
}