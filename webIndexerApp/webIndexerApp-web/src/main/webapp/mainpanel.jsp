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
#button {
	font-size: 0.80em;
}
</style>
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

</head>
<% 
	if(!Util.isLogged(request)) {
		response.sendRedirect("login.jsp");
		return;
	}
	String user = (String)request.getSession().getAttribute("user"); 
%>
<body link="#555555" vlink="#555555" alink="#aaaaaa">
	
	<div id="welcome">Добро пожаловать, <%=user %></div>
	<% 
		LinkedList<StatsUnit> pagesStats = StatsStorage.getWebStatsUser(user);
	%>
	<table align="center">
		<tr>
			<th align="center" colspan="4">Список сайтов</th>
			<th align="center" id="button"><input value="Добавить" onclick="addPage()" type="button"></th> 
		</tr>
		<% if(pagesStats.size() == 0) { %>
			<tr>
				<td align="center" colspan=5>нет добавленных сайтов</td>
			</tr>
		<% } else { %>
		<tr id="header">
			<th align="left">Адрес страницы</th>
			<th colspan="2">Проиндексирована</th>
			<th align="center" colspan="2">Действия</th>
		</tr>	
		<%
			int i = 0;
			for(StatsUnit stats : pagesStats) { %>
		<tr>
			<td><%=stats.getName() %></td>
			<% if(stats.isCalculated()) { %>
				<td align="center">✓</td>
				<td id="button-cell"><a href="?stats=<%=i%>">Статистика</a></td>
			<%} else {%>
				<td align="center"> ‒ </td>
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
			System.out.println("logout="+logout);
			session.setAttribute("user", null);
			response.sendRedirect("login.jsp");
		}		
		
		String index = request.getParameter("index");
		if(index != null) {			
			int id = Integer.parseInt(index);
			String url = StatsStorage.getWebStatsUser(user).get(id).getName();
			StatsUnit stats = Service.calcStats( Service.parseHtml( Service.loadPage( url)));
			stats.setName(url);
			StatsStorage.getWebStatsUser(user).set(id, stats);
			response.sendRedirect(""); //refresh page
		}
		
		String remove = request.getParameter("remove");
		if(remove != null) {
			int id = Integer.parseInt(remove);
			StatsStorage.getWebStatsUser(user).remove(id);
			response.sendRedirect("");
		}
		
		String add = request.getParameter("add");
		if(add != null) { //добавить страницу
			String doIndex = request.getParameter("doindex");
			StatsStorage.addWebStatsUser(user, new StatsUnit(add));
			if(doIndex != null && doIndex.equals("true"))
				response.sendRedirect("?index=" + (StatsStorage.getWebStatsUser(user).size()-1));
			else
				response.sendRedirect("");
		}
		
	 %>
</body>
</html>