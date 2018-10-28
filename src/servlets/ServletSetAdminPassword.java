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
import javax.servlet.http.HttpSession;

import bean.Amministratore;
import beanDAO.AmministratoreDAO;

@WebServlet("/SetAdminPassword")
public class ServletSetAdminPassword extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 HttpSession sessione = request.getSession(false);
		 
		 if(sessione == null) {
				request.getRequestDispatcher("/").forward(request, response);  //pagina login admin
		 }
		 Amministratore admin = (Amministratore) sessione.getAttribute("admin");
		 
		 String password = request.getParameter("password");

		 boolean esito = false;
		 if((password != null) && (password.length()>=6)) {
			 try {
				AmministratoreDAO.doUpdatePassword(admin);
				admin.setPassword(password);
				esito = true;
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare password amministratore!").forward(request, response);
			}
		 }
		JsonObjectBuilder cambiamentoPassword = Json.createObjectBuilder();
		
		if(esito)
			cambiamentoPassword.add("password", "nuova");
		
		else
			cambiamentoPassword.add("password", "vecchia");
		
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		out.print(cambiamentoPassword.build());
	}

}
