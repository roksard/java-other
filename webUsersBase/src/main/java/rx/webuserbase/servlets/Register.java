package rx.webuserbase.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rx.webuserbase.User;
import rx.webuserbase.Users;

/**
 * Servlet implementation class Register
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
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
		String password2 = request.getParameter("password2");
		if(login == null)
			return;
		if(password == null)
			return;
		if(password2 == null)
			password2 = "";
		PrintWriter out = response.getWriter();
		if(!password.equals(password2)) {
			out.write("Error: passwords are not equal.");
			return;
		}
		
		response.sendRedirect("index.jsp");
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		User newUser = new User(login, password);
		if(!Users.map.containsKey(login)) {
			Users.map.put(login, newUser);
			out.write("Successfully added user: " + newUser);
			response.sendRedirect("login.html");
		} else {
			out.write("User " + login + " already exists.");
		}
	}

}
