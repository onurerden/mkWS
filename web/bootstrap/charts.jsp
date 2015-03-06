<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="databaseconnection.jsp" %>


<c:set var="ipAddressOfClient" value="${pageContext.request.remoteAddr}"/>
<c:set var="pageName" value="Çizelgeler"/>

<sql:update dataSource="${snapshot}" var="count">
    INSERT INTO `logs`(`logLevel`, `message`, `datetime`) VALUES (0,?,CONVERT_TZ(NOW(),'-05:00','+02:00'));
    <sql:param value="Charts.jsp sayfasina ${ipAddressOfClient} IP'li cihazdan erisildi." />
</sql:update>

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


<c:choose>
    <c:when test="${empty param['hisse']}">
        <c:set var="hisseadi" value="HLGYO"/>
    </c:when>
    <c:otherwise>
        <c:set var="hisseadi" value="${fn:toUpperCase(param['hisse'])}"/>
    </c:otherwise>
</c:choose>

<sql:query dataSource="${snapshot}" var="gunluksorgu">
    SELECT unix_timestamp(guncelleme)*1000 AS time, fiyati, guncelleme
    FROM veriler_dev
    INNER JOIN
    hisseler AS h ON h.id = veriler_dev.hisse_id
    WHERE h.hisse_adi = ? AND guncelleme > CURDATE()
    ORDER BY time DESC
    <sql:param value="${hisseadi}" />
</sql:query>
<sql:query dataSource="${snapshot}" var="ayliksorgu">
    SELECT unix_timestamp(guncelleme)*1000 AS time, fiyati, guncelleme
    FROM veriler_temp
    INNER JOIN
    hisseler AS h ON h.id = veriler_temp.hisse_id
    WHERE h.hisse_adi = ? AND guncelleme BETWEEN SUBDATE(CURDATE(), INTERVAL 1 MONTH) AND NOW()
    ORDER BY time DESC
    <sql:param value="${hisseadi}" />
</sql:query>
<sql:query dataSource="${snapshot}" var="yilliksorgu">
    SELECT unix_timestamp(guncelleme)*1000 AS time, fiyati, guncelleme
    FROM veriler_temp
    INNER JOIN
    hisseler AS h ON h.id = veriler_temp.hisse_id
    WHERE h.hisse_adi = ? AND guncelleme BETWEEN SUBDATE(CURDATE(), INTERVAL 1 YEAR) AND NOW()
    ORDER BY time DESC
    <sql:param value="${hisseadi}" />
</sql:query>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Çizelgeler</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->

    <div class="row">
        <div class="col-lg-8">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <c:out value="${hisseadi}"/>
                </div>
                <div class="panel-body">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#gunluk" data-toggle="tab">Günlük</a>
                        </li>
                        <li>
                            <a href="#aylik" data-toggle="tab">Aylık</a>
                        </li>
                        <li>
                            <a href="#yillik" data-toggle="tab">Yıllık</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="gunluk">
                            <div class="flot-chart">
                                <div id="gunlukdata" class="flot-chart-content" style="padding: 0px; position: relative;"></div>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="aylik">
                            <div class="flot-chart">
                                <div id="aylikdata" class="flot-chart-content" style="padding: 0px; position: relative;"></div>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="yillik">
                            <div class="flot-chart">
                                <div id="yillikdata" class="flot-chart-content" style="padding: 0px; position: relative;"></div>
                            </div>
                        </div>
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

<!-- Flot Plugin -->
<script src="js/plugins/flot/excanvas.min.js"></script>
<!--<script src="js/plugins/flot/flot-data.js"></script>-->
<script src="js/plugins/flot/jquery.flot.js"></script>
<script src="js/plugins/flot/jquery.flot.pie.js"></script>
<script src="js/plugins/flot/jquery.flot.resize.js"></script>
<script src="js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script src="js/plugins/flot/jquery.flot.time.js"></script>
<script src="js/plugins/flot/date.js"></script>



<!-- DataTables JavaScript -->
<script src="js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>


<!-- Custom Theme JavaScript -->
<script src="js/sb-admin-2.js"></script>

<script src="js/plugins/typeahead/typeahead.js"></script>
<script src="js/autocomplete.js"></script>

<script>
            $(document).ready(function() {
    timezoneJS.timezone.zoneFileBasePath = "tz";
            timezoneJS.timezone.defaultZoneFile = [];
            timezoneJS.timezone.init({async: false});
            $.plot($("#gunlukdata"), [{
            label: "Günlük",
                    data: [
    <c:set var="fordegisken" value="0"/>
    <c:forEach var="row1" items="${gunluksorgu.rows}">
                    [ <c:out value="${row1.time}"/>, <c:out value="${row1.fiyati}"/> ],
        <%--[ <c:out value="${fordegisken}"/>, <c:out value="${row1.fiyati}"/> ],--%>
        <c:set var="fordegisken" value="${fordegisken+1}"/>
    </c:forEach>
                    ]}], {
            series: {
            lines: {
            show: true
            },
                    points: {
                    show: true
                    }
            },
                    grid: {
                    hoverable: true //IMPORTANT! this is needed for tooltip to work
                    },
                    tooltip: true,
                    tooltipOpts: {
                    content: 'Fiyat = %y.0',
                            shifts: {
                            x: - 60,
                                    y: 25
                            }
                    },
                    xaxis: {
                    mode: "time",
                            timezone: "America/Detroit"
                    },
                    yaxis:
                    {
                    tickDecimals: 2
                            }
            }
            );
    });</script>
<script>
            $(document).ready(function() {
    timezoneJS.timezone.zoneFileBasePath = "tz";
            timezoneJS.timezone.defaultZoneFile = [];
            timezoneJS.timezone.init({async: false});
            $.plot($("#aylikdata"), [ {
            label: "Aylık",
                    data: [
    <c:set var="fordegisken2" value="0"/>
    <c:forEach var="row2" items="${ayliksorgu.rows}">
                    [ <c:out value="${row2.time}"/>, <c:out value="${row2.fiyati}"/> ],
        <%--[ <c:out value="${fordegisken}"/>, <c:out value="${row1.fiyati}"/> ],--%>
        <c:set var="fordegisken2" value="${fordegisken2+1}"/>
    </c:forEach>
                    ]}], {series: {
            lines: {
            show: true
            },
                    points: {
                    show: true
                    }
            },
                    grid: {
                    hoverable: true //IMPORTANT! this is needed for tooltip to work
                    },
                    tooltip: true,
                    tooltipOpts: {
                    content: 'Fiyat = %y.0',
                            shifts: {
                            x: - 60,
                                    y: 25
                            }
                    },
                    xaxis: {
                    mode: "time",
                            timezone: "America/Detroit"
                    },
                    yaxis:
                    {
                    tickDecimals: 2
                            }
            }
            );
    });</script>
<script>
            $(document).ready(function() {
    timezoneJS.timezone.zoneFileBasePath = "tz";
            timezoneJS.timezone.defaultZoneFile = [];
            timezoneJS.timezone.init({async: false});
            $.plot($("#yillikdata"), [ {
            label:"Yıllık",
                    data:[
    <c:set var="fordegisken3" value="0"/>
    <c:forEach var="row3" items="${yilliksorgu.rows}">
                    [ <c:out value="${row3.time}"/>, <c:out value="${row3.fiyati}"/> ],
        <%--[ <c:out value="${fordegisken}"/>, <c:out value="${row1.fiyati}"/> ],--%>
        <c:set var="fordegisken3" value="${fordegisken3+1}"/>
    </c:forEach>
                    ]}], {
            series: {
            lines: {
            show: true
            },
                    points: {
                    show: true
                    }
            },
                    grid: {
                    hoverable: true //IMPORTANT! this is needed for tooltip to work
                    },
                    tooltip: true,
                    tooltipOpts: {
                    content: 'Fiyat = %y.0',
                            shifts: {
                            x: - 60,
                                    y: 25
                            }
                    },
                    xaxis: {
                    mode: "time",
                            timezone: "America/Detroit"
                    },
                    yaxis:
                    {
                    tickDecimals: 2
                            }
            }
            );
    });</script>    
</body>
</html>
