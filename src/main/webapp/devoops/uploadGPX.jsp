<%-- 
    Document   : kopters
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="selectedMenu" value="logs"/>
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
                    ResultSet followMeResultset
                            = statement.executeQuery("SELECT * FROM `followme` AS f INNER JOIN followmedevices "
                                    + "ON f.followMeDeviceId = followmedevices.id "
                                    + "GROUP BY f.followMeDeviceId ORDER BY `time` DESC");
                %>
                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="routes.jsp">Routes</a></li>
                            <li><a href="#">Upload GPX</a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                <div id="results" class="col-xs-12">
                    </div>
                </div>

                <div class="row">
                    <div id="uploadGPX" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Upload a GPX file</span>
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
                                <p>FollowMe Device</p>
                                <div class="row form-group">
                                    <div class="col-sm-4">
                                        <form enctype="multipart/form-data">
                                            <select id="uid" name="uid" class="form-control">
                                                <% while (followMeResultset.next()) {
                                                        out.println("<option value=\"" + followMeResultset.getString("UID") + "\">"
                                                                + followMeResultset.getInt("followMeDeviceId") + ": "
                                                                + followMeResultset.getString("name") + "</option>");
                                                    }%>	

                                            </select>

                                            <label class="btn btn-primary" for="my-file-selector">
                                                <input id="my-file-selector" type="file" accept=".gpx" style="display:none;" onchange="$('#upload-file-info').html($(this).val());"/>
                                                Browse GPX File 
                                            </label>
                                                    <span class='label label-primary' id="upload-file-info"></span>
                                        </form>
                                        
                                        <span class='label label-info' id="upload-info"></span>
                                        <button class="btn btn-primary"
                                                onClick="uploadFile()">
                                            Upload
                                        </button>
                                    </div>
                                </div>


                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </div>

    <script>
        function uploadFile() {
            var files = document.getElementById('my-file-selector');
            var uid = document.getElementById('uid').value;
            var blobfile = files.files[0];
            var fd = new FormData();
            fd.append("file", blobfile);
            fd.append("uid", uid);

            $.ajax({
                url: "../SaveGpxFile",
                type: "POST",
                data: fd,
                processData: false,
                contentType: false,
                success: function (response) {
                    console.log("upload response: " + response)
                    if (response == "1") {
                        $("#results").html("<div class=\"alert alert-success\" class=\"col-xs-12\">\n\
                         <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a>\n\
                        <strong>Success!</strong> File uploaded successfully.</div>");
                    }else{
                        $("#results").html("<div class=\"alert alert-danger\" class=\"col-xs-12\">\n\
                         <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a>\n\
                        <strong>Failed!</strong> There was an error during upload process. Error code is: " + response +"</div>");
                    }
                },
                error: function (jqXHR, textStatus, errorMessage) {
                    console.log(errorMessage); // Optional
                }
            });
        }
    </script>

</body>
</html>    
<%@ include file="foot.jsp" %> 