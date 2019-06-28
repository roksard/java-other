<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="rx.webindexer.dao.*"%>    
<%@page import="rx.webapp.Util"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Locale"%>
<%@page import="static rx.webindexer.dao.StatsUnit.LetterType.cyrillic"%>
<%@page import="static rx.webindexer.dao.StatsUnit.LetterType.latin"%>
<%@page import="java.util.List" %>

<% 
	if(!Util.isLogged(request)) {
		response.sendRedirect("login.jsp");
		return;
	}	
	String idpass = request.getParameter("id");
	StatsUnit stats = null;
	
	List<StatsUnit> pagesStats = (List<StatsUnit>) session.getAttribute("stats");
		
	if(idpass != null) {			
		int id = Integer.parseInt(idpass);
		synchronized (pagesStats) {
			stats = pagesStats.get(id);
		}
	}
	
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="webapp-style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Статистика</title>
</head>

<script src="lib/Chart.bundle.js"></script>
<script>
window.onload = function() {
	
	<% 
		if(stats != null) {
			//инициализация js переменных - кириллица (надписи и данные)
			StringBuilder cyrLabels = new StringBuilder();
			StringBuilder cyrDataValues = new StringBuilder();
			for(Map.Entry<Character,Float> entry : stats.getLetterFrequency().get(cyrillic.ordinal()).entrySet()) {
				cyrLabels.append("'" + entry.getKey() + "',");
				cyrDataValues.append(String.format(Locale.ROOT, "%.2f", entry.getValue()) + ",");
			}
			cyrLabels.replace(cyrLabels.length()-1, cyrLabels.length(), ""); //удалить последнюю запятую
			cyrDataValues.replace(cyrDataValues.length()-1, cyrDataValues.length(), ""); //удалить последнюю запятую
			out.println("var cyrLabels = ["+ cyrLabels.toString() +"];");
			out.println("var cyrFreqData = ["+ cyrDataValues.toString() +"];");
			
			//инициализация js переменных - латиница (надписи и данные)
			StringBuilder latLabels = new StringBuilder();
			StringBuilder latDataValues = new StringBuilder();
			for(Map.Entry<Character,Float> entry : stats.getLetterFrequency().get(latin.ordinal()).entrySet()) {
				latLabels.append("'" + entry.getKey() + "',");
				latDataValues.append(String.format(Locale.ROOT, "%.2f", entry.getValue()) + ",");
			}
			latLabels.replace(latLabels.length()-1, latLabels.length(), ""); //удалить последнюю запятую
			latDataValues.replace(latDataValues.length()-1, latDataValues.length(), ""); //удалить последнюю запятую
			out.println("var latLabels = ["+ latLabels.toString() +"];");
			out.println("var latFreqData = ["+ latDataValues.toString() +"];");
	%>
	
	var cyrDataSet = {
  	label: "Частота вхождения букв - кириллица (%)",
  	data: cyrFreqData,
  	backgroundColor: "rgba(255,244,222,0.9)",
  	borderColor: "rgba(51,34,0,0.2)",
  	borderWidth: 1,
  	};
  	
 	var latDataSet = {
  	label: "Частота вхождения букв - латиница (%)",
  	data: latFreqData,
  	backgroundColor: "rgba(255,244,222,0.9)",
  	borderColor: "rgba(51,34,0,0.2)",
  	borderWidth: 1,
  	
  	};
	
 	var cyrCanvas = document.getElementById("cyrCanvas");
  var latCanvas = document.getElementById("latCanvas");
  
  var options = {
             scales: {
                 yAxes: [{
                     display: true,
                     gridLines: {
                         color: "rgb(210,210,210)"
                     },
                     ticks: {
                         max: 100,
                         min: 0,
                         stepSize: 20,
                         beginAtZero: true,
                         padding: 0,
                         callback: function(value, index, values) {
                             return value;
                         }
                     },
                  
                 }]
             }
         };
 
	var cyrChart = new Chart(cyrCanvas, {
  	type: 'bar',
  	data: {
    	labels: cyrLabels,
    	datasets: [cyrDataSet],
  	},
	});
	var latChart = new Chart(latCanvas, {
  	type: 'bar',
  	data: {
    	labels: latLabels,
    	datasets: [latDataSet]
  	},
		});
	<%
		} //if (stats != null)
	%>
	
};
</script>
<style>
	#td-stats {
		width: 33%;
		text-align: center;
	}
</style>
<body>
		<a href="mainpanel.jsp">К списку сайтов</a>
		<% if(stats != null) {  %>
			<table style="width:60%;" align="center">			
				<tr> 
					<th colspan="3"><%=stats.getName() %></th>
				</tr><tr>
					<td id="td-stats"><b>Количество слов:</b> <%=stats.getWordsCount() %></td>
					<td id="td-stats"><b>Уникальных слов:</b> <%=String.format("%.2f", stats.getUniqueWordsPercent())+"%"  %></td>
					<td id="td-stats"><b>Слов не кириллицы:</b> <%=String.format("%.2f", stats.getNonCyrillicWordsPercent())+"%"%></td>
				</tr>
				<tr>
					<td style="vertical-align: text-top" id="td-stats"><b>Часто используемые слова:</b></td><td id="word-list" colspan="2">
				<%
					for(Map.Entry<String, Integer> entry : stats.getTopWords().entrySet())  
						out.print(entry.getKey() + " ");
				%>
				</td>
			</table>
			<canvas id="cyrCanvas" width="600" height="80"></canvas><br>
			<canvas id="latCanvas" width="600" height="80"></canvas>
		<% } else {  %>
			<div>нет статистики для отображения</div>
		<% } %>
</body>
</html>