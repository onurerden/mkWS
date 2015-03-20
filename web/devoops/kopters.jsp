<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>
<!DOCTYPE html>
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
                            cr.getMysqlConnectionString(), "onur", "19861986");

                    Statement statement = connection.createStatement();
                    ResultSet resultset
                            = statement.executeQuery("SELECT * from kopter where active='1' ORDER BY latestTouch DESC");
                %>
                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="#">Kopters</a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="kopters" class="col-xs-8">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Kopters</span>
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
                                <p>Kopters defined in mkWS system listed below:</p>
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Kopter Id</th>
                                            <th>Kopter Name</th>
                                            <th>Kopter UID</th>
                                            <th>Latest Touch</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% while (resultset.next()) {%>
                                        <tr>
                                            <td><%= resultset.getInt("id")%></td>
                                            <td><%= resultset.getString("name")%></td>
                                            <td><%= resultset.getString("UID")%></td>
                                            <td><%= resultset.getTimestamp("latestTouch")%></td>

                                        </tr>
                                        <% }%>

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-4">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Kopters</span>
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
                                <p>Kopters defined in mkWS system listed below:</p>
                            </div>
                        </div>
                    </div>
                </div>
                </body>
                </html>
                <%@ include file="foot.jsp" %> 