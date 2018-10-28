package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import bean.Utente;
import beanDAO.ServiziUtenteDCS;

@WebServlet("/Confirm")
public class ServletConfermaEmail extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String param = request.getParameter("cod");
		
		if(param==null){
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Richiesta errata, non modificare il link ricevuto!").forward(request, response);
		}
		
		HttpSession session = request.getSession(false);
		boolean success = false;
		if(session!=null){
			String id = session.getId();
			Utente nuovoUtente = (Utente) session.getAttribute("nuovoUtente");
			
			if(id.equals(param)){
				try {
					ServiziUtenteDCS.creaUtente(nuovoUtente);
				}catch(SQLException e) {
					request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema Col Database").forward(request, response);
				}
				
				session.invalidate();
				success=true;
			}
		}
		
		if(success)
			request.getRequestDispatcher("/signUpSucceed.html").forward(request, response);
		else
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Registrazione non avvenuta con successo!").forward(request, response);
	}
	
}
