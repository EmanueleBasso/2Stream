package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Utente;
import beanDAO.UtenteDAO;
import utility.Base64Utility;

@WebServlet("/Stato")
public class ServletStato extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		
		Utente user = null;
		try {
			user = UtenteDAO.doRetrieveByUsername(username);
		} catch (SQLException e) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema aggiornamento stato utente").forward(request, response);
		}
		response.setContentType("application/json");
		response.getWriter().print(Json.createObjectBuilder().add("stato", user.getStato()).add("immagine", Base64Utility.encodeFileToBase64(user.getImmagineProfilo())).build());
	}
}
