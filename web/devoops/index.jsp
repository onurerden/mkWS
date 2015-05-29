<%-- 
    Document   : index
    Created on : 19.Mar.2015, 14:23:54
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<c:set var="selectedMenu" value="anasayfa"/>
<html>
    <%@ include file="head.jsp" %> 
<style>
      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
      }
    </style>
    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">
                <div class="row">
                    <div id="breadcrumb" class="col-md-12">
                        <ol class="breadcrumb">
                            <li><a href="#">Home</a></li>

                        </ol>
                    </div>
                </div>
                
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box">
                                <div class="box-header">
                                    <div class="box-name">
                                        <i class="fa fa-table"></i>
                                        <span>Welcome</span>
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
                                    denemedir, şöyle denemedir böyle denemedir
                                </div>
                            </div>                                                
                        </div>
                    </div>
                    <!-- map here -->
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box">
                                <div class="box-header">
                                    <div class="box-name">
                                        <i class="fa fa-table"></i>
                                        <span>chart example</span>
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
                                    <div id="map-canvas" style="height: 350px"></div>
                                </div>
                            </div>                                                
                        </div>
                    </div>
                
            </div>
        </div>

        <%@ include file="foot.jsp" %> 

        
        
        <script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
    <script>
var map;
function initialize() {
  var mapOptions = {
    zoom: 8,
    center: new google.maps.LatLng(-34.397, 150.644)
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
}

google.maps.event.addDomListener(window, 'load', initialize);

    </script>

        
<script type="text/javascript">
    $(document).ready(function() {
WinMove();
    });
</script>
    </body>
</html>