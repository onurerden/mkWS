<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>
<%// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); %>

<html>
<%@ include file="head.jsp" %> 

<div id="main" class="container-fluid">
                                            <div class="row">
<%@ include file="nav.jsp" %>                                                 
                                                <div class="col-xs-12 col-sm-10">
<jsp:useBean
            id = "routeBean" class= "mkws.webbeans.GetRouteDetails">
        </jsp:useBean>
<jsp:setProperty name="routeBean" property = "routeId" value="${param.routeId}"/>

        <div class="row">
	<div id="breadcrumb" class="col-xs-12">
		<ol class="breadcrumb">
			<li><a href="../index.html">Home</a></li>
			<li><a href="routes.jsp">Routes</a></li>
		</ol>
	</div>
</div>
<div class="row">
    <div id="kopters" class="col-xs-9">
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
				<div id="map" style="width:auto; height:400px">
                                
                                </div>
			</div>
    </div>
    </div>
    <div id="kopters" class="col-xs-3">
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
                var myMap = new ymaps.Map("map", {
                    center: <jsp:getProperty name="routeBean" property="routeStartPoint"/>
                    zoom: 15
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

                myMap.controls.add('mapTools');
                myMap.controls.add('typeSelector');
                myMap.controls.add('smallZoomControl');

            }
        </script>
                    
                    </div>
</body>
</html>