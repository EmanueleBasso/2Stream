package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashSet;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Preferenza;
import bean.Utente;
import beanDAO.PreferenzaDAO;
import beanDAO.UtenteDAO;

@WebServlet("/UserOnline")
public class ServletUserOnline extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		String interlocutore  = request.getParameter("username");
		
		if(interlocutore == null) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Javascript Manipolato!").forward(request, response);
		}
		
		Preferenza preferenza = null;
		try {
			 preferenza = PreferenzaDAO.doRetrieveByUsername(interlocutore);
		} catch (SQLException e1) {
			request.getRequestDispatcher("/ErrorPage.jsp?Errore nell'ottenere le preferenze dell'utente sulla privacy").forward(request, response);
		}
		
		if(preferenza == null) {
			request.getRequestDispatcher("/ErrorPage.jsp?Preferenze interlucotore non presente").forward(request, response);
		}
		ServletContext context =  getServletContext();
		HashSet<String> sessioni = (HashSet<String>) context.getAttribute("sessioni");
		
		JsonObjectBuilder connectionStatus = null;		
		
		if(preferenza.isPrivacyAccesso()){
			connectionStatus = Json.createObjectBuilder().add("stato","privacy");
		}else if(sessioni.contains(interlocutore)){
			connectionStatus = Json.createObjectBuilder().add("stato","online");
		}else{
			Utente utente;
			try {
				utente = UtenteDAO.doRetrieveByUsername(interlocutore);
				
				connectionStatus = Json.createObjectBuilder().add("stato", "offline")
															 .add("accesso", utente.getUltimoAccesso().getTime());
			
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema Col Database").forward(request, response);
			}
		}
		response.setContentType("application/json");
		out.print(connectionStatus.build());
	}
}
