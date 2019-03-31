package rx.webuserbase.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import rx.webuserbase.User;
import rx.webuserbase.Users;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		if(login == null)
			login = "";
		if(password == null)
			password = "";
		
		HttpSession session = request.getSession(); 
		Object currentUser = session.getAttribute("user");
		if(login.equals(currentUser)) {
			new Home().doGet(request, response);
		}
		
		PrintWriter out = response.getWriter();
		
		User loginUser = Users.map.get(login);
		
		if(loginUser != null) {//user is identified by login only
			if(loginUser.getPassword().equals(password)) {
				session.setAttribute("user", login);
				session.setMaxInactiveInterval(50);
				doPost(request, response);
				return;
			}
				
		} else {
			out.write("Wrong password, or user " + login + " doesn't exists.");
			out.write("<a href=\"login\">обновить</a>");
		}
	}

}
