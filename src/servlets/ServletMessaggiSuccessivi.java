package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Chat;
import bean.Messaggio;
import bean.MessaggioMultimediale;
import bean.MessaggioTestuale;
import bean.Utente;
import beanDAO.ServiziChatDCS;
import beanDAO.ServiziMessaggiDCS;
import beanDAO.UtenteDAO;
import utility.Base64Utility;

@WebServlet("/MessaggiSuccessivi")
public class ServletMessaggiSuccessivi extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if(session == null){
			request.getRequestDispatcher("/signIn.jsp").forward(request, response);
		}
		
		Utente utenteLoggato = (Utente) session.getAttribute("user"); 
		
		if(utenteLoggato == null) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Utente non connesso, prima effettuare il login!").forward(request, response);
		}
		
		String username = request.getParameter("username");
		
		Utente utenteNuovaConversazione = null;
		
		try {
			utenteNuovaConversazione = UtenteDAO.doRetrieveByUsername(username);
		} catch (SQLException e) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema col Database a prendere l'utente").forward(request, response);
		}
		if(utenteNuovaConversazione == null){
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Utente non presente sul Database!!!").forward(request, response);
		}
		
		Chat chat = null;
		try {
			chat = ServiziChatDCS.getChat(utenteLoggato, utenteNuovaConversazione);
		} catch (SQLException e1) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Utente non presente sul Database!!!").forward(request, response);
		}
		
		PrintWriter out = response.getWriter();

		String primoTestuale = request.getParameter("primoTestuale");
		String primoMultimediale = request.getParameter("primoMultimediale");
		
		if(Pattern.matches(regexID, primoTestuale) && Pattern.matches(regexID, primoMultimediale)) {
			try {
				int intTestuale = Integer.parseInt(primoTestuale);
				int intMultimediale = Integer.parseInt(primoMultimediale);
				int idChat = chat.getIdChat();
				
				List<Messaggio> messaggi = ServiziMessaggiDCS.getMessaggiSuccessiviChat(idChat, intTestuale, intMultimediale);
				
				if(messaggi!=null) {
					JsonArrayBuilder arrayMessaggiBuilder = Json.createArrayBuilder();
					JsonObjectBuilder messObj;
					
					for(Messaggio mess : messaggi) {	
						messObj = Json.createObjectBuilder();
						
						messObj.add("id", mess.getIdMessaggio())							         
							   .add("mittente", mess.getMittente())
							   .add("dataInvio", mess.getDataInvio().getTime());
						
						if(mess.getClass().getSimpleName().equals("MessaggioTestuale")) {
							MessaggioTestuale testuale = (MessaggioTestuale) mess;							
							messObj.add("contenutoTestuale", testuale.getContenuto());
						}
						else {
							MessaggioMultimediale multimediale = (MessaggioMultimediale) mess;
							messObj.add("contenutoMultimediale",Base64Utility.encodeFileToBase64(multimediale.getContenuto()));
						}
						
						arrayMessaggiBuilder.add(messObj);
					}
					
					response.setContentType("application/json");
					JsonObjectBuilder userObj = Json.createObjectBuilder();
					out.print(userObj.add("messaggi",arrayMessaggiBuilder).build());
				}
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema Col Database a prendere i messaggi").forward(request, response);
			}
		}
		else {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Parametri modificati!").forward(request, response);
		}
	}
	
	private final static String regexID = "^([0]{1}|([1-9][0-9]*))$";
}
