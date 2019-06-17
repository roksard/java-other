<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="javax.xml.ws.RequestWrapper"%>
<%@page import="rx.webindexer.dao.*"%>
<%@page import="rx.webapp.Util"%>
<%@page import="java.sql.SQLException"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>WebIndexer</title>
<link rel="stylesheet" type="text/css" href="webapp-style.css">
</head>
<body>
	<p>
		<%=session.getAttribute("message") %>
	</p>
	<a href="javascript:history.back()">Назад</a>
</body>
</html>

<% 
	session.setAttribute("message", "");
%>