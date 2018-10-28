package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Preferenza;
import bean.Utente;
import beanDAO.PreferenzaDAO;
import utility.Base64Utility;

@WebServlet("/Preferenza")
public class ServletPreferenza extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utente utente = (Utente) request.getSession(false).getAttribute("user");
		
		String username = utente.getUsername();
		Preferenza preferenza = null;
		
		try {
			preferenza = PreferenzaDAO.doRetrieveByUsername(username);
		} catch (SQLException e) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore col database").forward(request, response);	
		}
		
		if(preferenza == null) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Impossibile ottenere le preferenze dell'utente").forward(request, response);	
		}

		JsonObjectBuilder preferenzaObj = Json.createObjectBuilder();
		preferenzaObj.add("immagine", Base64Utility.encodeFileToBase64(utente.getImmagineProfilo()));
		preferenzaObj.add("stato",utente.getStato());
		preferenzaObj.add("tema", preferenza.getColoreTema());
		preferenzaObj.add("privacyAccesso", preferenza.isPrivacyAccesso());
		preferenzaObj.add("privacyImmagine", preferenza.isPrivacyImmagine());
		preferenzaObj.add("sfondo", Base64Utility.encodeFileToBase64(preferenza.getImmagineSfondo()));
		preferenzaObj.add("font", preferenza.getFont());
		
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		out.print(preferenzaObj.build());
	}
}
