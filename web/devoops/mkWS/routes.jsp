<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>
<%// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); %>
<% Class.forName("com.mysql.jdbc.Driver").newInstance(); %>
<% 
    Credentials cr = new Credentials();
            Connection connection = DriverManager.getConnection(
                //cr.getMysqlConnectionString(),cr.getDbUserName(),cr.getDbPassword());
                cr.getMysqlConnectionString(),cr.getDbUserName(),cr.getDbPassword());

            Statement statement = connection.createStatement() ;
            ResultSet resultset = 
                statement.executeQuery("SELECT route.id, followmedevices.name, route.time FROM  `route` "
                        + "INNER JOIN followmedevices ON route.followMeDeviceId = followmedevices.id "
                        + "ORDER BY TİME DESC ") ; 
        %>
<div class="row">
	<div id="breadcrumb" class="col-xs-12">
		<ol class="breadcrumb">
			<li><a href="index.html">Home</a></li>
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
                            <td><%= resultset.getInt("id")%></td>
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
