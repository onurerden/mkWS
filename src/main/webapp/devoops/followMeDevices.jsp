<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page import="mkws.Credentials"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<c:set var="selectedMenu" value="followme"/>
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
                    
                    String isAdminQuery = "";
                    boolean isAdmin = Boolean.parseBoolean(session.getAttribute("isAdmin").toString());
                    if(!isAdmin){
                        isAdminQuery = "WHERE userId = " + session.getAttribute("id").toString() + " ";
                    }
                    ResultSet resultset
                            = statement.executeQuery("SELECT * FROM `followme` AS f INNER JOIN followmedevices "
                                    + "ON f.followMeDeviceId = followmedevices.id "
                                    + isAdminQuery 
                                    + "GROUP BY f.followMeDeviceId ORDER BY `time` DESC");%>


                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="#">FollowMeDevices</a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="kopters" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>FollowMe</span>
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
                                <p>FollowMe Devices defined in mkWS system listed below:</p>
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>FollowMe Device Id</th>
                                                <th>FollowMe Device Name</th>
                                                <th>FollowMe Device UID</th>
                                                <th>Latest Data</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% while (resultset.next()) {%>
                                            <tr>
                                                <td><%= resultset.getInt("followMeDeviceId")%></td>
                                                <td><%= resultset.getString("name")%></td>
                                                <td><%= resultset.getString("UID")%></td>
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
<% connection.close();%>
<%@ include file="foot.jsp" %> 