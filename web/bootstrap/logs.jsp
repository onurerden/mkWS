<%-- 
    Document   : logs
    Created on : 10.Eki.2014, 10:14:27
    Author     : oerden
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<%@ include file="databaseconnection.jsp" %> 

<c:set var="ipAddressOfClient" value="${pageContext.request.remoteAddr}"/>
<c:set var="pageName" value="Loglar"/>

<sql:update dataSource="${snapshot}" var="count">
    INSERT INTO `logs`(`logLevel`, `message`, `datetime`) VALUES (0,?,CONVERT_TZ(NOW(),'-05:00','+02:00'));
    <sql:param value="loglar.jsp sayfasina ${ipAddressOfClient} IP'li cihazdan erisildi." />
</sql:update>

<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

    <title> Hisse Web Servis durumu</title>

    <!--Styles are here-->
    <%@include file="link.jsp" %>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

<div id="wrapper">



    <%@ include file="topmenu.jsp" %>                                 
    <%@ include file="sidebar.jsp" %> 

    <!-- /.navbar-static-side -->
</nav>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Loglar</h1>
        </div>
        <!-- /.col-lg-12 -->

        <sql:query dataSource="${snapshot}" var="inforesult">
            SELECT * FROM `logs` WHERE `logLevel`=3 ORDER BY datetime DESC LIMIT 10
        </sql:query>


        <sql:query dataSource="${snapshot}" var="errorresult">
            SELECT * FROM `logs` WHERE `logLevel`=1 ORDER BY datetime DESC LIMIT 10
        </sql:query>

        <sql:query dataSource="${snapshot}" var="webresult">
            SELECT * FROM `logs` WHERE `logLevel`=0 ORDER BY datetime DESC LIMIT 10
        </sql:query>


        <div class="row">
            <div class="col-lg-9">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        Loglar
                    </div>
                    <div class="panel-body">
                        <ul class="nav nav-tabs">
                            <li class="active">
                                <a href="#info" data-toggle="tab">İnfo</a>
                            </li>
                            <li>
                                <a href="#webinfo" data-toggle="tab">Web İnfo</a>
                            </li> 
                            <li>
                                <a href="#error" data-toggle="tab">Error</a>
                            </li>    
                        </ul>
                        <!--tab panes-->
                        <div class="tab-content">
                            <div class="tab-pane fade in active" id="info">
                                <h4>İnfo</h4>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Açıklama</th>
                                                <th>Tarih</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="row4" items="${inforesult.rows}">
                                                <tr>
                                                    <td><c:out value="${row4.id}"/></td>
                                                    <td><c:out value="${row4.message}"/></td>
                                                    <td><c:out value="${row4.datetime}"/></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                            </div>
                            <div class="tab-pane fade" id="webinfo">
                                <h4>Web İnfo</h4>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Açıklama</th>
                                                <th>Tarih</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="row6" items="${webresult.rows}">
                                                <tr>
                                                    <td><c:out value="${row6.id}"/></td>
                                                    <td><c:out value="${row6.message}"/></td>
                                                    <td><c:out value="${row6.datetime}"/></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                            </div> 
                            <div class="tab-pane fade" id="error">
                                <h4>Error</h4>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Açıklama</th>
                                                <th>Tarih</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="row5" items="${errorresult.rows}">
                                                <tr>
                                                    <td><c:out value="${row5.id}"/></td>
                                                    <td><c:out value="${row5.message}"/></td>
                                                    <td><c:out value="${row5.datetime}"/></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                            </div>   

                        </div>

                    </div>
                </div>
            </div>
        </div>

        <sql:query dataSource="${snapshot}" var="veriresult">
            SELECT * from veriler_dev order by id desc LIMIT 5;
        </sql:query>


        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        Son 5 Güncelleme
                    </div>
                    <!-- /.panel-heading -->
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Hisse ID</th>
                                        <th>Fiyatı</th>
                                        <th>Hacim</th>
                                        <th>Güncelleme ID</th>
                                        <th>En Yuksek</th>
                                        <th>En Düşük</th>
                                        <th>Değişim</th>
                                        <th>Güncelleme</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="row3" items="${veriresult.rows}">
                                        <tr>
                                            <td><c:out value="${row3.id}"/></td>
                                            <td><c:out value="${row3.hisse_id}"/></td>
                                            <td><c:out value="${row3.fiyati}"/></td>
                                            <td><c:out value="${row3.hacim}"/></td>
                                            <td><c:out value="${row3.guncelleme_id}"/></td>
                                            <td><c:out value="${row3.enyuksek}"/></td>
                                            <td><c:out value="${row3.endusuk}"/></td>
                                            <td><c:out value="${row3.degisim}"/></td>
                                            <td><c:out value="${row3.guncelleme}"/></td>
                                        </tr>
                                    </c:forEach>   

                                </tbody>
                            </table>
                        </div>
                        <!-- /.table-responsive -->
                    </div>
                    <!-- /.panel-body -->
                </div>
                <!-- /.panel -->
            </div>
            <!-- /.col-lg-6 -->

        </div>
        <!-- /.row -->
    </div>
</div>
</div>


<script src="js/jquery-1.11.0.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="js/plugins/metisMenu/metisMenu.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="js/sb-admin-2.js"></script>

<script src="js/plugins/typeahead/typeahead.js"></script>
<script src="js/autocomplete.js"></script>


</body>

</html>

