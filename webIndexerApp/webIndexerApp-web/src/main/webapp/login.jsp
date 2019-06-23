<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="javax.xml.ws.RequestWrapper"%>
<%@page import="rx.webindexer.dao.*"%>
<%@page import="rx.webapp.Util"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.io.*"%>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Collections" %>
<%@page import="rx.webindexer.security.Hasher"%>
<%@page import="java.util.Arrays"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="webapp-style.css">
<meta charset="UTF-8">
<title>Вход</title>
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
	<p id="intro">
		<b>Приложение для индексации веб-страниц webindexer.</b></br> Пожалуйста
		выполните вход, либо </br> <a href="register.jsp">пройдите быструю
			регистрацию</a>
	</p>

	<form action="login.jsp" method="post">
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
				<td align="right" colspan="2"><input type="submit" value="Вход"></td>
			</tr>
			<!-- <tr id="logintip">
				<td align="right" colspan="2">логин: admin / пароль: password</td>
			</tr>  -->
		</table>
	</form>
	<%
		if (Util.isLogged(request)) {
			response.sendRedirect("mainpanel.jsp");
			return;
		}
		String login = request.getParameter("login");
		String password = request.getParameter("password");

		if (login == null)
			login = "";
		if (password == null)
			password = "";
		if (login == "" || password == "")
			return;
		Keeper keeper = (Keeper) session.getAttribute("keeper");
		if (keeper == null) {
			keeper = new Keeper();
			keeper.establishConnection();
			session.setAttribute("keeper", keeper);
		}
		try {
			byte[] hashRaw = keeper.getUserPassword(login);
			byte[] hash = Hasher.extractHash(hashRaw);
			byte[] salt = Hasher.extractSalt(hashRaw);
			byte[] hashUser = Hasher.hashPassword(password, salt);
			if (Arrays.equals(hash, hashUser)) {
				session.setAttribute("user", new User(login, "")); //объект текущего пользователя будет храниться в сессии
				
				//загружаем статистику веб страниц: 
				List<StatsUnit> pagesStats = (List<StatsUnit>)keeper.getUserStats(login);
				if (pagesStats == null) {
					pagesStats = new ArrayList<StatsUnit>();

				}
				pagesStats = Collections.synchronizedList(pagesStats);
				session.setAttribute("stats", pagesStats);
				
				response.sendRedirect("login.jsp");
			} else {
				if (login != "" || password != "") {
					session.setAttribute("message", "Неверный логин или пароль. Попробуйте снова.");
					response.sendRedirect("message.jsp");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof SQLException) {
				SQLException e1 = (SQLException) e;
				System.out.println("state: " + e1.getSQLState());
				session.setAttribute("message", "Не удалось подключиться к БД: " + e1.toString());
				response.sendRedirect("message.jsp");
			}
		} finally {
			keeper.closeConnection();
		}
	%>
</body>
</html>