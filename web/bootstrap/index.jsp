<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>


<%--<%@ include file="databaseconnection.jsp" %> --%>

<c:set var="ipAddressOfClient" value="${pageContext.request.remoteAddr}"/>
<c:set var="pageName" value="AnaSayfa"/>

<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

    <title>Hisse Web Servis durumu</title>

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
            <h1 class="page-header">Webservis Rapor</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->

    <div class="row">
        <div class="col-lg-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Borsa Performansı
                </div>

                <div class="panel-body">
                    <div id="morris-borsa-performans"></div>
                </div>
            </div>
        </div>
    </div>



    <sql:query dataSource="${snapshot}" var="dusen">
        SELECT v3.hisse_adi, v1.fiyati, v1.degisim 
        FROM latestdata AS v1
        INNER JOIN hisseler AS v3 ON v1.hisse_id = v3.id
        ORDER BY degisim ASC
        LIMIT 5
    </sql:query>
    <sql:query dataSource="${snapshot}" var="yukselen">
        SELECT v3.hisse_adi, v1.fiyati, v1.degisim 
        FROM latestdata AS v1
        INNER JOIN hisseler AS v3 ON v1.hisse_id = v3.id
        ORDER BY degisim DESC
        LIMIT 5
    </sql:query>
    <sql:query dataSource="${snapshot}" var="enyuksekhacim">
        SELECT v3.hisse_adi, v1.fiyati, v1.hacim 
        FROM latestdata AS v1
        INNER JOIN hisseler AS v3 ON v1.hisse_id = v3.id
        ORDER BY hacim DESC
        LIMIT 5
    </sql:query> 



    <sql:query dataSource="${snapshot}" var="degismeyensay">
        SELECT COUNT(`id`) AS say FROM latestdata where degisim = 0 AND DATE(guncelleme)=CURDATE()
    </sql:query> 
    <sql:query dataSource="${snapshot}" var="yukselensay">
        SELECT COUNT(`id`) AS say FROM latestdata where degisim > 0 AND DATE(guncelleme)=CURDATE()
    </sql:query> 
    <sql:query dataSource="${snapshot}" var="dusensay">
        SELECT COUNT(`id`) AS say FROM latestdata where degisim < 0 AND DATE(guncelleme)=CURDATE()
    </sql:query> 

    <div class="row">
        <div class="col-lg-4">
            <div class="panel panel-green">
                <div class="panel-heading">
                    Yükselenler
                </div>

                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Hisse Adı</th>
                                    <th>Fiyatı</th>
                                    <th>Değişim</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="row7" items="${yukselen.rows}">
                                    <tr>
                                        <td><a href="charts.jsp?hisse=<c:out value="${row7.hisse_adi}"/>"><c:out value="${row7.hisse_adi}"/></a></td>
                                        <td><c:out value="${row7.fiyati}"/></td>
                                        <td><c:out value="${row7.degisim}"/></td>
                                    </tr>

                                </c:forEach>


                            </tbody>
                        </table>
                    </div>
                </div>


            </div>
        </div>

        <div class="col-lg-4">
            <div class="panel panel-red">
                <div class="panel-heading">
                    Düşenler
                </div>

                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Hisse Adı</th>
                                    <th>Fiyatı</th>
                                    <th>Değişim</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="row8" items="${dusen.rows}">
                                    <tr>
                                        <td><a href="charts.jsp?hisse=<c:out value="${row8.hisse_adi}"/>"><c:out value="${row8.hisse_adi}"/></a></td>
                                        <td><c:out value="${row8.fiyati}"/></td>
                                        <td><c:out value="${row8.degisim}"/></td>
                                    </tr>

                                </c:forEach>


                            </tbody>
                        </table>
                    </div>
                </div>


            </div>
        </div>
        <div class="col-lg-4">
            <div class="panel panel-success">
                <div class="panel-heading">
                    En Yüksek Hacim
                </div>

                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Hisse Adı</th>
                                    <th>Fiyatı</th>
                                    <th>Hacim</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="row9" items="${enyuksekhacim.rows}">
                                    <tr>
                                        <td><a href="charts.jsp?hisse=<c:out value="${row9.hisse_adi}"/>"><c:out value="${row9.hisse_adi}"/></a></td>
                                        <td><c:out value="${row9.fiyati}"/></td>
                                        <td class="text-right"><c:out value="${row9.hacim}"/></td>
                                    </tr>

                                </c:forEach>


                            </tbody>
                        </table>
                    </div>
                </div>


            </div>
        </div>


    </div>
    <!-- /.row -->


</div>
<!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<!-- jQuery Version 1.11.0 -->
<script src="js/jquery-1.11.0.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="js/plugins/metisMenu/metisMenu.min.js"></script>

<!--                         DataTables JavaScript 
                        <script src="js/plugins/dataTables/jquery.dataTables.js"></script>
                        <script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>-->

<!-- Custom Theme JavaScript -->
<script src="js/sb-admin-2.js"></script>

<script src="js/plugins/typeahead/typeahead.js"></script>
<script src="js/autocomplete.js"></script>

<script src="//cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
<script src="http://cdn.oesmith.co.uk/morris-0.5.1.min.js"></script>

<script>
    $(document).ready(function() {
        Morris.Donut({
            element: 'morris-borsa-performans',
            data: [
                {label: "Yükselen", value: <c:forEach var="row11" items="${yukselensay.rows}">
                    <c:out value="${row11.say}"/>
                </c:forEach>},
                {label: "Düşen", value: <c:forEach var="row12" items="${dusensay.rows}">
                    <c:out value="${row12.say}"/>
                </c:forEach>},
                {label: "Değişmeyen", value: <c:forEach var="row13" items="${degismeyensay.rows}">
                    <c:out value="${row13.say}"/>
                </c:forEach>}
            ],
            colors: [
                '#5cb85c',
                '#d9534f',
                '#F7FA39'
              ],
              resize:true
        });
    })
</script>


<!-- Page-Level Demo Scripts - Tables - Use for reference -->
<!--                        <script>
                            $(document).ready(function() {
                                $('#dataTables-example').dataTable();
                            });
                        </script>-->

</body>

</html>
