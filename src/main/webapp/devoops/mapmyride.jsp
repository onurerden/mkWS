<%-- 
    Document   : mapmyride.jsp
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page import="com.google.gson.Gson"%>
<%@page import="mkws.Model.MMRUser"%>
<%@page import="mkws.ServerEngine"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="selectedMenu" value=""/>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp" %> 

    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">


                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="routes.jsp">MapMyRide</a></li>

                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="results" class="col-xs-12">
                        <c:choose>
                            <c:when test="${param.code!=null}">

                                <div class="alert alert-success" class="col-xs-12">
                                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                    <strong>Success!</strong> MapMyRide paired successfully. Key is: 
                                    <% out.print(request.getParameter("code"));
                                        ServerEngine server = new ServerEngine();
                                        server.saveMMRauthorizationCodeForUser(Integer.valueOf(session.getAttribute("id").toString()), request.getParameter("code"));
                                    %>
                                </div>


                            </c:when> 


                        </c:choose>
                        <c:choose>
                            <c:when test="${param.message!=ad}">

                                <div class="alert alert-success" class="col-xs-12">
                                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                    <strong>Success!</strong> MapMyRide account deleted successfully.
                                </div>


                            </c:when>    

                        </c:choose>
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>MapMyRide</span>
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

                                <div id="authCode">                           




                                    <jsp:useBean
                                        id = "userBean" class= "mkws.webbeans.GetMMRUserInfo">
                                    </jsp:useBean>
                                    <jsp:setProperty name="userBean" property = "userId" value="${sessionScope.id}"/>


                                    <c:choose>
                                        <c:when test="${userBean.email!=null}">

                                            <table>
                                                <tr>
                                                    <td rowspan="7" style="width: 120px" valign="top">
                                                        <img src="<jsp:getProperty name="userBean" property="MMRProfilePhoto"/>"
                                                             alt="Profile Photo" height="100" width="100" class="img-rounded">
                                                    </td>
                                                    <td style="width:100px">Name</td>
                                                    <td style="width:10px">:</td>
                                                    <td><jsp:getProperty name="userBean" property="display_name"/></td>
                                                </tr>
                                                <tr>
                                                    <td>Email</td>
                                                    <td>:</td>
                                                    <td><jsp:getProperty name="userBean" property="email"/></td>
                                                </tr>
                                                <tr>
                                                    <td>Gender</td>
                                                    <td>:</td>
                                                    <td><jsp:getProperty name="userBean" property="gender"/></td>
                                                </tr>
                                                <tr>
                                                    <td>MMR User Id</td>
                                                    <td>:</td>
                                                    <td><jsp:getProperty name="userBean" property="id"/></td>
                                                </tr>
                                                <tr>
                                                    <td>Height</td>
                                                    <td>:</td>
                                                    <td><jsp:getProperty name="userBean" property="height"/> m</td>
                                                </tr>
                                                <tr>
                                                    <td>Weight</td>
                                                    <td>:</td>
                                                    <td><jsp:getProperty name="userBean" property="weight"/> kg</td>
                                                </tr>
                                                <tr>
                                                    <td>Date Joined</td>
                                                    <td>:</td>
                                                    <td><jsp:getProperty name="userBean" property="date_joined"/></td>
                                                </tr>
                                            </table>
                                            <br>
                                            <button id="deleteButton" class="btn btn-danger" onclick="deleteMapMyRideToken()" style="width:150px">Unlink MMR Account</button>
                                        </c:when>
                                        <c:otherwise>
                                            <p>Pair MapMyRide Account</p>
                                            <a href="https://www.mapmyfitness.com/v7.1/oauth2/uacf/authorize/?client_id=<% Credentials cr = new Credentials();
                                    out.print(cr.getMmrClientIdForWeb());%>&response_type=code" role="button" class="btn btn-primary">MapMyRide</a>
                                            <p class="small"> User account isn't linked with an <abbr title="Under Armour">MapMyRide</abbr> account.</p>
                                        </c:otherwise>
                                    </c:choose>

                                </div>

                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </div>
    <script>
        function deleteMapMyRideToken() {
            console.log("Deleting Token...")
            $.ajax({
                url: "../DeleteMapMyRideToken",
                success: function (response) {
                    console.log("delete response: " + response)
                    $("#deleteButton").html("<i class=\"fa fa-spinner fa-spin\"></i>")
                    var button = document.getElementById("deleteButton");
                    button.disabled = true;
                    button.style.width="150px";
                    var serverResponse = JSON.parse(response);

                    if (serverResponse.result === "success") {
                        window.location.replace("./mapmyride.jsp?message=ad");
                    } else {
                        $("#results").html("<div class=\"alert alert-danger\" class=\"col-xs-12\">\n\
<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a>\n\
<strong>Failed!</strong> There was an error during unlinking MapMyRide account.");
                        button.disabled = false;
                        $("#deleteButton").html("Unlink MMR Account")
                    }
                },
                error: function (jqXHR, textStatus, errorMessage) {
                    console.log(errorMessage); // Optional
                }
            });
        }
    </script>



</body>
</html>    
<%@ include file="foot.jsp" %> 