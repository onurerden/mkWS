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

<c:set var="selectedMenu" value="routes"/>
<html>
    <%@ include file="head.jsp" %> 

    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">
                <jsp:useBean
                    id = "routeBean" class= "mkws.webbeans.GetRouteDetails">
                </jsp:useBean>
                <jsp:setProperty name="routeBean" property = "routeId" value="${param.routeId}"/>

                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="../index.html">Home</a></li>
                            <li><a href="routes.jsp">Routes</a></li>
                            <li><a href="#">Route #<% out.println(Integer.parseInt(request.getParameter("routeId")));%></a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="routes" class="col-md-8 col-lg-9">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Route Map</span>
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


                    <div id="details" class="col-md-4 col-lg-3">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Details</span>
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
                            <div class="box-content" style="width:auto; height:430px">
                                <h2>Route Id: <% out.println(Integer.parseInt(request.getParameter("routeId")));%> </h2>
                                <b>Device Name :</b> <i><jsp:getProperty name="routeBean" property="deviceName"/> </i><br/>
                                <b>Route Date : </b> <i><jsp:getProperty name="routeBean" property="routeCreationDate"/> </i><br/>
                                <b>Route length: </b>
                                <c:choose> 
                                    <c:when test="${routeBean.getRouteLength()>1}">
                                        <fmt:formatNumber type="number" maxFractionDigits="2" value="<%= routeBean.getRouteLength()%>" /> km
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" maxFractionDigits="2" value="<%= routeBean.getRouteLength() * 1000%>" /> m
                                    </c:otherwise>
                                </c:choose>   
                                </br>
                                <b>Max Speed : </b> <i><jsp:getProperty name="routeBean" property="maxSpeed"/> m/sec</i><br/>
                                <b>Max Altitude : </b> <i><jsp:getProperty name="routeBean" property="maxAltitude"/> m </i><br/>
                                <b>Min Altitude : </b> <i><jsp:getProperty name="routeBean" property="minAltitude"/> m </i><br/>



                            </div>

                        </div>
                    </div>
                </div>
                <div class="row">       
                    <div id="speed" class="col-xs-12 col-sm-12 col-md-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Altitude (m)</span>
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
                                    <figure style="height: 200px;" id="altitudeChart"></figure>

                                </div>
                                <div class="row" style="text-align:center" id="altitudeValue">
                                    Altitude (m)
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
                <!-- Other graph -->
                <div class="row">       
                    <div id="speed" class="col-xs-12 col-sm-12 col-md-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span id="speedLabel" style="display:inline-block; width:125px;">Speed (m/sec)</span>
                                    <button id="kmhButton" onclick="kmhConv()" type="button" class="btn btn-primary">km/h</button>
                                    <button id= "msecButton" onclick="msecConv()" type="button" class="btn btn-primary">m/sec</button>
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
                                    <figure style="height: 200px;" id="speedChart"></figure>

                                </div>
                                <div class="row" style="text-align:center" id="speedValue">
                                    Speed (m/sec)
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
    <%@include file="foot.jsp" %>
    <script type="text/javascript">


                                        // Initializes the map as soon as the API is loaded and DOM is ready
                                        ymaps.ready(init);

                                        function init() {
                                            var myMap = new ymaps.Map("yandex", {
                                                //    center: <jsp:getProperty name="routeBean" property="routeStartPoint"/>
                                                //    zoom: 15,
                                                bounds:<jsp:getProperty name="routeBean" property="mapBounds"/>
                                            }),
                                                    // A polyline
                                                    myPolyline = new ymaps.Polyline([
                                                        // The coordinates of polyline vertices.


        <jsp:getProperty name="routeBean" property="routePoints"/>
                                                    ], {
                                                        // The balloon content
                                                        balloonContent: "FollowMe Device Route for device: <jsp:getProperty name="routeBean" property="routeId"/>"
                                                    }, {
                                                        // Balloon options
                                                        // Disabling the "Close" button
                                                        balloonHasCloseButton: false,
                                                        // The line width
                                                        strokeWidth: 4,
                                                        // The line transparency
                                                        strokeOpacity: 0.8,
                                                        // Defines the color of the line - white
                                                        strokeColor: "#FF0000"
                                                    });

                                            myStartPoint = new ymaps.GeoObject({
                                                geometry: {
                                                    type: "Point",
                                                    coordinates: <jsp:getProperty name="routeBean" property="routeStartPoint"/>
                                                },
                                                properties: {
                                                    iconContent: "Başlangıç"}
                                            }, {preset: 'twirl#greenStretchyIcon'});

                                            myEndPoint = new ymaps.GeoObject({
                                                geometry: {
                                                    type: "Point",
                                                    coordinates: <jsp:getProperty name="routeBean" property="routeEndPoint"/>
                                                },
                                                properties: {
                                                    iconContent: "Bitiş"}
                                            }, {preset: 'twirl#redStretchyIcon'});

                                            // Adds geo objects to the map 

                                            myMap.geoObjects
                                                    .add(myPolyline)
                                                    .add(myStartPoint)
                                                    .add(myEndPoint);





                                            var button = new ymaps.control.Button({
                                                data: {
                                                    // Setting an icon for the button.
                                                    //image: 'images/button.jpg',
                                                    // Text on the icon.
                                                    content: 'Save',
                                                    // Text for the popup hint.
                                                    title: 'Click to save the route'
                                                }
                                            }, {
                                                // Setting options for the button.
                                                selectOnClick: false

                                            });
                                            myMap.controls.add(button, {bottom: 10, left: 5});

                                            myMap.controls.add('mapTools');
                                            myMap.controls.add('typeSelector');
                                            myMap.controls.add('smallZoomControl');
                                            myMap.copyrights.add('&copy; Belkopter Team');
                                            myMap.setZoom(myMap.getZoom() - 1);
                                        }

                                        function DrawAllxCharts() {
                                           var altitudeJSON = <jsp:getProperty name="routeBean" property = "routeAltitudeValues"/>;
                                            

                                            var altitudeData = {
                                                "xScale": "linear",
                                                "yScale": "linear",
                                                "main": [
                                                    {
                                                        "className": ".pizza",
                                                        "data": altitudeJSON
                                                    }
                                                ]

                                            };

                                            var opts = {
                                                "axisPaddingRight": 50,
                                                "yMin": 0.0,
                                                "paddingRight": 50,
                                                "interpolate":"basis"
                                            };

                                            var altitudeChart = new xChart('line', altitudeData, '#altitudeChart', opts);
                                            msecConv();

                                        }

                                        function kmhConv() {

                                            var speedJSON = <jsp:getProperty name="routeBean" property = "routeSpeedKmhValues"/>
                                            var speedData = {
                                                "xScale": "linear",
                                                "yScale": "linear",
                                                "main": [
                                                    {
                                                        "className": ".pizza",
                                                        "data": speedJSON
                                                    }
                                                ]

                                            };
                                            var opts = {
                                                "axisPaddingRight": 50,
                                                "yMin": 0.0,
                                                "paddingRight": 50,
                                                "interpolate":"basis"
                                            };
                                            var speedChart = new xChart('line', speedData, '#speedChart', opts);
                                            document.getElementById("kmhButton").className = 'btn btn-primary';
                                            document.getElementById("kmhButton").disabled = true;
                                            document.getElementById("msecButton").className = 'btn btn-default';
                                            document.getElementById("msecButton").disabled = false;
                                            document.getElementById("speedLabel").innerHTML = "Speed (km/h) ";
                                        }

                                        function msecConv() {
//alert("denemedir");

                                            var speedJSON = <jsp:getProperty name="routeBean" property = "routeSpeedValues"/>
                                            var speedData = {
                                                "xScale": "linear",
                                                "yScale": "linear",
                                                "main": [
                                                    {
                                                        "className": ".pizza",
                                                        "data": speedJSON

                                                    }
                                                ]

                                            };
                                            var opts = {
                                                "axisPaddingRight": 50,
                                                "yMin": 0.0,
                                                "paddingRight": 50,
                                                "interpolate":"basis"
                                            };
                                            var speedChart = new xChart('line', speedData, '#speedChart', opts);

                                            document.getElementById("kmhButton").className = 'btn btn-default';
                                            document.getElementById("kmhButton").disabled = false;
                                            document.getElementById("msecButton").className = 'btn btn-primary';
                                            document.getElementById("msecButton").disabled = true;
                                            document.getElementById("speedLabel").innerHTML = "Speed (m/sec) ";
                                        }


    </script>
    <script>
        $(document).ready(function () {
            // Load required scripts and callback to draw
            LoadXChartScript(DrawAllxCharts);
            // Required for correctly resize charts, when boxes expand
            var graphxChartsResize;
            $(".box").resize(function (event) {
                event.preventDefault();
                clearTimeout(graphxChartsResize);
                graphxChartsResize = setTimeout(DrawAllxCharts, 500);
            });
            WinMove();
        });
    </script>


</body>
</html>
