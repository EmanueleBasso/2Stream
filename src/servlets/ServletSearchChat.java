package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Utente;
import beanDAO.UtenteDAO;
import utility.Base64Utility;

@WebServlet("/SearchChat")
public class ServletSearchChat extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if(session == null){
			request.getRequestDispatcher("/signIn.jsp").forward(request, response);
		}

		Utente utenteLoggato = (Utente) session.getAttribute("user"); 
		
		if(utenteLoggato == null) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Utente non connesso, prima effettuare il login!").forward(request, response);
		}
		
		String sequence = request.getParameter("sequence");
		List<String> listaUtenti = null;
	
		if(!sequence.isEmpty()){
			try {
				listaUtenti = UtenteDAO.doRetrieveUsernamesBySequence(utenteLoggato,sequence);
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema Col Database").forward(request, response);
			}
		}
		
		JsonArrayBuilder builder = Json.createArrayBuilder();

		if(listaUtenti != null){
			for (String user:listaUtenti)
			{
				String image = null;
				try {
					Utente utenteDellaLista = UtenteDAO.doRetrieveByUsername(user);
					image = Base64Utility.encodeFileToBase64(utenteDellaLista.getImmagineProfilo());
				} catch (SQLException e) {
					request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema Col Database").forward(request, response);
				}
				
				builder.add(Json.createObjectBuilder().add("name", user).add("image", image).build());
			}
		}
		
		JsonArray jarr = builder.build();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(jarr);
		out.flush();
	}

}
