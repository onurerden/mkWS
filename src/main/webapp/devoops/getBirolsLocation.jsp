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

<c:set var="selectedMenu" value="birol"/>
<c:set var="isGuest" value="true"/>
<html>
    <%
        //  System.out.println("UserId: " + session.getAttribute("userid"));
        if (session.getAttribute("userid") == null) {
            session.setAttribute("isGuest", true);
            session.setAttribute("userid", "birolGuest");
            session.setAttribute("id", "410");
            session.setAttribute("first_name", "Birol'un");
            session.setAttribute("last_name", "Misafiri");
            session.setAttribute("isAdmin", false);
        } else {
            System.out.println("session is valid.");
        }
    %>
    <%@ include file="head.jsp" %> 

    <div id="main" class="container-fluid">
        <div class="row">

            <div id="content" class="col-xs-12 col-sm-12">


                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">                            
                            <li><a href="#">Birol's Last Route</a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="results" class="col-xs-12"></div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Ads</span>
                                </div>
                                <div class="box-icons">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>

                                </div>
                                <div class="no-move"></div>
                            </div>

                            <div class="box-content" style="height:50px">
                                <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
                                <!-- BirolsLocation -->
                                <ins class="adsbygoogle"
                                     style="display:block"
                                     data-ad-client="ca-pub-3002886887391013"
                                     data-ad-slot="4132961566"
                                     data-ad-format="auto"></ins>
                                <script>
                                    (adsbygoogle = window.adsbygoogle || []).push({});
                                </script>
                            </div>
                        </div>
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
                                <div id=routeDetails>
                                    <h2 id="routeTitle">Route Id: </h2>
                                    <b>Gathering data... </b>

                                    <br>
                                </div>
                                <div id="divResult"></div>
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
                                    <span id="speedLabel" style="display:inline-block; width:125px;">Speed (kmh)</span>                                   
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
    <script src="https://api-maps.yandex.ru/2.0/?load=package.full&lang=en-US"
    type="text/javascript"></script>

    <%@include file="foot.jsp" %>
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script>

    </script>
    <script type="text/javascript">
        var token;
        var speedChart;
        var altitudeChart;
        var myTooltip;
        var altitudeTooltip;
        var speedButton;
        var altitudeButton;
        var myMap;
        var mapMarker;
        var followMeData;
        var coordinates = [];
        var routeData;
        var bounds = [[0, 0], [20, 20]];
        var altitudeJSON = [];
        var speedJSON = [];
        var speedJSONkmh = [];
        var refreshCount = 0;

        // Initializes the map as soon as the API is loaded and DOM is ready

        function init() {
            myMap = new ymaps.Map("yandex", {
                //    center: 
                //    zoom: 15,
                bounds: bounds
            }),
                    // A polyline
                    myPolyline = new ymaps.Polyline(
                            coordinates

                            , {
                                // The balloon content
                                balloonContent: "FollowMe Device Route for device:"
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
                    coordinates: coordinates[0]
                },
                properties: {
                    iconContent: "Start"}
            }, {preset: 'twirl#greenStretchyIcon'});
            myEndPoint = new ymaps.GeoObject({
                geometry: {
                    type: "Point",
                    coordinates: coordinates[coordinates.length - 1]
                },
                properties: {
                    iconContent: "Finish"}
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

            drawAltitudeChart();
            kmhConv();
        }

        function kmhConv() {

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
                        data: speedJSONkmh,
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
                                    //  myTooltip.refresh(speedChart.series[0].points[evt.target.index]);
                                    //altitudeTooltip.refresh(altitudeChart.series[0].searchPoint(event, true));
                                    //  altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);

                                }}}
                    }]
            });


        }


    </script>
    <script>
        function downloadGPX(routeId) {
            console.log("downloading route: " + routeId);
            var linkUri = "../DownloadGPX?routeId=" + routeId;
            console.log(linkUri);
            var link = document.createElement("a");
            link.download = "route" + routeId;
            link.href = linkUri;
            link.click();

        }
    </script>
    <script>
        $(document).ready(function () {
            // Load required scripts and callback to draw
            getBirolsLastRoute();

            refreshData();

            //   LoadXChartScript(DrawAllxCharts);
            // Required for correctly resize charts, when boxes expand

            WinMove();
//            kmhConv();            
        });
        function refreshData() {
            setInterval(function () {
                window.location.reload();
            }, 60000);
        }
    </script>
    <script>
        function drawAltitudeChart() {
            //var altitudeJSON;
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
//                                
                                    //       myTooltip.refresh(speedChart.series[0].points[evt.target.index]);
                                    //       altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);


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
        function getBirolsLastRoute() {
            $.ajax({
                url: '../api/GetBirolsLastRoute',
                type: 'GET',
                dataType: 'json',
                contentType: "json;charset=utf-8",
                success: function (response) {
                    console.log("Last route is: " + response.routeId);
                    token = response.token;
                    getRouteDetails(response.routeId);

                },
                error: function (error) {
                    alert('Hata!' + error.description);

                },
                beforeSend: setHeader

            });

        }
        function getRouteDetails(routeId) {
            $.ajax({
                url: '../api/GetRouteDetails?routeId=' + routeId,
                type: 'GET',
                dataType: 'json',
                contentType: "json;charset=utf-8",
                success: function (response) {
                    token = "";
                    coordinates = [];
                    altitudeJSON = [];
                    speedJSON = [];
                    speedJSONkmh = [];
                    followMeData = response.followMeData;
                    routeData = response;
                    var minBounds = [999, 999];
                    var maxBounds = [-100, -100];

                    followMeData.forEach(function (entry) {
                        //console.log(entry);
                        var newCoordinate = [entry.lat, entry.lng];
                        if (entry.lat < minBounds[0]) {
                            minBounds[0] = entry.lat;
                        }
                        if (entry.lat > maxBounds[0]) {
                            maxBounds[0] = entry.lat;
                        }
                        if (entry.lng < minBounds[1]) {
                            minBounds[1] = entry.lng;
                        }
                        if (entry.lng > maxBounds[1]) {
                            maxBounds[1] = entry.lng;
                        }

                        coordinates.push(newCoordinate);
                        altitudeJSON.push(entry.altitude);
                        speedJSON.push(entry.speed);
                        speedJSONkmh.push(entry.speed * 3.6);
                        bounds = [minBounds, maxBounds];

                    });
                    var duration = "";
                    if (routeData.duration < 60) {
                        duration = routeData.duration + " seconds";
                    } else if (routeData.duration < 3600) {
                        duration = "" + Math.floor(routeData.duration / 60) + ":" + timeFormatter(routeData.duration % 60);
                    } else {
                        duration = "" + Math.floor(routeData.duration / 3600) + ":" + timeFormatter(Math.floor((routeData.duration % 3600) / 60)) + ":" + timeFormatter(routeData.duration % 60);
                    }

                    var details = "<h2>Route Id: " + routeData.routeId + "</h2>\n\
                                    <b>Route Length: </b> <i>" + routeData.routeLength + " km</i><br>\n\
                                    <b>Route Date:</b><i> " + routeData.time + " </i><br>\n\
                                   <b>Duration:</b> <i>" + duration + " </i><br>";


                    $('#routeDetails').html(details);
                    ymaps.ready(init);

                    //console.log(response);
                    DrawAllxCharts();
                    var graphxChartsResize;
                    $(".box").resize(function (event) {
                        event.preventDefault();
                        clearTimeout(graphxChartsResize);
                        graphxChartsResize = setTimeout(DrawAllxCharts, 500);
                    });


                },
                error: function (error) {
                    alert('Hata!' + error.description);
                },
                beforeSend: setHeader

            });
        }
        function setHeader(xhr) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + token);
        }
        function timeFormatter(n) {
            return n > 9 ? "" + n : "0" + n;
        }
    </script>

</body>
</html>
