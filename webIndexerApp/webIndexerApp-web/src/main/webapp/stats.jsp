<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="rx.webindexer.dao.*"%>    
<%@page import="rx.webapp.Util"%>

<% 
	if(!Util.isLogged(request)) {
		response.sendRedirect("login.jsp");
		return;
	}
	String user = (String)request.getSession().getAttribute("user");
	String idpass = request.getParameter("id");
	StatsUnit stats = null;
	if(idpass != null) {			
		int id = Integer.parseInt(idpass);
		try {
			stats = StatsStorage.getWebStatsUser(user).get(id);
		} catch (Exception e) {
			//ничего не делаем, дальше на странице проверим stats на null
		}
	}
	
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Статистика</title>
</head>

<script src="lib/Chart.bundle.js"></script>
<script>
window.onload = function() {
    var popCanvas = document.getElementById("cyrillicLetters");
    alert(popCanvas)
	var densityData = {
  label: 'Density of Planets (kg/m3)',
  data: [5427, 5243, 5514]
};
 
var barChart = new Chart(popCanvas, {
  type: 'bar',
  data: {
    labels: ["Mercury", "Venus", "Earth"],
    datasets: [densityData]
  }
});
	
	

};
	
</script>

<body>
		<table align="center">
			<% if(stats != null) {  %>
				<tr> 
					<td>Адрес страницы:</td><td><%=stats.getName() %>
				</tr>
			<% } else {  %>
				<tr><td>нет статистики для отображения</td></tr>
			<% } %>
		</table>
		<canvas id="popChart" width="600" height="400">asdasd</canvas>
		<canvas id="cyrillicLetters" width="600" height="40"></canvas><br>
		<canvas id="latinLetters" width="600" height="40"></canvas>
</body>
</html>