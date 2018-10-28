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

import bean.Chat;
import bean.Preferenza;
import bean.Utente;
import beanDAO.PreferenzaDAO;
import beanDAO.ServiziChatDCS;
import beanDAO.UtenteDAO;
import utility.Base64Utility;

@WebServlet("/AggiornamentoContatti")
public class ServletAggiornamentoContatti extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		Utente user = (Utente) request.getSession(false).getAttribute("user");
		
		if(user != null){
			List<Chat> listaChat = null;
			try {
				listaChat = ServiziChatDCS.getChatsUtente(user);
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=non riesco a scaricare le chat!").forward(request, response);
			}
			JsonArrayBuilder builder = Json.createArrayBuilder();
			
			if(listaChat != null){
				String immagineDefault = Utente.getIMMAGINE_PROFILO_BASE64();
				
				for(Chat chat:listaChat){
					String image = null;
					Utente utenteDellaLista = null;
					
					try {
						utenteDellaLista = UtenteDAO.doRetrieveByUsername(chat.getInterlocutore());
						Preferenza preferenza = PreferenzaDAO.doRetrieveByUsername(utenteDellaLista.getUsername());
						
						if(preferenza.isPrivacyImmagine()) 
							image = immagineDefault;	
						else 
							image = Base64Utility.encodeFileToBase64(utenteDellaLista.getImmagineProfilo());
					} catch (SQLException e) {
						request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Non riesco a scaricare l'immagine profilo di "+chat.getInterlocutore()).forward(request, response);
					}
					builder.add(Json.createObjectBuilder().add("name", chat.getInterlocutore()).add("image", image).add("ultimaModifica", chat.getUltimaModifica().getTime()));
				}
			}
			
			JsonArray jarr = builder.build();
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jarr);
			out.flush();
		}
		else{
			request.getRequestDispatcher("/signIn.jsp").forward(request, response);
		}
	}
}
