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
                <!--Highchart -->
                <div class="row">       
                    <div id="highchart" class="col-xs-12 col-sm-12 col-md-12 col-lg-6">
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
                                    <div id="altitudeChart" style="width:100%; height:400px;"></div>

                                </div>


                            </div>
                        </div>
                    </div>


                    <!-- Other graph -->

                    <div id="speed" class="col-xs-12 col-sm-12 col-md-12 col-lg-6">
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
                                    <figure style="height: 400px;" id="speedChart"></figure>

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
    <script src="http://code.highcharts.com/highcharts.js"></script>
    <script type="text/javascript">

                                        var speedChart;
                                        var altitudeChart;
                                        var myTooltip;
                                        var altitudeTooltip;
                                        var speedButton;
                                        var altitudeButton;

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
                                          //  var altitudeJSON = <jsp:getProperty name="routeBean" property = "routeAltitudeValues"/>;

                                            
                                            drawAltitudeChart();
                                            msecConv();

                                        }

                                        function kmhConv() {

                                            var speedJSON = <jsp:getProperty name="routeBean" property = "routeSpeedKmhValues"/>

                                            speedChart = new Highcharts.Chart({
                                                chart: {
                                                    renderTo: 'speedChart',
                                                    type: 'area',
                                                    zoomType: "x",
                                                    events: {
                                                        load: function () {
                                                            myTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                                                        }
                                                    }
                                                },
                                                title: {
                                                    text: 'Speed (km/h)'
                                                },
                                                xAxis: {
                                                    title: {
                                                        text: "Speed (km/h)"
                                                    },
                                                    events: {
                                                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                                                            altitudeChart.xAxis[0].setExtremes(speedChart.xAxis[0].getExtremes().min, speedChart.xAxis[0].getExtremes().max);
                                                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                                                console.log('speedChart reset zoom');
                                                                $('.highcharts-button').hide();

                                                            } else {
                                                                altitudeButton = altitudeChart.showResetZoom();
                                                                console.log('speedChart zoom-in');

                                                            }
                                                        }
                                                    }
                                                },
                                                credits: {
                                                    enabled: false
                                                },
                                                animation: true,
                                                tooltip: {
                                                    enabled: false
                                                },
                                                series: [{
                                                        name: 'Speed (km/h)',
                                                        data: speedJSON,
                                                        color: '#3880aa',
                                                        fillOpacity: 0.1,
                                                        lineWidth: 3,
                                                        turboThreshold: 0,
                                                        point: {
                                                            events: {
                                                                mouseOver: function (evt) {
//                                    console.log('mouseOver');
                                                                 //   myTooltip.enabled = false;
                                                      //myTooltip.enabled = false;
                                                                   // myTooltip.refresh(speedChart.series[0].searchPoint(event, true));
                                                                  myTooltip.refresh(speedChart.series[0].points[evt.target.index]);
                                                                    //altitudeTooltip.refresh(altitudeChart.series[0].searchPoint(event, true));
                                                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);

                                                                }}}
                                                    }]
                                            });

                                            document.getElementById("kmhButton").className = 'btn btn-primary';
                                            document.getElementById("kmhButton").disabled = true;
                                            document.getElementById("msecButton").className = 'btn btn-default';
                                            document.getElementById("msecButton").disabled = false;

                                        }

                                        function msecConv() {
//alert("denemedir");

                                            var speedJSON = <jsp:getProperty name="routeBean" property = "routeSpeedValues"/>

                                            speedChart = new Highcharts.Chart({
                                                chart: {
                                                    renderTo: 'speedChart',
                                                    type: 'area',
                                                    zoomType: "x",
                                                    events: {
                                                        load: function () {
                                                            myTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                                                        }
                                                    }
                                                },
                                                title: {
                                                    text: 'Speed (m/sec)'
                                                },
                                                credits: {
                                                    enabled: false
                                                },
                                                xAxis: {
                                                    title: {
                                                        text: "Speed (m/sec)"
                                                    },
                                                    events: {
                                                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                                                            altitudeChart.xAxis[0].setExtremes(speedChart.xAxis[0].getExtremes().min, speedChart.xAxis[0].getExtremes().max);
                                                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                                                console.log('speedChart reset zoom');
                                                                $('.highcharts-button').hide();

                                                            } else {
                                                                altitudeButton = altitudeChart.showResetZoom();
                                                                console.log('speedChart zoom-in');

                                                            }

                                                        }
                                                    }
                                                },
                                                tooltip: {
                                                    enabled: false
                                                },
                                                animation: true,
                                                series: [{
                                                        name: 'Speed (m/sec)',
                                                        data: speedJSON,
                                                        color: '#3880aa',
                                                        fillOpacity: 0.1,
                                                        lineWidth: 3,
                                                        turboThreshold: 0,
                                                        point: {
                                                            events: {
                                                                mouseOver: function (evt) {
//                                    console.log('mouseOver');
                                                                //    myTooltip.enabled = false;
                                                                   // myTooltip.refresh(speedChart.series[0].searchPoint(event, true));
                                                                  myTooltip.refresh(speedChart.series[0].points[evt.target.index]);
                                                                    //altitudeTooltip.refresh(altitudeChart.series[0].searchPoint(event, true));
                                                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);


                                                                }}}
                                                    }]
                                            });
                                            document.getElementById("kmhButton").className = 'btn btn-default';
                                            document.getElementById("kmhButton").disabled = false;
                                            document.getElementById("msecButton").className = 'btn btn-primary';
                                            document.getElementById("msecButton").disabled = true;

                                        }


    </script>
    <script>
        $(document).ready(function () {
            // Load required scripts and callback to draw

DrawAllxCharts();
         //   LoadXChartScript(DrawAllxCharts);
            // Required for correctly resize charts, when boxes expand
            var graphxChartsResize;
            $(".box").resize(function (event) {
                event.preventDefault();
                clearTimeout(graphxChartsResize);
                graphxChartsResize = setTimeout(DrawAllxCharts, 500);
            });
            WinMove();
//            kmhConv();            
        });
    </script>
    <script>
        function drawAltitudeChart() {
            var altitudeJSON = <jsp:getProperty name="routeBean" property = "routeAltitudeValues"/>;
//            var yData = altitudeJSON.y;
//            (function (H) {
//                H.wrap(H.Tooltip.prototype, 'hide', function () {
//                });
//            }(Highcharts));
            altitudeChart = new Highcharts.Chart({
                chart: {
                    renderTo: 'altitudeChart',
                    type: 'area',
                    zoomType: "x",
                    events: {
                        load: function () {
                            altitudeTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
//                            console.log("chart loaded");
                        }
                    }
                },
                title: {
                    text: 'Altitude'
                },
                credits: {
                    enabled: false
                },
                xAxis: {
                    title: {
                        text: "Altitude"

                    },
                    events: {
                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                            speedChart.xAxis[0].setExtremes(altitudeChart.xAxis[0].getExtremes().min, altitudeChart.xAxis[0].getExtremes().max);

                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                console.log('altitudechart reset zoom');
                                $('.highcharts-button').hide();

                            } else {
                                speedButton = speedChart.showResetZoom();
                                console.log('altitude chart zoom-in');

                            }

                        }
                    }
                },
                animation: true,
                tooltip: {
                    enabled: false
                },
                series: [{
                        name: 'Altitude',
                        data: altitudeJSON,
                        color: '#3880aa',
                        fillOpacity: 0.1,
                        lineWidth: 3,
                        turboThreshold: 0,
                        point: {
                            events: {
                                mouseOver: function (evt) {
//                                    console.log('mouseOver');
                                    //   myTooltip.enabled = false;
                                                                   // myTooltip.refresh(speedChart.series[0].searchPoint(event, true));
                                                                  myTooltip.refresh(speedChart.series[0].points[evt.target.index]);
                                                                    //altitudeTooltip.refresh(altitudeChart.series[0].searchPoint(event, true));
                                                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);


                                },
                                mouseOut: function () {
//                                    HideAllToolTips();
                                }
                            }
                        }
                    }]
            });

        }
        function DrawAllToolTips(event) {

        }
        function HideAllToolTips() {
//            myTooltip.hide();
//            altitudeTooltip.hide();
        }

    </script>

</body>
</html>
