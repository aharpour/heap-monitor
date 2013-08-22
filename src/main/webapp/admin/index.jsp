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
form {
 display: inline;
}

input[type="submit"] {
	background-color: #5e99c9;
	height: 50px;
	width: 100px;
	font-size: 17px;
	font-weight: bold;
	color: #dddddd;
}

.info div{
	float: left;
}
.buttons {
	clear: both;
	
}
</style>

</head>
<body>
<section class="info">
<div class="memory-usage-chart">
	<canvas id="pie-chart"></canvas>
</div>
<div class="pie-chart">
	<canvas id="pie-chart"></canvas>
</div>

<div class="table">
<table border="1">
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
</section>
<section class="buttons">
<form action="" method="post">
	<input value="invokeGC" type="hidden" name="test">
	<input type="submit" value="invoke GC">
</form>
<form action="" metdod="get">
	<input type="submit" value="Refresh">
</form>
</section>
<script type="text/javascript">


</script>
</body>
</html>






