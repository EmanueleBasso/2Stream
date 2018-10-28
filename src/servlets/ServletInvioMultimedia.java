package servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import bean.Chat;
import bean.MessaggioMultimediale;
import bean.Utente;
import beanDAO.ServiziChatDCS;
import beanDAO.ServiziMessaggiDCS;
import beanDAO.UtenteDAO;

@MultipartConfig(maxFileSize = 1024 * 1024 * 5,maxRequestSize = 1024 * 1024 * 10)
@WebServlet("/InvioMultimedia")
public class ServletInvioMultimedia extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		GregorianCalendar calendar = new GregorianCalendar();
		String ora = request.getParameter("ora");
		String timeZone = request.getParameter("timezone");
		
		if((ora != null) || (timeZone != null)){
			long oraMillisecondi = 0;
			long timeZoneMillisecondi = 0;
			
			try{
				oraMillisecondi = Long.parseLong(ora);
				timeZoneMillisecondi = Long.parseLong(timeZone) * 60000;
			}catch (NumberFormatException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Non cambiare la data!").forward(request, response);	
			}
			
			calendar.setTimeInMillis(oraMillisecondi + timeZoneMillisecondi);
		}else
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nel calcolare orario").forward(request, response);	

		MessaggioMultimediale messaggio = new MessaggioMultimediale();		
		messaggio.setDataInvio(new Timestamp(calendar.getTimeInMillis()));
		
		InputStream immagine=null;
		Collection<Part> requestParts = request.getParts();
		for(Part part: requestParts){
			String fileName = extractFileName(part);
			if(fileName!=null && !fileName.equals("")){
				immagine = part.getInputStream();
			}
		}
		
		if(immagine==null){
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Nessun multimedia nella richiesta").forward(request, response);	
		}
		
		messaggio.setContenuto(immagine);
		
		Utente mittente = (Utente) request.getSession(false).getAttribute("user");
		if (mittente != null)
			messaggio.setMittente(mittente.getUsername());
		else
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nel recuperare sessione").forward(request,response);
		
		Utente destinatario = null;
		try {
			String nome = request.getParameter("destinatario");
			if(nome != null)
				destinatario = UtenteDAO.doRetrieveByUsername(nome);
			else
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema ricezione destinatario").forward(request, response);
		} catch (SQLException e) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema indivudiazione destinatario").forward(request, response);
		}
		
		Chat chat = null;
		try {
			if(destinatario !=null && mittente != null)
				chat = ServiziChatDCS.getChat(mittente, destinatario);
		} catch (SQLException e) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema nel recuperare chat").forward(request,response);
		}
			
		try {
			if(chat != null && messaggio != null)
				ServiziMessaggiDCS.nuovoMessaggioMultimediale(chat, messaggio);
		} catch (SQLException e) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema nel salvare il messaggio").forward(request,response);
		}
	}
	
	private String extractFileName(Part part){
		//content-disposition: form-data; name="nomeCampo"; filename="nomeFile.*"
		String header = part.getHeader("content-disposition");
		String[] items = header.split(";");
		for(String item: items){
			if(item.trim().startsWith("filename")){
				String fileName = item.substring(item.indexOf("=") + 2, item.length() - 1);
				fileName = new File(fileName).getName();
				return fileName;
			}
		}
		return "";
	}
}
