<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="rx.webapp.*"%>
<%@page import="rx.webindexer.service.*"%>
<%@page import="rx.webindexer.dao.*"%>
<%@page import="java.io.IOException" %>
<%@page import="java.util.LinkedList" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="webapp-style.css">
<meta charset="UTF-8">

<title>Список сайтов</title>
<script>
function addPage() {
  var url = prompt("Введите адрес страницы:", "http://");
  if (url != null && url != "") {
	  var doIndex = confirm("Проиндексировать добавленную страницу?");
	  var pass = "?add=" + url;
	  if(doIndex) 
		 	pass = pass + "&doindex=true";
		window.location.href = pass;
  }
  document.getElementById("demo").innerHTML = txt;
}
</script>

<style>
	th {
		text-align: left;
	}
</style>

</head>
<% 
	if(!Util.isLogged(request)) {
		response.sendRedirect("login.jsp");
		return;
	}
	String user = (String)session.getAttribute("user");
	String msg = (String)session.getAttribute("message");
	if(msg != null && msg != "")  //если было какое то сообщение 
		session.setAttribute("message", null); //удалим его из сессии, чтобы оно не показывалось после обновления страницы
%>
<body link="#555555" vlink="#555555" alink="#aaaaaa">
	<% if(msg != null) { %>
		<div align="center"><%=msg %></div>
	<%} %>
	<table id="welcome">
		<tr>
			<td>Добро пожаловать, <%=user %></td>
			<td align="center" id="logout"><a href="?logout=true">Выход</a></td>
		</tr>
	</table>
	<% 
		LinkedList<StatsUnit> pagesStats = StatsStorage.getWebStatsUser(user);
	%>
	<table id="site-list">
		<tr>
			<th align="center" colspan="4">Список сайтов</th>
			<th style="text-align:right" id="button"><input value="Добавить" onclick="addPage()" type="button"></th> 
		</tr>
		<% if(pagesStats.size() == 0) { %>
			<tr>
				<td align="center" colspan=5>нет добавленных сайтов</td>
			</tr>
		<% } else { %>
		<tr id="header">
			<th style="width:50%;">Адрес</th>
			<th colspan="2">Проиндексирован</th>
			<th colspan="2">Действия</th>
		</tr>	
		<%
			int i = 0;
			for(StatsUnit stats : pagesStats) { %>
		<tr>
			<td><%=stats.getName() %></td>
			<% if(stats.isCalculated()) { %>
				<td align="center">✓</td>
				<td id="button-cell"><a href="stats.jsp?id=<%=i%>">Статистика</a></td>
			<%} else {%>
				<td> ‒ </td>
				<td id="button-cell"> - </td>
			<%} %>
				<td id="button-cell"><a href="?index=<%=i%>">Индексировать</a></td>
			<td id="button-cell"><a href="?remove=<%=i%>">Удалить</a></td>
		</tr>
		<% 
				i++;	
			} %>
		<% } //if empty else%>
	</table>

	<%
		
		String logout = request.getParameter("logout"); 
		if(logout != null && logout.equals("true")) {
			session.setAttribute("user", null);
			response.sendRedirect("login.jsp");
		}		
		
		String index = request.getParameter("index");
		if(index != null) {			
			int id = Integer.parseInt(index);
			String url = StatsStorage.getWebStatsUser(user).get(id).getName();
			StatsUnit stats = null;
			try {
				stats = Service.calcStats( Service.parseHtml( Service.loadPage( url)));
				stats.setName(url);
				StatsStorage.getWebStatsUser(user).set(id, stats);
			} catch(Exception e) {
				session.setAttribute("message", "Не удалось проиндексировать страницу " + url);
			}
			StatsStorage.saveToExternalDefault();
			response.sendRedirect("mainpanel.jsp"); //refresh page
		}
		
		String remove = request.getParameter("remove");
		if(remove != null) {
			int id = Integer.parseInt(remove);
			StatsStorage.getWebStatsUser(user).remove(id);
			StatsStorage.saveToExternalDefault();
			response.sendRedirect("mainpanel.jsp"); //refresh page
		}
		
		String add = request.getParameter("add");
		if(add != null) { //добавить страницу
			String doIndex = request.getParameter("doindex");
			StatsStorage.addWebStatsUser(user, new StatsUnit(add));
			StatsStorage.saveToExternalDefault();
			if(doIndex != null && doIndex.equals("true"))
				response.sendRedirect("?index=" + (StatsStorage.getWebStatsUser(user).size()-1));
			else
				response.sendRedirect("mainpanel.jsp"); //refresh page
			
		}
		
	 %>
</body>
</html>