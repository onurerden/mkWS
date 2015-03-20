<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>

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
                            = statement.executeQuery("SELECT followme.routeId, followmedevices.name, route.time FROM  followme "
                                    + "INNER JOIN followmedevices ON followme.followMeDeviceId = followmedevices.id "
                                    + "INNER JOIN route ON followme.routeId = route.id "
                                    + "GROUP BY followme.routeId "
                                    + "ORDER BY time DESC");
                %>
                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="#">Routes</a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="kopters" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Routes</span>
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
                                <p>Routes defined in mkWS system listed below:</p>
                                <!--   <a href ="#" onclick="load_home()"> HOME </a> </div>
               <script>
                                   function load_home(){
   document.getElementById("content").innerHTML='<object type="text/html" data="mkWS/getFollowMeOnMap.jsp?routeId=171" ></object>';
   }
           </script> -->
                                <table class="table">

                                    <thead>
                                        <tr>
                                            <th>Route Id</th>
                                            <th>Device Name</th>
                                            <th>DateTime</th>

                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% while (resultset.next()) {%>
                                        <tr>
                                            <td><a href="getFollowMeOnMap.jsp?routeId=<%= resultset.getInt("routeId")%>">
                                                    <%= resultset.getInt("routeId")%></a></td>
                                            <td><%= resultset.getString("name")%></td>
                                            <td><%= resultset.getTimestamp("time")%></td>


                                        </tr>
                                        <% }%>

                                    </tbody>
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