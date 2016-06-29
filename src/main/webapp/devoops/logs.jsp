<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="selectedMenu" value="logs"/>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp" %> 

    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">

                <%// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); %>
                <% Class.forName("com.mysql.jdbc.Driver").newInstance(); %>
                <% 
                    Credentials cr = new Credentials();
                    Connection connection = DriverManager.getConnection(
                            //cr.getMysqlConnectionString(),cr.getDbUserName(),cr.getDbPassword());
                            cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());

                    Statement statement = connection.createStatement();
                    ResultSet resultset
                            = statement.executeQuery("SELECT * from logs ORDER BY ID DESC LIMIT 100");
                %>
                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="#">Logs</a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="routes" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Logs</span>
                                </div>
                                <div class="box-icons">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
                                    <a class="expand-link">
                                        <i class="fa fa-expand"></i>
                                    </a>
                                    <a class="close-link">
                                        <i class="fa fa-times"></i>
                                    </a>
                                </div>
                                <div class="no-move"></div>
                            </div>
                            <div class="box-content">
                                <p>System Logs:</p>
                                <!--   <a href ="#" onclick="load_home()"> HOME </a> </div>
               <script>
                                   function load_home(){
   document.getElementById("content").innerHTML='<object type="text/html" data="mkWS/getFollowMeOnMap.jsp?routeId=171" ></object>';
   }
           </script> -->
                                <table class="table">

                                    <thead>
                                        <tr>
                                            <th>Log Id</th>
                                            <th>log Level</th>
                                            <th>Kopter Device Id</th>
                                            <th>FollowMe Device Id</th>
                                            <th>Log Message</th>
                                            <th>Time</th>

                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% while (resultset.next()) {%>
                                        <tr>
                                            <td>
                                            <% 

                                            out.println(resultset.getInt("id")+"</td>");
                                            out.println("<td>" +resultset.getInt("logLevel")+"</td>");
                                            out.println("<td>" +resultset.getInt("kopterId")+"</td>");
                                            out.println("<td>" +resultset.getInt("followMeDeviceId")+"</td>");
                                            out.println("<td>");
                                            switch (resultset.getInt("logLevel")) {
                                                    case 1:
                                                        out.println("<p style=\"color:#CC0000\">");
                                                        break;
                                                    case 2:
                                                        out.println("<p style=\"color:#00CC00\">");
                                                        break;
                                                    default:
                                                        out.println("<p style=\"color:#000000\">");
                                                        break;
                                                }
                                            out.println(resultset.getString("logMessage")+"</p></td>");
                                            
                                            out.println("<td>" +resultset.getTimestamp("time")+"</td>");
                                            
                                            out.println("</tr>");

                                         }%>

                                    </tbody>
                                    <% connection.close();%>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </div>
</body>
</html>    
<%@ include file="foot.jsp" %> 