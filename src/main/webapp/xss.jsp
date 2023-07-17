<html>
<body>
<h2>Hello XSS World!</h2>
<form action="xss.jsp" method="POST">
First Name : <input type="text" id="fname" name="fname"><br><br>
Last Name : <input type="text" id="lastname" name="lname"><br><br>
Email : <input type="text" id="email" name="email"><br><br>
<input type="submit" value="Submit"><br><br>
<% if("POST".equalsIgnoreCase(request.getMethod())){ %>
<br><label for="lastname">Last name: <%= request.getParameter("lname")%> </label><br><br>
Length of Input parameters : <br><br>
first name : <%= request.getParameter("fname").length()%><br><br>
Last name : <%= request.getParameter("lname").length()%><br><br>
Email : <%= request.getParameter("email").length()%><br><br>
<%String test1 =  request.getParameter("fname") + request.getParameter("email").length() + "randomtext";%>
Printing random text length : <%= test1.length()%><br><br>
<%Thread.sleep(1000);}%>
</form> 
</body>
</html>