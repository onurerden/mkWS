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
                    <div class="col-xs-12">
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

                            <div class="box-content">
                                <!--<div id="yandex" style="height:430px"></div>-->
                                <div id="map-canvas" style="height: 450px"></div>
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
                                    <figure style="height: 200px;" id="voltageChart"></figure>

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

    <%@include file="foot.jsp" %>
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
    <script>
        var map;
        var voltageChart;
        var capacityChart;
        var currentChart;
        var altitudeChart;
        var gpsCountChart;
        var gsmChart;
        var rcChart;
        var voltageTooltip;
        var capacityTooltip;
        var currentTooltip;
        var altitudeTooltip;
        var gpsCountTooltip;
        var gsmTooltip;
        var rcTooltip;

        function BoundsControl(controlDiv, map) {

            // Set CSS for the control border
            var controlUI = document.createElement('div');
            controlUI.style.backgroundColor = '#fff';
            controlUI.style.border = '2px solid #fff';
            controlUI.style.borderRadius = '3px';
            controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
            controlUI.style.cursor = 'pointer';
            controlUI.style.marginTop = '22px';
            controlUI.style.marginLeft = '22px';
            controlUI.style.textAlign = 'center';
            controlUI.title = 'Uçuş Rotasını Ortalamak için tıklayın';

            controlDiv.appendChild(controlUI);

            // Set CSS for the control interior
            var controlText = document.createElement('div');
            controlText.style.color = 'rgb(25,25,25)';
            controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
            controlText.style.fontSize = '16px';
            controlText.style.lineHeight = '38px';
            controlText.style.paddingLeft = '5px';
            controlText.style.paddingRight = '5px';
            controlText.innerHTML = 'Rotayı Ortala';
            controlUI.appendChild(controlText);

            // Setup the click event listeners: simply set the map to
            // Chicago
            google.maps.event.addDomListener(controlUI, 'click', function () {
        <jsp:getProperty name="kopterSessionInfo" property = "mapBounds"/>;
                var bounds = new google.maps.LatLngBounds(southWest, northEast);
                map.fitBounds(bounds);
            });

        }


        function initialize() {
            var mapOptions = {
                disableDefaultUI: true,
                mapTypeControl: true,
                zoomControl: true,
                zoomControlOptions: {
                    style: google.maps.ZoomControlStyle.LARGE,
                    position: google.maps.ControlPosition.LEFT_CENTER
                }
            };
            map = new google.maps.Map(document.getElementById('map-canvas'),
                    mapOptions);
            var flightPlanCoordinates = <jsp:getProperty name="kopterSessionInfo" property = "latLonSerie"/>;

            var flightPath = new google.maps.Polyline({
                path: flightPlanCoordinates,
                geodesic: true,
                strokeColor: '#3880aa',
                strokeOpacity: 1.0,
                strokeWeight: 3
            });
        <jsp:getProperty name="kopterSessionInfo" property = "mapBounds"/>;
            var bounds = new google.maps.LatLngBounds(southWest, northEast);
            map.fitBounds(bounds);

            flightPath.setMap(map);
            var boundsControlDiv = document.createElement('div');
            var boundsControl = new BoundsControl(boundsControlDiv, map);

            boundsControlDiv.index = 1;
            map.controls[google.maps.ControlPosition.TOP_LEFT].push(boundsControlDiv);
        }
        google.maps.event.addDomListener(window, 'load', initialize);

    </script>

    <!-- çizelge verileri dolduruluyor-->


    <script>
        function DrawAllxCharts() {
            capacityChart = new Highcharts.Chart({
                chart: {
                    type: 'area',
                    renderTo: "capacityChart",
                    zoomType: "x",
                    events: {
                        load: function () {
                            capacityTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                        }
                    }
                },
                title: {
                    text: ''
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    enabled: false
                },
                xAxis: {
                    title: {
                        text: "Capacity (mAh)"
                    },
                    events: {
                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                            currentChart.xAxis[0].setExtremes(capacityChart.xAxis[0].getExtremes().min, capacityChart.xAxis[0].getExtremes().max);
                            rcChart.xAxis[0].setExtremes(capacityChart.xAxis[0].getExtremes().min, capacityChart.xAxis[0].getExtremes().max);
                            satChart.xAxis[0].setExtremes(capacityChart.xAxis[0].getExtremes().min, capacityChart.xAxis[0].getExtremes().max);
                            gsmChart.xAxis[0].setExtremes(capacityChart.xAxis[0].getExtremes().min, capacityChart.xAxis[0].getExtremes().max);
                            voltageChart.xAxis[0].setExtremes(capacityChart.xAxis[0].getExtremes().min, capacityChart.xAxis[0].getExtremes().max);
                            altitudeChart.xAxis[0].setExtremes(capacityChart.xAxis[0].getExtremes().min, capacityChart.xAxis[0].getExtremes().max);
                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                console.log('gsmChart reset zoom');
                                $('.highcharts-button').hide();

                            } else {
                                altitudeChart.showResetZoom();
                                voltageChart.showResetZoom();
                                currentChart.showResetZoom();
                                capacityChart.showResetZoom();
                                satChart.showResetZoom();
                                gsmChart.showResetZoom();
                                rcChart.showResetZoom();
                                console.log('speedChart zoom-in');

                            }
                        }
                    }
                },
                animation: true,
                legend: {
                    enabled: false
                },
                series: [{
                        name: 'Capacity (mAh)',
                        data: <jsp:getProperty name="kopterSessionInfo" property = "capacitySerie"/>,
                        color: '#3880aa',
                        fillOpacity: 0.1,
                        lineWidth: 3,
                        turboThreshold: 0,
                        point: {
                            events: {
                                mouseOver: function (evt) {


                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);
                                    gsmTooltip.refresh(gsmChart.series[0].points[evt.target.index]);
                                    rcTooltip.refresh(rcChart.series[0].points[evt.target.index]);
                                    satTooltip.refresh(satChart.series[0].points[evt.target.index]);
                                    voltageTooltip.refresh(voltageChart.series[0].points[evt.target.index]);
                                    capacityTooltip.refresh(capacityChart.series[0].points[evt.target.index]);
                                    currentTooltip.refresh(currentChart.series[0].points[evt.target.index]);

                                }}}
                    }]
            });
            rcChart = new Highcharts.Chart({
                chart: {
                    type: 'area',
                    renderTo: "rcSignalChart",
                    zoomType: "x",
                    events: {
                        load: function () {
                            rcTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                        }
                    }
                },
                title: {
                    text: ''
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    enabled: false
                },
                xAxis: {
                    title: {
                        text: "RC Signal"
                    },
                    events: {
                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                            currentChart.xAxis[0].setExtremes(rcChart.xAxis[0].getExtremes().min, rcChart.xAxis[0].getExtremes().max);
                            capacityChart.xAxis[0].setExtremes(rcChart.xAxis[0].getExtremes().min, rcChart.xAxis[0].getExtremes().max);
                            satChart.xAxis[0].setExtremes(rcChart.xAxis[0].getExtremes().min, rcChart.xAxis[0].getExtremes().max);
                            gsmChart.xAxis[0].setExtremes(rcChart.xAxis[0].getExtremes().min, rcChart.xAxis[0].getExtremes().max);
                            voltageChart.xAxis[0].setExtremes(rcChart.xAxis[0].getExtremes().min, rcChart.xAxis[0].getExtremes().max);
                            altitudeChart.xAxis[0].setExtremes(rcChart.xAxis[0].getExtremes().min, rcChart.xAxis[0].getExtremes().max);
                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                console.log('gsmChart reset zoom');
                                $('.highcharts-button').hide();

                            } else {
                                altitudeChart.showResetZoom();
                                voltageChart.showResetZoom();
                                currentChart.showResetZoom();
                                capacityChart.showResetZoom();
                                satChart.showResetZoom();
                                gsmChart.showResetZoom();
                                rcChart.showResetZoom();
                                console.log('speedChart zoom-in');

                            }
                        }
                    }
                },
                animation: true,
                legend: {
                    enabled: false
                },
                series: [{
                        name: 'RC Signal',
                        data: <jsp:getProperty name="kopterSessionInfo" property = "rcSignalSerie"/>,
                        color: '#3880aa',
                        fillOpacity: 0.1,
                        lineWidth: 3,
                        turboThreshold: 0,
                        point: {
                            events: {
                                mouseOver: function (evt) {


                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);
                                    gsmTooltip.refresh(gsmChart.series[0].points[evt.target.index]);
                                    rcTooltip.refresh(rcChart.series[0].points[evt.target.index]);
                                    satTooltip.refresh(satChart.series[0].points[evt.target.index]);
                                    voltageTooltip.refresh(voltageChart.series[0].points[evt.target.index]);
                                    capacityTooltip.refresh(capacityChart.series[0].points[evt.target.index]);
                                    currentTooltip.refresh(currentChart.series[0].points[evt.target.index]);

                                }}}
                    }]
            });
            voltageChart = new Highcharts.Chart({
                chart: {
                    type: 'area',
                    renderTo: "voltageChart",
                    zoomType: "x",
                    events: {
                        load: function () {
                            voltageTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                        }
                    }
                },
                title: {
                    text: ''
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    enabled: false
                },
                xAxis: {
                    title: {
                        text: "Voltage (V)"
                    },
                    events: {
                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                            currentChart.xAxis[0].setExtremes(voltageChart.xAxis[0].getExtremes().min, voltageChart.xAxis[0].getExtremes().max);
                            capacityChart.xAxis[0].setExtremes(voltageChart.xAxis[0].getExtremes().min, voltageChart.xAxis[0].getExtremes().max);
                            satChart.xAxis[0].setExtremes(voltageChart.xAxis[0].getExtremes().min, voltageChart.xAxis[0].getExtremes().max);
                            gsmChart.xAxis[0].setExtremes(voltageChart.xAxis[0].getExtremes().min, voltageChart.xAxis[0].getExtremes().max);
                            rcChart.xAxis[0].setExtremes(voltageChart.xAxis[0].getExtremes().min, voltageChart.xAxis[0].getExtremes().max);
                            altitudeChart.xAxis[0].setExtremes(voltageChart.xAxis[0].getExtremes().min, voltageChart.xAxis[0].getExtremes().max);
                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                console.log('gsmChart reset zoom');
                                $('.highcharts-button').hide();

                            } else {
                                altitudeChart.showResetZoom();
                                voltageChart.showResetZoom();
                                currentChart.showResetZoom();
                                capacityChart.showResetZoom();
                                satChart.showResetZoom();
                                gsmChart.showResetZoom();
                                rcChart.showResetZoom();
                                console.log('speedChart zoom-in');

                            }
                        }
                    }
                },
                animation: true,
                legend: {
                    enabled: false
                },
                series: [{
                        name: 'Voltage',
                        data: <jsp:getProperty name="kopterSessionInfo" property = "voltageSerie"/>,
                        color: '#3880aa',
                        fillOpacity: 0.1,
                        lineWidth: 3,
                        turboThreshold: 0,
                        point: {
                            events: {
                                mouseOver: function (evt) {


                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);
                                    gsmTooltip.refresh(gsmChart.series[0].points[evt.target.index]);
                                    rcTooltip.refresh(rcChart.series[0].points[evt.target.index]);
                                    satTooltip.refresh(satChart.series[0].points[evt.target.index]);
                                    voltageTooltip.refresh(voltageChart.series[0].points[evt.target.index]);
                                    capacityTooltip.refresh(capacityChart.series[0].points[evt.target.index]);
                                    currentTooltip.refresh(currentChart.series[0].points[evt.target.index]);

                                }}}
                    }]
            });
            currentChart = new Highcharts.Chart({
                chart: {
                    type: 'area',
                    renderTo: "currentChart",
                    zoomType: "x",
                    events: {
                        load: function () {
                            currentTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                        }
                    }
                },
                title: {
                    text: ''
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    enabled: false
                },
                xAxis: {
                    title: {
                        text: "Current (mA)"
                    },
                    events: {
                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                            voltageChart.xAxis[0].setExtremes(currentChart.xAxis[0].getExtremes().min, currentChart.xAxis[0].getExtremes().max);
                            capacityChart.xAxis[0].setExtremes(currentChart.xAxis[0].getExtremes().min, currentChart.xAxis[0].getExtremes().max);
                            satChart.xAxis[0].setExtremes(currentChart.xAxis[0].getExtremes().min, currentChart.xAxis[0].getExtremes().max);
                            gsmChart.xAxis[0].setExtremes(currentChart.xAxis[0].getExtremes().min, currentChart.xAxis[0].getExtremes().max);
                            rcChart.xAxis[0].setExtremes(currentChart.xAxis[0].getExtremes().min, currentChart.xAxis[0].getExtremes().max);
                            altitudeChart.xAxis[0].setExtremes(currentChart.xAxis[0].getExtremes().min, currentChart.xAxis[0].getExtremes().max);
                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                console.log('gsmChart reset zoom');
                                $('.highcharts-button').hide();

                            } else {
                                altitudeChart.showResetZoom();
                                voltageChart.showResetZoom();
                                currentChart.showResetZoom();
                                capacityChart.showResetZoom();
                                satChart.showResetZoom();
                                gsmChart.showResetZoom();
                                rcChart.showResetZoom();
                                console.log('speedChart zoom-in');

                            }
                        }
                    }
                },
                animation: true,
                legend: {
                    enabled: false
                },
                series: [{
                        name: 'Current',
                        data: <jsp:getProperty name="kopterSessionInfo" property = "currentSerie"/>,
                        color: '#3880aa',
                        fillOpacity: 0.1,
                        lineWidth: 3,
                        turboThreshold: 0,
                        point: {
                            events: {
                                mouseOver: function (evt) {


                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);
                                    gsmTooltip.refresh(gsmChart.series[0].points[evt.target.index]);
                                    rcTooltip.refresh(rcChart.series[0].points[evt.target.index]);
                                    satTooltip.refresh(satChart.series[0].points[evt.target.index]);
                                    voltageTooltip.refresh(voltageChart.series[0].points[evt.target.index]);
                                    capacityTooltip.refresh(capacityChart.series[0].points[evt.target.index]);
                                    currentTooltip.refresh(currentChart.series[0].points[evt.target.index]);

                                }}}
                    }]
            });
            satChart = new Highcharts.Chart({
                chart: {
                    type: 'area',
                    renderTo: "satChart",
                    zoomType: "x",
                    events: {
                        load: function () {
                            satTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                        }
                    }
                },
                title: {
                    text: ''
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    enabled: false
                },
                xAxis: {
                    title: {
                        text: "Satellite Count"
                    },
                    events: {
                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                            voltageChart.xAxis[0].setExtremes(satChart.xAxis[0].getExtremes().min, satChart.xAxis[0].getExtremes().max);
                            capacityChart.xAxis[0].setExtremes(satChart.xAxis[0].getExtremes().min, satChart.xAxis[0].getExtremes().max);
                            currentChart.xAxis[0].setExtremes(satChart.xAxis[0].getExtremes().min, satChart.xAxis[0].getExtremes().max);
                            gsmChart.xAxis[0].setExtremes(satChart.xAxis[0].getExtremes().min, satChart.xAxis[0].getExtremes().max);
                            rcChart.xAxis[0].setExtremes(satChart.xAxis[0].getExtremes().min, satChart.xAxis[0].getExtremes().max);
                            altitudeChart.xAxis[0].setExtremes(satChart.xAxis[0].getExtremes().min, satChart.xAxis[0].getExtremes().max);
                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                console.log('gsmChart reset zoom');
                                $('.highcharts-button').hide();

                            } else {
                                altitudeChart.showResetZoom();
                                voltageChart.showResetZoom();
                                currentChart.showResetZoom();
                                capacityChart.showResetZoom();
                                satChart.showResetZoom();
                                gsmChart.showResetZoom();
                                rcChart.showResetZoom();
                                console.log('speedChart zoom-in');

                            }
                        }
                    }
                },
                animation: true,
                legend: {
                    enabled: false
                },
                series: [{
                        name: 'Satellite Count',
                        data: <jsp:getProperty name="kopterSessionInfo" property = "satSerie"/>,
                        color: '#3880aa',
                        fillOpacity: 0.1,
                        lineWidth: 3,
                        turboThreshold: 0,
                        point: {
                            events: {
                                mouseOver: function (evt) {


                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);
                                    gsmTooltip.refresh(gsmChart.series[0].points[evt.target.index]);
                                    rcTooltip.refresh(rcChart.series[0].points[evt.target.index]);
                                    satTooltip.refresh(satChart.series[0].points[evt.target.index]);
                                    voltageTooltip.refresh(voltageChart.series[0].points[evt.target.index]);
                                    capacityTooltip.refresh(capacityChart.series[0].points[evt.target.index]);
                                    currentTooltip.refresh(currentChart.series[0].points[evt.target.index]);

                                }}}
                    }]
            });
            gsmChart = new Highcharts.Chart({
                chart: {
                    type: 'area',
                    renderTo: "gsmSignalChart",
                    zoomType: "x",
                    events: {
                        load: function () {
                            gsmTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                        }
                    }
                },
                title: {
                    text: ''
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    enabled: false
                },
                xAxis: {
                    title: {
                        text: "GSM Signal Strength"
                    },
                    events: {
                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                            voltageChart.xAxis[0].setExtremes(gsmChart.xAxis[0].getExtremes().min, gsmChart.xAxis[0].getExtremes().max);
                            capacityChart.xAxis[0].setExtremes(gsmChart.xAxis[0].getExtremes().min, gsmChart.xAxis[0].getExtremes().max);
                            currentChart.xAxis[0].setExtremes(gsmChart.xAxis[0].getExtremes().min, gsmChart.xAxis[0].getExtremes().max);
                            satChart.xAxis[0].setExtremes(gsmChart.xAxis[0].getExtremes().min, gsmChart.xAxis[0].getExtremes().max);
                            rcChart.xAxis[0].setExtremes(gsmChart.xAxis[0].getExtremes().min, gsmChart.xAxis[0].getExtremes().max);
                            altitudeChart.xAxis[0].setExtremes(gsmChart.xAxis[0].getExtremes().min, gsmChart.xAxis[0].getExtremes().max);
                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                console.log('gsmChart reset zoom');
                                $('.highcharts-button').hide();

                            } else {
                                altitudeChart.showResetZoom();
                                voltageChart.showResetZoom();
                                currentChart.showResetZoom();
                                capacityChart.showResetZoom();
                                satChart.showResetZoom();
                                gsmChart.showResetZoom();
                                rcChart.showResetZoom();
                                console.log('speedChart zoom-in');

                            }
                        }
                    }
                },
                animation: true,
                legend: {
                    enabled: false
                },
                series: [{
                        name: 'GSM Signal Strength',
                        data: <jsp:getProperty name="kopterSessionInfo" property = "gsmSignalSerie"/>,
                        color: '#3880aa',
                        fillOpacity: 0.1,
                        lineWidth: 3,
                        turboThreshold: 0,
                        point: {
                            events: {
                                mouseOver: function (evt) {


                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);
                                    gsmTooltip.refresh(gsmChart.series[0].points[evt.target.index]);
                                    rcTooltip.refresh(rcChart.series[0].points[evt.target.index]);
                                    satTooltip.refresh(satChart.series[0].points[evt.target.index]);
                                    voltageTooltip.refresh(voltageChart.series[0].points[evt.target.index]);
                                    capacityTooltip.refresh(capacityChart.series[0].points[evt.target.index]);
                                    currentTooltip.refresh(currentChart.series[0].points[evt.target.index]);

                                }}}
                    }]
            });
            altitudeChart = new Highcharts.Chart({
                chart: {
                    renderTo: 'altitudeChart',
                    type: 'area',
                    zoomType: "x",
                    events: {
                        load: function () {
                            altitudeTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
                        }
                    }
                },
                title: {
                    text: ''
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    enabled: false
                },
                xAxis: {
                    title: {
                        text: ""
                    },
                    events: {
                        afterSetExtremes: function (evt) {
//                            console.log(altitudeChart.xAxis[0].getExtremes());

                            voltageChart.xAxis[0].setExtremes(altitudeChart.xAxis[0].getExtremes().min, altitudeChart.xAxis[0].getExtremes().max);
                            capacityChart.xAxis[0].setExtremes(altitudeChart.xAxis[0].getExtremes().min, altitudeChart.xAxis[0].getExtremes().max);
                            currentChart.xAxis[0].setExtremes(altitudeChart.xAxis[0].getExtremes().min, altitudeChart.xAxis[0].getExtremes().max);
                            satChart.xAxis[0].setExtremes(altitudeChart.xAxis[0].getExtremes().min, altitudeChart.xAxis[0].getExtremes().max);
                            rcChart.xAxis[0].setExtremes(altitudeChart.xAxis[0].getExtremes().min, altitudeChart.xAxis[0].getExtremes().max);
                            gsmChart.xAxis[0].setExtremes(altitudeChart.xAxis[0].getExtremes().min, altitudeChart.xAxis[0].getExtremes().max);
                            if (typeof evt.userMax === 'undefined' && typeof evt.userMin === 'undefined') {
                                console.log('altitudeChart reset zoom');
                                $('.highcharts-button').hide();

                            } else {
                                //altitudeChart.showResetZoom();
                                voltageChart.showResetZoom();
                                currentChart.showResetZoom();
                                capacityChart.showResetZoom();
                                satChart.showResetZoom();
                                gsmChart.showResetZoom();
                                rcChart.showResetZoom();
                                console.log('altitudeChart zoom-in');

                            }
                        }
                    }
                },
                animation: true,
                legend: {
                    enabled: false
                },
                series: [{
                        name: 'Altitude (m)',
                        data: <jsp:getProperty name="kopterSessionInfo" property = "altitudeSerie"/>,
                        color: '#3880aa',
                        fillOpacity: 0.1,
                        lineWidth: 3,
                        turboThreshold: 0,
                        point: {
                            events: {
                                mouseOver: function (evt) {


                                    altitudeTooltip.refresh(altitudeChart.series[0].points[evt.target.index]);
                                    gsmTooltip.refresh(gsmChart.series[0].points[evt.target.index]);
                                    rcTooltip.refresh(rcChart.series[0].points[evt.target.index]);
                                    satTooltip.refresh(satChart.series[0].points[evt.target.index]);
                                    voltageTooltip.refresh(voltageChart.series[0].points[evt.target.index]);
                                    capacityTooltip.refresh(capacityChart.series[0].points[evt.target.index]);
                                    currentTooltip.refresh(currentChart.series[0].points[evt.target.index]);

                                }}}
                    }]
            });

        }
        ;


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