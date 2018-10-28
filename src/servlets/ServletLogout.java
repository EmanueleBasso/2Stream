package servlets;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Logout")
public class ServletLogout extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		ServletContext context = session.getServletContext();
		synchronized (context) {
			long sessioniInvalidate = (long) context.getAttribute("sessioniInvalidate");
			sessioniInvalidate = sessioniInvalidate - 1;
			context.setAttribute("sessioniInvalidate", sessioniInvalidate);
		}
		
		session.invalidate();
		request.getRequestDispatcher("/signIn.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
