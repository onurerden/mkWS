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
                                    <div class="col-sm-6">
                                        <h4 class="page-header"><i class="fa fa-users"></i> User Count</h4>
                                        <div id="userCount">652</div>
                                    </div>
                                    <div class="col-sm-6">
                                        <h4 class="page-header"><i class="fa fa-map-marker"></i> Routes</h4>
                                        <div id="routeCount">853</div>
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

    <script type="text/javascript">
        $(document).ready(function () {
            WinMove();
            populateDashboard();
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