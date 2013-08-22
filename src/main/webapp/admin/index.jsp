<%@page import="net.sourceforge.heapmonitor.model.MemoryData"%>
<%
Runtime runTime = Runtime.getRuntime();
if ("POST".equalsIgnoreCase(request.getMethod())) {
	runTime.gc();
} 
MemoryData memoryData = new MemoryData(runTime.maxMemory(), runTime.totalMemory(), runTime.freeMemory());
request.setAttribute("memoryData", memoryData);
%>
<!DOCTYPE HTML>
<html>
<head>
<style type="text/css">
td, th {
	padding: 5px;
	border-bottom: 1px solid black;
	
}
tbody tr:nth-child(odd) {
   background-color: #F9F9F9;
}


table {
 border: 1px solid;

}


section {
	width: 500px;
	margin: 100px auto;
}

.info table {
	margin: 0 auto;
}
.buttons {
	width: 240px;
	margin: 10px auto;
	
}

.buttons form  {
 	width: 100px;
 	margin: 0 10px;
	float: left;
}
.buttons form input[type="submit"] {

	background-color: #5e99c9;
	height: 50px;
	width: 100px;
	font-size: 17px;
	font-weight: bold;
	color: #dddddd;
	margin: 10px auto;
}
</style>

</head>
<body>
<section>
<div class="info">
<div class="table">
<table>
	<thead>
		<tr>
			<th>Usage</th>
			<th>Value</th>
		</tr>
	<thead>
  	<tbody>
		<tr>
			<th>Total Free Memory</td>
			<td>${memoryData.memoryTotalFree}</td>
		</tr>
		<tr>
			<th>Free Memory</td>
			<td>${memoryData.memoryFree}</td>
		</tr>
		<tr>
			<th>Memory Taken</td>
			<td>${memoryData.memoryTaken}</td>
		</tr>
		<tr>
			<th>Maximum Memory</td>
			<td>${memoryData.memoryMaximum}</td>
		</tr>
		<tr>
			<th>Memory In Use</td>
			<td>${memoryData.memoryInUse}</td>
		</tr>
		<tr>
			<th>Percentage Of Memory is Use</td>
			<td>${memoryData.percentageMemoryUsed}</td>
		</tr>
	</tbody>
</table>
</div>
</div>
<div class="buttons">
<form action="" method="post">
	<input value="invokeGC" type="hidden" name="test">
	<input type="submit" value="invoke GC">
</form>
<form action="" metdod="get">
	<input type="submit" value="Refresh">
</form>
</div>
</section>
<script type="text/javascript">


</script>
</body>
</html>






