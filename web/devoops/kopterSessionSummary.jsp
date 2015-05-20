<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>

<c:set var="selectedMenu" value="kopters"/>
<html>
    <%@ include file="head.jsp" %> 
    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">
                <jsp:useBean
                    id = "kopterSessionInfo" class= "mkws.webbeans.GetKopterSessionInfo">
                </jsp:useBean>
                <jsp:setProperty name="kopterSessionInfo" property = "sessionId" value="${param.sessionId}"/>
                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="../index.html">Home</a></li>
                            <li><a href="kopters.jsp">Kopters</a></li>
                            <li><a href="#">KopterSession #<% out.println(Integer.parseInt(request.getParameter("sessionId")));%></a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="routes" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>KopterSessionInfo for <% out.println(kopterSessionInfo.getSessionId());%></span>
                                </div>
                                <div class="box-icons">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>

                                    <a class="close-link">
                                        <i class="fa fa-times"></i>
                                    </a>
                                </div>
                                <div class="no-move"></div>
                            </div>
                            <div class="box-content">
                                <div class="row">
                                    <figure style="width: 900px; height: 500px;" id="voltageChart"></figure>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>       

    <%@include file="foot.jsp" %>
    <script src="plugins/d3/d3.v3.js"></script>
    <script src="plugins/xcharts/xcharts.js"></script>
    <script>
        (function() {

            var data = {
                "xScale": "linear",
                "yScale": "linear",
                "main": [
                    {
                        "className": ".pizza",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "voltageSerie"/>


                    }
                ], "comp": [
                    {
                        "className": ".pizza",
                        "type": "line",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "currentSerie"/>
                    }]
                    
            };
            var myChart = new xChart('line', data, '#voltageChart');


        }());
    </script>

</body>
</html>