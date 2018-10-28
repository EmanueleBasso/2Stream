package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beanDAO.UtenteDAO;

@WebServlet("/UserExists")
public class ServletUserExists extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		
		boolean ricerca=true;
		if(username!=null){
			try{
				ricerca = UtenteDAO.exists(username);
			}
			catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema a interfacciarsi col Database!").forward(request, response);
			}
		}
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		JsonObject jsonObject;
		if(ricerca == true){
			jsonObject = Json.createObjectBuilder().add("errore", "0").build();
			out.print(jsonObject);
		}
		else{
			jsonObject = Json.createObjectBuilder().add("errore", "1").build();
			out.print(jsonObject);
		}
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
