<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="javax.xml.ws.RequestWrapper"%>
<%@page import="rx.webindexer.dao.*"%>
<%@page import="rx.webapp.Util"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="webapp-style.css">
<meta charset="UTF-8">
<title>Регистрация в webindexer</title>
<style>
#intro {
	margin-left: auto;
	margin-right: auto;
	text-align: center;
	line-height: 1.2em;
	width: 30em;
}
</style>
</head>
<body>
	<%
		String msg = (String) session.getAttribute("msg");
		if (msg != null) {
			response.getWriter().print("<p align=\"center\">" + msg + "</p>");
			session.setAttribute("msg", null);
		}
	%>
	<p>
		<a href="login.jsp">Назад</a>
	</p>
	<p id="intro">
		<b>Зарегистрировать учетную запись.</b></br> Пожалуйста придумайте логин и
		пароль для вашего аккаунта.</br>
	</p>

	<form action="register.jsp" method="post">
		<table id="login-table" align="center">
			<tr>
				<td>Логин:</td>
				<td><input type="text" name="login"></td>
			</tr>
			<tr>
				<td>Пароль:</td>
				<td><input type="password" name="password"></td>
			</tr>
			<tr>
				<td>Подтвердите пароль:</td>
				<td><input type="password" name="password2"></td>
			</tr>
			<tr>
				<td align="right" colspan="2"><input type="submit"
					value="Зарегистрировать"></td>
			</tr>
		</table>
	</form>
	<%
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		if (login == null)
			login = "";
		if (password == null)
			password = "";
		if (password2 == null)
			password2 = "";
		if (!login.equals("")) {
			if (password.equals("") || password2.equals("")) {
				session.setAttribute("msg", "Вы не заполнили одно или несколько полей. Для успешной регист"
						+ "рации Вам необходимо заполнить все поля.");
				response.sendRedirect("register.jsp");
			} else {
				if (!password.equals(password2)) {
					session.setAttribute("msg",
							"Введенные пароли не совпадают. Пожалуйста вводите один и тот же" + " пароль.");
					response.sendRedirect("register.jsp");
				} else {
					User newOne = new User(login, password);
					Users.addUser(newOne);
					Users.saveToExternalDefault();
				}
			}
		}
	%>
</body>
</html>