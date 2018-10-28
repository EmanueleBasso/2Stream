package servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import bean.Utente;
import utility.MailUtility;

@MultipartConfig(maxFileSize = 1024 * 1024 * 5,maxRequestSize = 1024 * 1024 * 10)
@WebServlet("/Registrazione")
public class ServletRegistrazione extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String stato = request.getParameter("stato");
		String terminiCondizioni = request.getParameter("termAndCond");
		
		RequestDispatcher disp;
		if(username==null || password==null || email==null || terminiCondizioni==null || (!Pattern.matches(regexUsername, username)) || (!Pattern.matches(regexPassword, password)) || (!Pattern.matches(regexEmail, email)) || !terminiCondizioni.equals("Accetto")){
			String errore="paramsErr";
			disp = request.getRequestDispatcher("/signUp.jsp?err="+errore);
			disp.forward(request, response);
		}
		
		if(stato==null || stato.equals(""))
			stato=Utente.getSTATO();
		
		InputStream immagine=null;
		Collection<Part> requestParts = request.getParts();
		for(Part part: requestParts){
			String fileName = extractFileName(part);
			if(fileName!=null && !fileName.equals("")){
				immagine = part.getInputStream();
			}
		}
		
		if(immagine==null){
			immagine = Utente.getIMMAGINE_PROFILO_STREAM();
		}

		Utente nuovoUtente = new Utente();
		nuovoUtente.setUsername(username);
		nuovoUtente.setPassword(password);
		nuovoUtente.setEmail(email);
		nuovoUtente.setStato(stato);
		nuovoUtente.setImmagineProfilo(immagine);
		
		HttpSession session = request.getSession(true);
		session.setAttribute("nuovoUtente", nuovoUtente);
		session.setMaxInactiveInterval(30*60);
		String id = session.getId();
		
		try {
			MailUtility.sendMail(email, id);
		} catch (Exception e) {
			disp = request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema Invio Email Conferma");
			disp.forward(request, response);
		}
		
		disp = request.getRequestDispatcher("/signUpProgress.html");
		disp.forward(request, response);
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
	
	private final static String regexUsername = "^[a-zA-Z]+[0-9]*$";
	private final static String regexPassword = "^\\S*.{6}$";
	private final static String regexEmail = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
	
}
