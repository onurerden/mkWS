<%-- 
    Document   : getFollowMeOnMap
    Created on : 13.Oca.2015, 11:43:06
    Author     : oerden
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Examples. Defining the style of polylines.</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <sql:query dataSource="${snapshot}" var="enyuksekhacim">
        SELECT 
    </sql:query> 
    
    <!--
        Connects Yandex.Maps API 2.x
        Parameters:
          - 'load' = package.full - full package;
	      - 'lang' = en-US - American English.
    -->
    
    <jsp:useBean
    id = "routeBean" class= "mkws.GetRoutePoints">
    
</jsp:useBean>
    

    
    
    
    <script src="http://api-maps.yandex.ru/2.0/?load=package.full&lang=en-US"
            type="text/javascript"></script>
    <script type="text/javascript">
        
        
        // Initializes the map as soon as the API is loaded and DOM is ready
        ymaps.ready(init);

        function init () {
            var myMap = new ymaps.Map("map", {
            center: [38.425218, 27.151823],        
            zoom: 10
                }),

                // A polyline
                myPolyline = new ymaps.Polyline([
                    // The coordinates of polyline vertices.
                    <jsp:getProperty name="routeBean" property="routePoints"/>
                ], {
                    // The balloon content
                    balloonContent: "Polyline"
                }, {
                    // Balloon options
                    // Disabling the "Close" button
                    balloonHasCloseButton: false,
                    // The line width
                    strokeWidth: 4,
                    // The line transparency
                    strokeOpacity: 0.5,
                    // Defines the color of the line - white
                    strokeColor: "#000000"
                }),

                myGeoObject = new ymaps.GeoObject({
                    // Geometry = the object type + its geo coordinates
                    geometry: {
                        // The object type is a line
                        type: "LineString",
                        // The coordinates of 3 points of the line.
                        coordinates: [
                            [38.432749, 27.173624],
                            [38.432884, 27.187872],
                            [38.427303, 27.212176],
                            [38.433152, 27.231302]
                        ]
                    },
                    // The geo object properties
                    properties: {
                        // The hint content
                        hintContent: "I'm a geo object",
                        // The balloon content
                        balloonContent: "You can drag me!"
                    }
                }, {
                    // Options
                    // Enables dragging.
                    draggable: true,
                    // Defines the color - yellow
                    strokeColor: "#FFFF00",
                    strokeWidth: 5
                });

            // Adds geo objects to the map 
            myMap.geoObjects
                .add(myPolyline)
                .add(myGeoObject);
        }
    </script>
</head>

<body>
<h2>Defining the style of polylines</h2>

<div id="map" style="width:600px;height:400px"></div>
<br/>
<jsp:useBean
    id = "toplam" class= "mkws.DenemeToplama">
    
</jsp:useBean>
    
<p>Deneme toplam =  <jsp:getProperty name="toplam" property="toplam"/>
</body>

</html>
