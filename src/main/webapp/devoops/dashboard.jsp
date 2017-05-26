<%-- 
    Document   : index
    Created on : 19.Mar.2015, 14:23:54
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<c:set var="selectedMenu" value="dashboard"/>
<html>
    <%@ include file="head.jsp" %> 

    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">
                <div class="row">
                    <div id="breadcrumb" class="col-md-12">
                        <ol class="breadcrumb">
                            <li><a href="#">Dashboard</a></li>

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

                            </div>
                            <div class="box-content">
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h4 class="page-header"><i class="fa fa-users"></i> User Count</h4>
                                        <div id="userCount">-</div>
                                    </div>
                                    <div class="col-sm-3">
                                        <h4 class="page-header"><i class="fa fa-map-marker"></i> Routes</h4>
                                        <div id="routeCount">-</div>
                                    </div>
                                    <div class="col-sm-3">
                                        <h4 class="page-header"><i class="fa fa-map-marker"></i> DB Size</h4>
                                        <div class="row">
                                            <div id="dbSize" class="col-xs-4">-</div>
                                            <div class="col-xs-8">
                                                <div id="donut-example" style="width:120px;height:120px;"></div>
                                                    
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>                                                
                    </div>
                </div>


            </div>
        </div>
    </div>

    <%@ include file="foot.jsp" %> 
    <script src="plugins/raphael/raphael-min.js"></script>
    <script src="plugins/morris/morris.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            WinMove();
            populateDashboard();
            Morris.Donut({
                element: 'donut-example',
                data: [
                    {label: "Download Sales", value: 0},
                    {label: "In-Store Sales", value: 0}                    
                ]
            });
        });
        function  populateDashboard() {
            $.ajax({
                url: '../api/DashboardInfo',
                type: 'GET',
                dataType: 'json',
                contentType: "json;charset=utf-8",
                success: function (response) {

                    $('#routeCount').html("");
                    $('#routeCount').append(response.routeCount);
                    $('#userCount').html("");
                    $('#userCount').append(response.userCount);
                    $('#dbSize').html("");
                    $('#dbSize').append(response.dbSize + " MB");
                    Morris.Donut({
                element: 'donut-example',
                resize : true,
                data: [
                {label: "Available Space", value: (1000 - response.dbSize)},         
                {label: "Used Space", value: response.dbSize}                                   
                ],
                colors: ["#95bbd7","#3980b5"]
            });
                    console.log(response);
                },
                error: function (error) {
                    alert('Hata!' + error.description);
                },
                beforeSend: setHeader

            });
        }
        function setHeader(xhr) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + sessionStorage.getItem("access_token"));
        }
    </script>
</body>
</html>