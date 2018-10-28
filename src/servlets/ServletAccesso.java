package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Utente;
import beanDAO.UtenteDAO;
import utility.OraUtility;

@WebServlet("/Accesso")
public class ServletAccesso extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		ServletContext contesto = request.getServletContext();
		
		RequestDispatcher disp;
		if (username == null || password == null || (!Pattern.matches(regexUsername, username)) || (!Pattern.matches(regexPassword, password))) {
			incrementaAccessiSblagliati(contesto);
			String errore = "paramsErr";
			disp = request.getRequestDispatcher("/signIn.jsp?err=" + errore);
			disp.forward(request, response);
		}
		
		Utente user = null;
		try {
			user = UtenteDAO.doRetrieveByUsernamePassword(username, password);
			if (user != null) {
				user.setUltimoAccesso(new Timestamp(OraUtility.convertToGMT(new GregorianCalendar())));
				UtenteDAO.doUpadateUltimoAccesso(user);

				HttpSession session = request.getSession(true);
				session.setMaxInactiveInterval(1*60);				
				session.setAttribute("user", user);
				
				ServletContext context =  getServletContext();
				HashSet<String> sessioni = (HashSet<String>) context.getAttribute("sessioni");
				
				synchronized (sessioni) {
					sessioni.add(username);
					int utentiOnline = sessioni.size();
					synchronized (context) {
						int maxUtenti = (int) context.getAttribute("maxAttivi");
						if(utentiOnline > maxUtenti) {
							context.setAttribute("maxAttivi", utentiOnline);
						}
					}
				}
			}
		} catch (SQLException e) {
			incrementaAccessiSblagliati(contesto);
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema a interfacciarsi col Database!").forward(request, response);
		}

		if (user != null) {
			disp = request.getRequestDispatcher("/homePage.jsp");
			disp.forward(request, response);
		} else {
			String errore = "notReg";
			incrementaAccessiSblagliati(contesto);
			disp = request.getRequestDispatcher("/signIn.jsp?err=" + errore);
			disp.forward(request, response);
		}
	}
	
	private static synchronized void incrementaAccessiSblagliati(ServletContext contesto){
		long accessiErrati = (long) contesto.getAttribute("accessiErrati");
		accessiErrati = accessiErrati + 1;
		contesto.setAttribute("accessiErrati", accessiErrati);
	}
	
	private final static String regexUsername = "^[a-zA-Z]+[0-9]*$";
	private final static String regexPassword = "^\\S*.{6}$";

}
