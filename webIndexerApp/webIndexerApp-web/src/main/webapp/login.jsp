<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="javax.xml.ws.RequestWrapper"%>
<%@page import="rx.webindexer.dao.*"%>
<%@page import="rx.webapp.Util"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<style>
	#tooltip {
		font-size: 80%;
		color: #999999;
	}
</style>
<title>Вход</title>
</head>
<body>
	<form action="login.jsp" method="post">
		<table bgcolor="#ffecb3" align="center">
			<tr>
				<td>Логин:</td>
				<td><input type="text" name="login"></td>
			</tr>
			<tr>
				<td>Пароль:</td>
				<td><input type="password" name="password"></td>
			</tr>
			<tr>
				<td align="right" colspan="2"><input type="submit"
					value="Вход"></td>
			</tr>
			<tr id="tooltip">
				<td align="right" colspan="2">логин: admin / пароль: password</td>
			</tr>
		</table>
	</form>
	<% 
		if(Util.isLogged(request)) {
			response.sendRedirect("mainpanel.jsp");
			//request.getRequestDispatcher("mainpanel.jsp").forward(request, response);
			return; 
		}
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		if(login == null)
			login = "";
		if(password == null)
			password = "";
		//response.getWriter().println(login + password);
		User account = Users.getUser(login);
		if(account != null && account.getPassword().equals(password)) {
			session.setAttribute("user", login); //define a logged in user
			response.sendRedirect("login.jsp");
		} else {
			if(login != "" || password != "")
				response.sendRedirect("error_login.html");
		}
	%>
</body>
</html>