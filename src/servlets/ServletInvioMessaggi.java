package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Chat;
import bean.MessaggioTestuale;
import bean.Utente;
import beanDAO.ServiziChatDCS;
import beanDAO.ServiziMessaggiDCS;
import beanDAO.UtenteDAO;

@WebServlet("/InvioMessaggi")
public class ServletInvioMessaggi extends HttpServlet {
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

		MessaggioTestuale messaggio = new MessaggioTestuale();
		
		messaggio.setDataInvio(new Timestamp(calendar.getTimeInMillis()));
		
		String mex = request.getParameter("messaggio");
		if(mex != null) 
			messaggio.setContenuto(mex);
		else
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=errore nella ricezione messaggio").forward(request, response);
		
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
		} catch (SQLException e1) {
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
				ServiziMessaggiDCS.nuovoMessaggioTestuale(chat, messaggio);
		} catch (SQLException e) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema nel salvare il messaggio").forward(request,response);
		}
	}

}
