<%@page import="rx.webapp.Util"%>
<%@page import="rx.webindexer.service.*"%>
<%@page import="java.io.IOException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<style>
table {
	border-style: solid;
	border-color: #bec7d5;
	border-width: 0px;
	width: 800px;
}

td, th {
	border-style: solid;
	border-color: #bec7d5;
	border-width: 0px;
	background-color: #ced7e5;
	padding: 5px;
}

#button-cell {
	text-align: center;
}

#welcome {
	border-style: solid;
	background-color: #bec7d5;
	border-color: #ced7e5;
	border-width: 0px 0px 0px 0px;
	font-weight: bold;
}

th {
	border-style: solid;
	border-color: #bec7d5;
	border-width: 0px 0px 0px 0px;
}
</style>
<title>Список сайтов</title>
</head>
<body link="#555555" vlink="#555555" alink="#aaaaaa">
	
	<div id="welcome">Добро пожаловать, <%=request.getSession().getAttribute("user") %></div>
	<table align="center">
		<tr><th align="center" colspan="5">Добавленные страницы</th> 
		</tr>
		<tr id="header">
			<th align="left">Адрес страницы</th>
			<th colspan="2">Проиндексирована</th>
			<th align="center" colspan="2">Действия</th>
		</tr>
		<% for(int i = 0; i < 3; i++) { %>
		<tr>
			<td>
				https://stackoverflow.com/questions/40171317/how-to-create-a-panel-
				in-html</td>
			<td align="center">‒ or<a href="?stats=<%=i%>">✓</a></td>
			<td id="button-cell"><a href="?stats=<%=i%>">Статистика</a></td>
			<td id="button-cell"><input type="button" value="Индексировать"
				onclick="window.location.href = '?index=<%=i%>'"></td>
			<td id="button-cell"><input type="button" value="X"
				onclick="window.location.href = '?remove=<%=i%>'"></td>
		</tr>
		<% } %>
	</table>

	<%
		if(!Util.isLogged(request)) {
			response.sendRedirect("login.jsp");
			return;
		}
		String logout = request.getParameter("logout"); 
		if(logout != null && logout.equals("true")) {
			System.out.println("logout="+logout);
			session.setAttribute("user", null);
			response.sendRedirect("login.jsp");
		}
		
		String index = request.getParameter("index");
		if(index != null) {
			//TODO stub сделать обработку запроса на переиндексацию сайта
			int id = Integer.parseInt(index);
			//...indexate(id)
			//refresh page
			try {
				Service.indexPage("pff.clan.su");
			} catch(IOException e) {
				response.sendRedirect("error_add.html");
			}
		}
		
		String remove = request.getParameter("remove");
		if(remove != null) {
			//TODO stub сделать обработку запроса на удаление сайта
			int id = Integer.parseInt(remove);
			//...remove(id)
			//refresh page
		}
		
	 %>
</body>
</html>