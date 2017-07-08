<%-- 
    Document   : nav
    Created on : 19.Mar.2015, 14:30:57
    Author     : oerden
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id="sidebar-left" class="col-xs-2 col-sm-2">
    <ul class="nav main-menu">

        <li>
            <c:choose>
                <c:when test="${selectedMenu=='kopters'}">
                <a href="kopters.jsp" class="active">
                </c:when>
                <c:otherwise>
                    <a href="kopters.jsp">
                </c:otherwise>
            </c:choose>
            
                <i class="fa fa-cog"></i>
                <span class="hidden-xs">Kopters</span>
            </a>
        </li>

        <li>
            <c:choose>
                <c:when test="${selectedMenu=='followme'}">
                <a href="followMeDevices.jsp" class="active">
                </c:when>
                <c:otherwise>
                    <a href="followMeDevices.jsp">
                </c:otherwise>
            </c:choose>
                <i class="fa fa-bullseye"></i>
                <span class="hidden-xs">FollowMeDevices</span>
            </a>
        </li>
        <li>
            <c:choose>
                <c:when test="${selectedMenu=='routes'}">
                <a href="routes.jsp" class="active">
                </c:when>
                <c:otherwise>
                    <a href="routes.jsp">
                </c:otherwise>
            </c:choose>
                <i class="fa fa-map-marker"></i>
                <span class="hidden-xs">Routes</span>
            </a>
        </li>
        <li>
            <c:choose>
                <c:when test="${selectedMenu=='logs'}">
                <a href="logs.jsp" class="active">
                </c:when>
                <c:otherwise>
                    <a href="logs.jsp">
                </c:otherwise>
            </c:choose>
                <i class="fa fa-wrench"></i>
                <span class="hidden-xs">Logs</span>
            </a>
        </li>
        <c:choose>
            <c:when test="${sessionScope.isAdmin}">
                <li>
           <c:choose>
                <c:when test="${selectedMenu=='users'}">
                <a href="users.jsp" class="active">
                </c:when>
                <c:otherwise>
                    <a href="users.jsp">
                </c:otherwise>
            </c:choose>
                <i class="fa fa-users"></i>
                <span class="hidden-xs">Users</span>
            </a>
                </li>
                <li>
           <c:choose>
                <c:when test="${selectedMenu=='dashboard'}">
                <a href="dashboard.jsp" class="active">
                </c:when>
                <c:otherwise>
                    <a href="dashboard.jsp">
                </c:otherwise>
            </c:choose>
                <i class="fa fa-dashboard"></i>
                <span class="hidden-xs">Dashboard</span>
            </a>
                </li>
                  <li>
           <c:choose>
                <c:when test="${selectedMenu=='feedbacks'}">
                <a href="feedbacks.jsp" class="active">
                </c:when>
                <c:otherwise>
                    <a href="feedbacks.jsp">
                </c:otherwise>
            </c:choose>
                <i class="fa fa-comment"></i>
                <span class="hidden-xs">Feedbacks</span>
            </a>
                </li>     
<li>
            <a href="http://mk-onurerden.rhcloud.com/phpmyadmin" target="_blank">
                <i class="fa fa-book"></i>
                <span class="hidden-xs">phpMyAdmin</span>
            </a>
        </li>
        </c:when>
        </c:choose>
 


    </ul>
</div>