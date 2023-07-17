<%--
  Created by IntelliJ IDEA.
  User: Ramesh Mani
  Date: 10/22/2021
  Time: 1:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>File Upload</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<form method="POST" action="upload" enctype="multipart/form-data" >
    File:
    <input type="file" name="file" id="file" /> <br/>
    Destination:
    <input type="text" value="/tmp" name="destination"/>
    </br>
    <input type="submit" value="Upload" name="upload" id="upload" />
</form>
</body>
</html>
