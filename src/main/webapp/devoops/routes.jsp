<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="selectedMenu" value="routes"/>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp" %> 

    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">

                <%// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); %>
                <% Class.forName("com.mysql.jdbc.Driver").newInstance(); %>
                <% 
                    Credentials cr = new Credentials();
                    Connection connection = DriverManager.getConnection(
                            //cr.getMysqlConnectionString(),cr.getDbUserName(),cr.getDbPassword());
                            cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());

                    Statement statement = connection.createStatement();
                    //ResultSet resultset
                    //      = statement.executeQuery("SELECT followme.routeId, followmedevices.name, route.time, route.isEnded FROM  followme "
                    //            + "INNER JOIN followmedevices ON followme.followMeDeviceId = followmedevices.id "
                    //          + "INNER JOIN route ON followme.routeId = route.id "
                    //        + "GROUP BY followme.routeId "
                    //      + "ORDER BY time DESC");
                    ResultSet resultset
                            = statement.executeQuery("SELECT followme.routeId, followmedevices.name, route.time, route.isEnded FROM  followme "
                                    + "INNER JOIN followmedevices ON followme.followMeDeviceId = followmedevices.id "
                                    + "INNER JOIN route ON followme.routeId = route.id "
                                    + "WHERE route.isDeleted=FALSE "
                                    + "GROUP BY followme.routeId "
                                    + "ORDER BY time DESC");

                %>
                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="#">Routes</a></li>
                        </ol>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${param.message=='dr'}">
                        <div class="row">
                            <div class="alert alert-success" class="col-xs-12">
                                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                <strong>Success!</strong> Route successfully deleted.
                            </div>
                        </div>
                    </c:when>    
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>


                <div class="row">
                    <div id="routes" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Routes</span>
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
                                <p>Routes defined in mkWS system listed below:</p>
                                <!--   <a href ="#" onclick="load_home()"> HOME </a> </div>
               <script>
                                   function load_home(){
   document.getElementById("content").innerHTML='<object type="text/html" data="mkWS/getFollowMeOnMap.jsp?routeId=171" ></object>';
   }
           </script> -->
                                <table id="routeTable" class="table table-datatable table-heading">

                                    <thead>
                                        <tr>
                                            <th>Route Id</th>
                                            <th>Device Name</th>
                                            <th>DateTime</th>

                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% while (resultset.next()) {%>
                                        <tr>
                                            <td><a href="getFollowMeOnMap.jsp?routeId=<%= resultset.getInt("routeId")%>">
                                                    <%= resultset.getInt("routeId")%></a></td>
                                            <td><%= resultset.getString("name")%></td>
                                            <td><%= resultset.getTimestamp("time")%>
                                                <% if (resultset.getInt("isEnded") == 0) {%>
                                                <a href="getFollowMeOnMap.jsp?routeId=<%= resultset.getInt("routeId")%>" role="button" class="btn btn-danger">Live</a>
                                                <% }%>
                                            </td>
                                            <td>
                                                <button class="btn btn-primary"
                                                        onClick="deleteRoute(<%=resultset.getInt("routeId")%>)">
                                                    Delete
                                                </button>
                                            </td>


                                        </tr>
                                        <% }%>

                                    </tbody>
                                    <% connection.close();%>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </div>
    <%@ include file="foot.jsp" %>         
    <script>
        function AllTables() {
            $('#routeTable').dataTable({
                "aaSorting": [[0, "desc"]],
                "sDom": "<'box-content'<'col-sm-6'f><'col-sm-6 text-right'l><'clearfix'>>rt<'box-content'<'col-sm-6'i><'col-sm-6 text-right'p><'clearfix'>>",
                "sPaginationType": "bootstrap",
                "oLanguage": {
                    "sSearch": "",
                    "sLengthMenu": '_MENU_',
                    "sInfo": 'Showing _START_ to _END_ of _TOTAL_ routes'
                }
            });
            LoadSelect2Script(MakeSelect2);
        }
        function MakeSelect2() {
            $('select').select2();
            $('.dataTables_filter').each(function () {
                $(this).find('label input[type=text]').attr('placeholder', 'Search');
            });
        }

        function deleteRoute(routeId) {
            console.log("delete clicked for route: " + routeId);
            var link = "../DeleteRoute?routeId=" + routeId;
            console.log(link);
            $.ajax({url: link, success: function (result) {

                    var info = JSON.parse(result);

                    console.log(info);

                    if (info.result === "success") {
                        window.location.replace("./routes.jsp?message=dr");
                    }
                }});

        }
        $(document).ready(function () {



            LoadDataTablesScripts(AllTables);
            WinMove();
        });
    </script>
</body>
</html>    
