<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="selectedMenu" value="users"/>
<html>
    <%@ include file="head.jsp" %> 

    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Delete User</h4>
                </div>
                <div class="modal-body" id="modalBody">
                  
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-danger" onclick=deleteUser()>Delete</button>
                </div>
            </div>
        </div>
    </div>

    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">

                <%// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); %>
                <% Class.forName("com.mysql.jdbc.Driver").newInstance(); %>
                <% 
                    Credentials cr = new Credentials();
                    Connection connection = DriverManager.getConnection(
                            cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
                    //   cr.getMysqlConnectionString(),  "onur", "19861986");

                    Statement statement = connection.createStatement();
                    ResultSet resultset
                            = statement.executeQuery("SELECT * from members");
                %>
                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="#">Users</a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="results" class="col-xs-12">
                        <c:choose>
                            <c:when test="${param.success!=null}">
                                <div class="alert alert-success" class="col-xs-12">
                                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                    <strong>Success!</strong> <% out.print(request.getParameter("success"));%>

                                </div> 

                            </c:when>
                        </c:choose>
                        <c:choose>
                            <c:when test="${param.error!=null}">
                                <div class="alert alert-danger" class="col-xs-12">
                                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                    <strong>Error!</strong> <% out.print(request.getParameter("error"));%>

                                </div> 

                            </c:when>
                        </c:choose>
                    </div>
                </div>

                <div class="row">
                    <div id="kopters" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Users</span>
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
                              

                                <div id="tabs">
                                    <ul>
                                        <li><a href="#tabs-1">Users</a></li>
                                        <li><a href="#tabs-2">Add New User</a></li>

                                    </ul>

                                    <div id="tabs-1">
                                        <p style="font-size:14px">Users defined on the mkWS system are given below:</p>

                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th>User Id</th>
                                                    <th>User Name</th>
                                                    <th>User Surname</th>
                                                    <th>User Email</th>
                                                    <th>Login Name</th>
                                                    <th>Register Date</th>
                                                    <th>is Admin?</th>
                                                </tr>
                                            </thead>
                                            <tbody style="font-size:14px">
                                                <% while (resultset.next()) {%>
                                                <tr>
                                                    <td><%= resultset.getInt("id")%></td>
                                                    <td><%= resultset.getString("first_name")%></td>
                                                    <td><%= resultset.getString("last_name")%></td>
                                                    <td><%= resultset.getString("email")%></td>
                                                    <td><%= resultset.getString("uname")%></td>
                                                    <td><%= resultset.getTimestamp("regdate")%></td>
                                                    <td><%= resultset.getBoolean("isAdmin")%></td>
                                                    <td>
                                                        <% if (Integer.valueOf(session.getAttribute("id").toString()) != resultset.getInt("id")) {%>
                                                        <button class="btn btn-danger"
                                                                onClick="askToBeDeleted(<%= resultset.getInt("id")%>)" style="font-size:12px">
                                                            Delete
                                                        </button>
                                                        <% } %>
                                                    </td>

                                                </tr>
                                                <% }%>

                                            </tbody>
                                            <% connection.close();%>
                                        </table>
                                    </div>

                                    <div id="tabs-2" >

                                        <form method="post" action="addUser.jsp">
                                            <div class="form-group">
                                                <label class="control-label">First Name</label>
                                                <input type="text" class="form-control" placeholder="User Name" name="first_name">
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">Last Name</label>
                                                <input type="text" class="form-control" placeholder="User Last Name" name="last_name">
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">User Email</label>
                                                <input type="text" class="form-control" placeholder="User Email" name="email">


                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">User Login Name</label>
                                                <input type="text" class="form-control" placeholder="User Login Name" name="uname">


                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">Password</label>
                                                <input type="password" class="form-control" placeholder="Password" name="password">
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label">is Admin?</label>
                                                <input type="checkbox" unchecked name="isAdmin">                                              
                                                <div class="text-center">
                                                    <!--		<a href="../index.html" class="btn btn-primary">Sign in</a>-->
                                                    <input type="submit" value="Add User" />
                                                </div>
                                        </form>



                                    </div>
                                </div>

                            </div>

                        </div>

                    </div>
                </div>

            </div>
            </body>
            <%@ include file="foot.jsp" %> 

            <script type="text/javascript">
                $(document).ready(function () {
                    // Create jQuery-UI tabs
                    $("#tabs").tabs();
                });
                var userIdToBeDeleted=-1;

                function deleteUser() {
                    console.log("delete clicked for user: " + userIdToBeDeleted);
                    var link = "../DeleteUser?userId=" + userIdToBeDeleted;
                    console.log(link);
                    $.ajax({url: link, success: function (result) {

                            var info = JSON.parse(result);

                            console.log(info);

                            if (info.result === "success") {
                                window.location.replace("./users.jsp?success=\"User deleted successfully\"");
                            } else {
                                $("#results").html("<div class=\"alert alert-danger\" class=\"col-xs-12\">\n\
        <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a>\n\
<strong>Failed!</strong> User cannot be deleted. " + info.description + "</div>");
                            }
                        }});

                }
                function askToBeDeleted(userId) {
                                        console.log("modal loaded for user: " +userId);
                                        userIdToBeDeleted=userId;
                    $("#modalBody").html("Are you sure you want to delete user with id number: " + userId );
                    $('#myModal').modal('toggle');
                }


            </script>
            </html>
