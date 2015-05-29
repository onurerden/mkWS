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
                    <div id="map" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Flight Map</span>
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

                            <div id="yandex" class="box-content" style="height:430px">

                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div id="voltageBox" class="col-xs-12 col-sm-6 col-md-4">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Voltage</span>
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
                                <div class="row">
                                    <figure style="width: 300px; height: 200px;" id="voltageChart"></figure>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div id="currentBox" class="col-xs-12 col-sm-6 col-md-4">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Current</span>
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
                                <div class="row">
                                    <figure style="height: 200px;" id="currentChart"></figure>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div id="capacityBox" class="col-xs-12 col-sm-6 col-md-4">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Capacity</span>
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
                                <div class="row">
                                    <figure style="height: 200px;" id="capacityChart"></figure>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
                <!--div altitude-->
                <div class="row">
                    <div id="altitudebox" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Altitude</span>
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
                                <div class="row">
                                    <figure  style="height: 200px;" id="altitudeChart"></figure>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

                <!-- 3. satır -->
                <div class="row">
                    <div id="gsmSignal" class="col-xs-12 col-sm-6 col-md-4">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>GSM Signal Strength</span>
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
                                <div class="row">
                                    <figure  style="height: 200px;" id="gsmSignalChart"></figure>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div id="rcSignalBox" class="col-xs-12 col-sm-6 col-md-4">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>RC Signal Strength</span>
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
                                <div class="row">
                                    <figure  style="height: 200px;" id="rcSignalChart"></figure>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div id="satBox" class="col-xs-12 col-sm-6 col-md-4">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Satellite Count</span>
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
                                <div class="row">
                                    <figure  style="height: 200px;" id="satChart"></figure>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>       
    <script src="http://api-maps.yandex.ru/2.0/?load=package.full&lang=en-US"
    type="text/javascript"></script>
    <script type="text/javascript">

        // Initializes the map as soon as the API is loaded and DOM is ready
        ymaps.ready(init);
        function init() {
            var myMap = new ymaps.Map("yandex", {
                bounds:<jsp:getProperty name="kopterSessionInfo" property="mapBounds"/>
            });
                    
                    // A polyline
                    myPolyline = new ymaps.Polyline([
                        // The coordinates of polyline vertices.
        <jsp:getProperty name="kopterSessionInfo" property = "latLonSerie"/>
                    ] );

            myMap.geoObjects
                    .add(myPolyline)

            myMap.controls.add('mapTools');
            myMap.controls.add('typeSelector');
            myMap.controls.add('smallZoomControl');
            myMap.copyrights.add('&copy; Belkopter Team');
            myMap.setZoom(myMap.getZoom() - 1);
        }
    </script>


    <script src = "plugins/d3/d3.v3.js" ></script>
    <script src="plugins/xcharts/xcharts.js"></script>
    <script>
        (function() {

            var voltageData = {
                "xScale": "linear",
                "yScale": "linear",
                "main": [
                    {
                        "className": ".pizza",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "voltageSerie"/>


                    }
                ]

            };
            var currentData = {
                "xScale": "linear",
                "yScale": "linear",
                "main": [
                    {
                        "className": ".pizza",
                        "type": "line",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "currentSerie"/>
                    }]};

            var capacityData = {
                "xScale": "linear",
                "yScale": "linear",
                "main": [
                    {
                        "className": ".pizza",
                        "type": "line",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "capacitySerie"/>
                    }]};

            var altitudeData = {
                "xScale": "linear",
                "yScale": "linear",
                "main": [
                    {
                        "className": ".pizza",
                        "type": "line",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "altitudeSerie"/>
                    }]};
            var gsmSignalData = {
                "xScale": "linear",
                "yScale": "linear",
                "main": [
                    {
                        "className": ".pizza",
                        "type": "line",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "gsmSignalSerie"/>
                    }]};
            var rcSignalData = {
                "xScale": "linear",
                "yScale": "linear",
                "main": [
                    {
                        "className": ".pizza",
                        "type": "line",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "rcSignalSerie"/>
                    }]};
            var satData = {
                "xScale": "linear",
                "yScale": "linear",
                "main": [
                    {
                        "className": ".pizza",
                        "type": "line",
                        "data":
        <jsp:getProperty name="kopterSessionInfo" property = "satSerie"/>
                    }]};

            var opts = {
                "tickHintY": 5
            };

            var voltageChart = new xChart('line', voltageData, '#voltageChart');
            var currentChart = new xChart('line', currentData, '#currentChart');
            var capacityChart = new xChart('line', capacityData, '#capacityChart');
            var altitudeChart = new xChart('line', altitudeData, '#altitudeChart');
            var gsmSignalChart = new xChart('line', gsmSignalData, '#gsmSignalChart');
            var rcSignalChart = new xChart('line', rcSignalData, '#rcSignalChart');
            var satChart = new xChart('line', satData, '#satChart', opts);

        }());
    </script>

    <%@include file="foot.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            WinMove();
        });
    </script>

</body>
</html>