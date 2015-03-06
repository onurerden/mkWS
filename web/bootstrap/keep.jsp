<%-- 
    Document   : index
    Created on : 06.Haz.2013, 16:39:37
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>



<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>KeepAlive</title>
</head>
<body>
    <h1>KeepAlive</h1>
    <br>
    <br>
    <sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
     url="jdbc:mysql://127.6.27.2:3306/glassfish"
     user="root"  password="xb0VSSWq"/>
    
    <sql:update dataSource="${snapshot}" var="count">
        INSERT INTO `logs`(`logLevel`, `message`, `datetime`) VALUES (0,"KeepAlive Sayfasina Erisildi.",CONVERT_TZ(NOW(),'-05:00','+02:00'));
    </sql:update>
    
    
<sql:query dataSource="${snapshot}" var="result">
SELECT * from diagnosis;
</sql:query>
 
<table border="1" width="50%">
<tr>
   <th>ID</th>
   <th>Feature</th>
   <th>Value</th>
   
</tr>
<c:forEach var="row" items="${result.rows}">
<tr>
   <td><c:out value="${row.id}"/></td>
   <td><c:out value="${row.feature}"/></td>
   <td><c:out value="${row.value}"/></td>
   </tr>
</c:forEach>
</table>
<br>
<br>
<sql:query dataSource="${snapshot}" var="result2">
SELECT COUNT(*) as count from hisseler;
</sql:query>

<c:forEach var="row2" items="${result2.rows}">
    <p>Webservis <c:out value="${row2.count}"/> hisse için çalışıyor.</p>
</c:forEach>
<sql:query dataSource="${snapshot}" var="result4">
SELECT * from config;
</sql:query>
    

    <table>
        <tr>
        <th>Feature</th>
        <th>Value</th>
        </tr>
        <c:forEach var="row4" items="${result4.rows}">
        <tr>
            <td><c:out value="${row4.feature}"/></td>
            <td><c:out value="${row4.value}"/></td>
        </tr>
        </c:forEach>
    </table>
    
    <


</body>
</html>
