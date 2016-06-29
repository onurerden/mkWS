<%-- 
    Document   : kopters
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
<c:set var="selectedMenu" value="logs"/>
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
                    </div>
                </div>

                <div class="row">
                    <div id="uploadGPX" class="col-xs-12">
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
                                <p>Pair MapMyRide Account</p>
                                <a href="https://www.mapmyfitness.com/v7.1/oauth2/uacf/authorize/?client_id=<% Credentials cr = new Credentials();
out.print(cr.getMmrClientIdForWeb());%>&response_type=code" role="button" class="btn btn-primary">MapMyRide</a>
                                <div id="authCode">
                                    <c:choose>
                            <c:when test="${param.code!=null}">

                                <div class="alert alert-success" class="col-xs-12">
                                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                    <strong>Success!</strong> MapMyRide paired successfully. Key is: 
                                    <% out.print(request.getParameter("code"));
                                    ServerEngine server = new ServerEngine();
                                    server.saveMMRauthorizationCodeForUser(Integer.valueOf(session.getAttribute("id").toString()),request.getParameter("code"));
                                    %>
                                </div>
                                <p>
                                    
                                    <% 
Gson gson = new Gson();                                    
MMRUser user = gson.fromJson(server.getMMRUserInfo(Integer.valueOf(session.getAttribute("id").toString())),MMRUser.class);
                                    
                                    out.print("User Name: " + user.getDisplay_name() + "<br>");
                                    out.print("Gender: " + user.getGender() + "<br>");
                                    out.print("eMail: " + user.getEmail() + "<br>");
                                    out.print("Date Joined " + user.getDate_joined()+ "<br>");
                                            %>
                                </p>

                            </c:when>    
                            <c:otherwise>

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

    

</body>
</html>    
<%@ include file="foot.jsp" %> 