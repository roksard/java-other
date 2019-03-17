package rx.webuserbase.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Face
 */
public class Face extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Face() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		if(login == null)
			login = "";
		if(password == null)
			password = "";
		String user1 = "admin";
		String user1pw = "pw";
		if(login.equals(user1) && password.equals(user1pw))
			session.setAttribute("user", "admin");
		Object userSess = session.getAttribute("user");
		if(userSess == null || userSess == "") {
			out.write("<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" + 
					"<meta charset=\"UTF-8\">\r\n" + 
					"<title>Home</title>\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"	<form method=\"post\" action=\"face\">\r\n" + 
					"		Login:<br /> login<input type=\"text\" name=\"login\"> \r\n" + 
					"		password<input type=\"text\" name=\"password\">\r\n" + 
					"		<input type=\"submit\" value=\"Sign in\"> <br/>\r\n" + 
					"		Don't have an account yet? \r\n" + 
					"		<a href=\"register\">Register</a>\r\n" + 
					"	</form>\r\n" + 
					"</body>\r\n" + 
					"</html>");
		} else {
			out.write("<h2>Welcome to user board!</h2>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
