<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page import="mkws.Credentials"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<% Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); %>
<% 
    Credentials cr = new Credentials();
            Connection connection = DriverManager.getConnection(
                cr.getMysqlConnectionString(),cr.dbUserName,cr.dbPassword);

            Statement statement = connection.createStatement() ;
            ResultSet resultset = 
                statement.executeQuery("SELECT * from kopter where active='1' ORDER BY latestTouch DESC") ; 
        %>
<div class="row">
	<div id="breadcrumb" class="col-xs-12">
		<ol class="breadcrumb">
			<li><a href="index.html">Home</a></li>
			<li><a href="#">Kopters</a></li>
		</ol>
	</div>
</div>
<div class="row">
    <div id="kopters" class="col-xs-12">
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
                                            <% while(resultset.next()){ %>
						<tr>
							<td><%= resultset.getInt(1) %></td>
							<td><%= resultset.getString(3) %></td>
							<td><%= resultset.getString(2) %></td>
							<td><%= resultset.getTimestamp(5) %></td>

						</tr>
                                                <% }%>
						
					</tbody>
				</table>
			</div>
    </div>
    </div>
</div>
