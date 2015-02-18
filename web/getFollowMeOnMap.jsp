<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- 
    Document   : getFollowMeOnMap
    Created on : 13.Oca.2015, 11:43:06
    Author     : oerden
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Route Id: <% out.println(Integer.parseInt(request.getParameter("routeId")));%> </title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>


        <!--
            Connects Yandex.Maps API 2.x
            Parameters:
              - 'load' = package.full - full package;
                  - 'lang' = en-US - American English.
        -->

        <jsp:useBean
            id = "routeBean" class= "mkws.webbeans.GetRoutePoints">
        </jsp:useBean>

        <jsp:setProperty name="routeBean" property = "routeId" value="${param.routeId}"/>

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
    </head>

    <body>
        <h2>Route Id: <% out.println(Integer.parseInt(request.getParameter("routeId")));%> </h2>

        <div id="map" style="width:auto; height:500px"></div>

        <br/>
        <a href="/mkWS/getFollowMeOnMap.jsp?routeId="<% out.println(Integer.parseInt(request.getParameter("routeId")) - 1);%> > Önceki </a>
        <a href="/mkWS/getFollowMeOnMap.jsp?routeId="<% out.println(Integer.parseInt(request.getParameter("routeId")) + 1);%> > Sonraki </a>        
    </body>

</html>
