package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.Preferenza;
import bean.Utente;
import beanDAO.PreferenzaDAO;
import beanDAO.UtenteDAO;

@MultipartConfig(maxFileSize = 1024 * 1024 * 20,maxRequestSize = 1024 * 1024 * 50)
@WebServlet("/SetPreferenza")
public class ServletSetPreferenza extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession(false);

		if(session == null){
			request.getRequestDispatcher("/signIn.jsp").forward(request, response);
		}
		
		Utente utenteLoggato = (Utente) session.getAttribute("user"); 
		
		if(utenteLoggato == null) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Utente non connesso, prima effettuare il login!").forward(request, response);
		}

		Preferenza preferenza = new Preferenza();
		preferenza.setUsernameUtente(utenteLoggato.getUsername());

		String coloreTema = request.getParameter("coloreTema");
		if(coloreTema != null) {
			preferenza.setColoreTema(coloreTema);
			
			try {
				PreferenzaDAO.doUpdateColoreTema(preferenza);
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare il colore del baloon!").forward(request, response);
			}
		}
		
		String privacyAccesso = request.getParameter("privacyAccesso");
		if(privacyAccesso != null) {
			if(privacyAccesso.equals("true"))
				preferenza.setPrivacyAccesso(true);
			else
				preferenza.setPrivacyAccesso(false);
			
			try {
				PreferenzaDAO.doUpdatePrivacyAccesso(preferenza);
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare privacy accesso!").forward(request, response);
			}
		}
		
		String privacyImmagine = request.getParameter("privacyImmagine");
		if(privacyImmagine != null) {
			if(privacyImmagine.equals("true"))
				preferenza.setPrivacyImmagine(true);			
			else
				preferenza.setPrivacyImmagine(false);
			
			try {
				PreferenzaDAO.doUpdatePrivacyImmagine(preferenza);
			} catch (SQLException e) {
				e.printStackTrace();
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare privacy immagine!").forward(request, response);
			}
		}
		
		
		String font = request.getParameter("font");
		if(font != null) {
			preferenza.setFont(font);
			
			try {
				PreferenzaDAO.doUpdateFont(preferenza);
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare il font!").forward(request, response);
			}
		}	
		
		String password = request.getParameter("password");
		if(password != null) {
			utenteLoggato.setPassword(password);			

			try {
				UtenteDAO.doUpadateStato(utenteLoggato);
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare lo stato!").forward(request, response);
			}
		}
		
		String stato = request.getParameter("stato");
		if(stato != null) {
			utenteLoggato.setStato(stato);			

			try {
				UtenteDAO.doUpadateStato(utenteLoggato);
			} catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare lo stato!").forward(request, response);
			}
		}
		
		InputStream immagine = null;
		Collection<Part> requestParts = request.getParts();
		for(Part part: requestParts){
			String fileName = extractFileName(part,"immagineSfondo");
			if(fileName!=null && !fileName.equals("")){
				immagine = part.getInputStream();
			}
		}
		
		if(immagine != null){
			preferenza.setImmagineSfondo(immagine);
			
			try {
				PreferenzaDAO.doUpdateImmagineSfondo(preferenza);
			} 
			catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare immagine sfondo!").forward(request, response);
			}
		}
		
		immagine = null;
		for(Part part: requestParts){
			String fileName = extractFileName(part,"immagineProfilo");
			if(fileName!=null && !fileName.equals("")){
				immagine = part.getInputStream();
			}
		}

		if(immagine != null){
			utenteLoggato.setImmagineProfilo(immagine);			

			try {
				UtenteDAO.doUpadateImmagineProfilo(utenteLoggato);
			} 
			catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Errore nell'aggiornare immagine Profilo!").forward(request, response);
			}
		}
	}
	
	private String extractFileName(Part part, String nomeParam){
		//content-disposition: form-data; name="nomeCampo"
		String header = part.getHeader("content-disposition");
				
		String[] items = header.split(";");
		for(int i = 0; i < items.length; i++){
			String campo = items[i];
			if(campo.trim().startsWith("name")){
				campo = campo.substring(campo.indexOf("=") + 2, campo.length() - 1);
				if(campo.equals(nomeParam)) {
					return campo;
				}
			}
		}
		return "";
	}
}
