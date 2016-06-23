<%-- 
    Document   : index
    Created on : 08.Ara.2014, 12:12:26
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="windows-1250"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
    <title>Redirecting...</title>
</head>
<body>
    <h1>MK Portal!</h1>
    <a href="/GetRegisteredData?device=mk">Kopters</a><br>
    <a href="/GetRegisteredData?device=mp">Devices</a><br>
    <a href="/latestRoutes.jsp">Latest Routes</a><br>
    <a href="devoops/index.jsp">devoops</a><br>
    
<script>
    window.location = "devoops";
    </script>    
</body>


</html>
