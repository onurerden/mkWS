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
                    id = "kopterStatus" class= "mkws.webbeans.GetKopterStatus">
                </jsp:useBean>
                <jsp:setProperty name="kopterStatus" property = "kopterId" value="${param.kopterId}"/>
                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="../index.html">Home</a></li>
                            <li><a href="kopters.jsp">Kopters</a></li>
                            <li><a href="#">Kopter #<% out.println(Integer.parseInt(request.getParameter("kopterId")));%></a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="routes" class="col-xs-12 col-md-8">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>KopterStatus for <% out.println(kopterStatus.getKopterId());%></span>
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
                                <div class="row">
                                    <div class="col-xs-4">
                                        Voltage: <c:out value="${kopterStatus.kopterVoltage/10}"/> V
                                    </div>
                                    <div class="col-xs-4">    
                                        <div class="progress progress-striped active">
                                            <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="<c:out value="${100*(kopterStatus.kopterVoltage-135)/(170-135)}" />" aria-valuemin="13.5" aria-valuemax="17" style="width:<c:out value="${100*(kopterStatus.kopterVoltage-135)/(170-135)}" />% ;">
                                                <span><c:out value="${kopterStatus.kopterVoltage/10}"/> V </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-4">
                                        Consumed Battery: <jsp:getProperty name="kopterStatus" property="batteryCapacity"/> mAh
                                    </div>
                                    <div class="col-xs-4">
                                        <div class="progress progress-striped active">
                                            <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="<c:out value="${100*(kopterStatus.batteryCapacity)/(5000)}" />"  aria-valuemin="0" aria-valuemax="5000" style="width:<c:out value="${100*(kopterStatus.batteryCapacity)/(5000)}" />% ;">
                                                <span><c:out value="${kopterStatus.batteryCapacity}"/> mAh </span>
                                            </div>
                                        </div> 
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- list of sessions-->
                    <div id="sessionList" class="col-xs-12 col-md-4">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Session Summaries</span>
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

                                <% Class.forName("com.mysql.jdbc.Driver"); %>
                                <%
                                    Credentials cr = new Credentials();
                                    Connection connection = DriverManager.getConnection(
                                            cr.getMysqlConnectionString(),cr.getDbUserName(),cr.getDbPassword());
                                            //cr.getMysqlConnectionString(), "onur", "19861986");

                                    Statement statement = connection.createStatement();
                                    ResultSet resultset
                                            = statement.executeQuery("SELECT sessionId, updateTime from mk.kopterstatus "
                                                    + "WHERE kopterId=" + request.getParameter("kopterId") + " "
                                                    + "GROUP BY sessionId "
                                                    + "ORDER BY id DESC");
                                %>
                                <table class="table table-heading table-datatable" id="sessionListTable">
                                    <thead>
                                        <tr>
                                            <th>SessionId</th>
                                            <th>Session Start Time</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% while (resultset.next()) {%>
                                        <tr>
                                        <!--    <td><a href="kopterSessionSummary.jsp?sessionId=<%= resultset.getInt("sessionId")%>"><%= resultset.getInt("sessionId")%></a></td>
                                            -->
                                            <td><a href="kopterSessionSummary.jsp?sessionId=<%= resultset.getInt("sessionId")%>">
                                                <%= resultset.getInt("sessionId")%>
                                                </a>
                                            </td>
                                            <td><a href="kopterSessionSummary.jsp?sessionId=<%= resultset.getInt("sessionId")%>"><%= resultset.getTimestamp("updateTime")%></a></td>

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
    <%@include file="foot.jsp" %>
        
    <script>
        function AllTables() {
            $('#sessionListTable').dataTable({
                "aaSorting": [[0, "desc"]],
                "sDom": "<'box-content'<'col-sm-6'f><'col-sm-6 text-right'l><'clearfix'>>rt<'box-content'<'col-sm-6'i><'col-sm-6 text-right'p><'clearfix'>>",
                "sPaginationType": "bootstrap",
                
                "oLanguage": {
                    "sSearch": "",
                    "sLengthMenu": '_MENU_',
                    "sInfo": 'Showing _START_ to _END_ of _TOTAL_ sessions'
                }
            });
            LoadSelect2Script(MakeSelect2);
        }
        function MakeSelect2() {
            $('select').select2();
            $('.dataTables_filter').each(function() {
                $(this).find('label input[type=text]').attr('placeholder', 'Search');
            });
        }
        $(document).ready(function() {



            LoadDataTablesScripts(AllTables);
            WinMove();
        });
    </script>
</body>
</html>
