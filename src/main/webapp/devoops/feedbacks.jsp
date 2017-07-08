<%-- 
    Document   : orders
    Created on : 17.Mar.2015, 15:03:08
    Author     : oerden
--%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mkws.Credentials"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="selectedMenu" value="feedbacks"/>
<html>
    <%@ include file="head.jsp" %> 


    <div id="main" class="container-fluid">
        <div class="row">
            <%@ include file="nav.jsp" %>                                                 
            <div id="content" class="col-xs-12 col-sm-10">

                <div class="row">
                    <div id="breadcrumb" class="col-xs-12">
                        <ol class="breadcrumb">
                            <li><a href="index.jsp">Home</a></li>
                            <li><a href="#">Feedbacks</a></li>
                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div id="kopters" class="col-xs-12">
                        <div class="box">
                            <div class="box-header">
                                <div class="box-name">
                                    <i class="fa fa-table"></i>
                                    <span>Feedbacks</span>
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
                                <p>Feedbacks Listed Below</p>
                                <table id="feedbacks" class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Id</th>
                                            <th>User</th>
                                            <th>Message</th>
                                            <th>Feedback Time</th>

                                        </tr>
                                    </thead>
                                    <tbody>


                                    </tbody>

                                </table>

                            </div>
                        </div>
                    </div>

                </div>
                </body>
                <!-- <script src="https://code.jquery.com/jquery-1.9.1.min.js"></script> -->
                <%@ include file="foot.jsp" %> 
                <script type="text/javascript">
                    var selectedOrder = 0;

                    $(document).ready(function () {
                        WinMove();
                        getAllFeedbacks()
                    });
                    function setHeader(xhr) {
                        xhr.setRequestHeader('Authorization', 'Bearer ' + sessionStorage.getItem("access_token"));
                    }

                    function getAllFeedbacks(orderId) {
                        $.ajax({
                            url: '../api/GetFeedbacks',
                            type: 'GET',
                            dataType: 'json',
                            contentType: "json;charset=utf-8",
                            success: function (response) {
                                $('#feedbacks tbody').html("");
                                response.forEach(function (entry) {

                                    $('#feedbacks tbody').append('<tr>\
                                                                <td>' + entry.feedbackId + '</td>\
                                                                <td> ' + entry.uname + '</td>\
                                                                <td> ' + entry.message + '</td>\
                                                                <td> ' + entry.time + '</td></tr>');

                                });


                            },
                            error: function (error) {
                                alert('Hata!' + error.description);
                            },
                            beforeSend: setHeader

                        });
                    }


                </script>


                </html>

