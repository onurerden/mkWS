<%-- 
    Document   : newpage
    Created on : 12.Eyl.2014, 09:32:16
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<%@ include file="databaseconnection.jsp" %> 

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
</head>
<body>
    <h1>Hello World!</h1>
    <c:set var="dene" value="${param['hisse']}"/>
    <c:out value="IPAddress: ${pageContext.request.remoteAddr}"/>


<div id="deneme" style="width:600px;height:300px"></div>

<script src="js/jquery-1.11.0.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="js/plugins/metisMenu/metisMenu.min.js"></script>

<script src="js/sb-admin-2.js"></script>
<!-- Flot Plugin -->
<script src="js/plugins/flot/excanvas.min.js"></script>
<script src="js/plugins/flot/jquery.flot.js"></script>
<script src="js/plugins/flot/jquery.flot.pie.js"></script>
<script src="js/plugins/flot/jquery.flot.resize.js"></script>
<script src="js/plugins/flot/jquery.flot.tooltip.min.js"></script>

<script src="js/plugins/flot/jquery.flot.time.js"></script>
<script src="js/plugins/flot/date.js"></script>

<script>
    $(document).ready(function() {

        timezoneJS.timezone.zoneFileBasePath = "./tz";
        timezoneJS.timezone.defaultZoneFile = [];
        timezoneJS.timezone.init({async: false});


        $.plot($("#deneme"), [[
                [1410961692000, 6.00],
                [1410962120000, 6.09]
            ]],
                {
                    xaxis: {
                        mode: "time",
                        timezone: "America/Detroit"}
                });
    });

</script>
</body>


</html>
