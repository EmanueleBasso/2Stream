package servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Amministratore;
import beanDAO.AmministratoreDAO;
import beanDAO.ServiziAmministratoreDCS;
import connection.DriverManagerConnectionPool;
import utility.OraUtility;

@WebServlet("/AccessoAdmin")
public class ServletAccessoAdmin extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		RequestDispatcher disp;
		if (username == null || password == null || (!Pattern.matches(regexUsername, username)) || (!Pattern.matches(regexPassword, password))) {
			String errore = "paramsErr";
			disp = request.getRequestDispatcher("/signInAdmin.jsp?err=" + errore);
			disp.forward(request, response);
		}
		
		Amministratore admin = null;
		try {
			admin = AmministratoreDAO.doRetrieveByUsernamePassword(username,password);
			
			if (admin != null) {				
				HttpSession session = request.getSession(true);
				session.setMaxInactiveInterval(10*60);			
				session.setAttribute("admin",admin);
			}
		} catch (SQLException e) {
			request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema a interfacciarsi col Database!").forward(request, response);
		}

		if (admin != null) {
			try{
				int iscritti = ServiziAmministratoreDCS.numeroIscritti();
				int chat = ServiziAmministratoreDCS.numeroChat();
				int testuali = ServiziAmministratoreDCS.numeroMexTestuali();
				int multimediali = ServiziAmministratoreDCS.numeroMexMultimediali();
				String occupazione = ServiziAmministratoreDCS.utilizzoDatbase();
				int quota = ServiziAmministratoreDCS.QUOTA;
				
				long data = OraUtility.convertToGMT(new GregorianCalendar());
				Date dataOggi = new Date(data);
				int iscrittiOggi = ServiziAmministratoreDCS.utentiIscrittiInData(dataOggi);
				
				ServletContext contesto =request.getServletContext();
				
				long autenticazioniErrate = (long) contesto.getAttribute("accessiErrati");
				long sessioniInvalidate = (long) contesto.getAttribute("sessioniInvalidate");
				
				int connessioni = DriverManagerConnectionPool.getNumeroConnessioniOccupate();
				int maxConnessioni = ServiziAmministratoreDCS.MAX_CONNESSIONI;

				HashSet<String> sessioni = (HashSet<String>) contesto.getAttribute("sessioni");
				int online = sessioni.size();
				
				int maxOnline = (int) contesto.getAttribute("maxAttivi");
				
				request.setAttribute("iscritti",iscritti);
				request.setAttribute("chat",chat);
				request.setAttribute("testuali",testuali);
				request.setAttribute("multimediali",multimediali);
				request.setAttribute("totaleMex",testuali + multimediali);
				request.setAttribute("occupazione",occupazione);
				request.setAttribute("quota",quota);
				request.setAttribute("iscrittiOggi",iscrittiOggi);
				request.setAttribute("autenticazioniErrate",autenticazioniErrate);
				request.setAttribute("sessioniInvalidate",sessioniInvalidate);
				request.setAttribute("connessioni",connessioni);
				request.setAttribute("maxConnessioni",maxConnessioni);
				request.setAttribute("online",online);
				request.setAttribute("maxOnline",maxOnline);
				request.setAttribute("username",admin.getUsername());
				
				disp = request.getRequestDispatcher("/adminPanel.jsp");
				disp.forward(request, response);
			}catch (SQLException e) {
				request.getRequestDispatcher("/ErrorPage.jsp?ErrorCode=Problema a interfacciarsi col Database!").forward(request, response);
			}
		} else {
			String errore = "notReg";
			disp = request.getRequestDispatcher("/signInAdmin.jsp?err=" + errore);
			disp.forward(request, response);
		}
	}

	private final static String regexUsername = "^[a-zA-Z]+[0-9]*$";
	private final static String regexPassword = "^\\S*.{6}$";
}
