<%-- 
    Document   : head
    Created on : 19.Mar.2015, 14:25:21
    Author     : oerden
--%>
<%
    //  System.out.println("UserId: " + session.getAttribute("userid"));
    if (session.getAttribute("userid") == null) {
        response.sendRedirect("../index.html");
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<head>
    <meta charset="utf-8">
    <title>Follow Me GPS Tracker</title>
    <meta name="description" content="description">
    <meta name="author" content="DevOOPS">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="plugins/bootstrap/bootstrap.css" rel="stylesheet">
    <link href="plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet">
    <!-- <link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet"> -->
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link href='https://fonts.googleapis.com/css?family=Righteous' rel='stylesheet' type='text/css'>
    <link href="plugins/fancybox/jquery.fancybox.css" rel="stylesheet">
    <link href="plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="plugins/xcharts/xcharts.min.css" rel="stylesheet">
    <link href="plugins/select2/select2.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
                    <script src="http://getbootstrap.com/docs-assets/js/html5shiv.js"></script>
                    <script src="http://getbootstrap.com/docs-assets/js/respond.min.js"></script>
    <![endif]-->
</head><body>
    <!--Start Header-->
    <div id="screensaver">
        <canvas id="canvas"></canvas>
        <i class="fa fa-lock" id="screen_unlock"></i>
    </div>
    <div id="modalbox">
        <div class="devoops-modal">
            <div class="devoops-modal-header">
                <div class="modal-header-name">
                    <span>Basic table</span>
                </div>
                <div class="box-icons">
                    <a class="close-link">
                        <i class="fa fa-times"></i>
                    </a>
                </div>
            </div>
            <div class="devoops-modal-inner">
            </div>
            <div class="devoops-modal-bottom">
            </div>
        </div>
    </div>
    <header class="navbar">
        <div class="container-fluid expanded-panel">
            <div class="row">
                <div id="logo" class="col-xs-12 col-sm-2">
                    <a href="index.jsp">mkWS</a>
                </div>
                <div id="top-panel" class="col-xs-12 col-sm-10">
                    <div class="row">
                        <div class="col-xs-8 col-sm-4">
                            <a href="#" class="show-sidebar">
                                <i class="fa fa-bars"></i>
                            </a>
                            <div id="search">
                                <input type="text" placeholder="search"/>
                                <i class="fa fa-search"></i>
                            </div>
                        </div>
                        <div class="col-xs-4 col-sm-8 top-panel-right">
                            <ul class="nav navbar-nav pull-right panel-menu">
                                <li class="hidden-xs">
                                    <a href="index.jsp" class="modal-link">
                                        <i class="fa fa-bell"></i>
                                        <span class="badge">7</span>
                                    </a>
                                </li>
                                <li class="hidden-xs">
                                    <a class="ajax-link" href="ajax/calendar.html">
                                        <i class="fa fa-calendar"></i>
                                        <span class="badge">7</span>
                                    </a>
                                </li>
                                <li class="hidden-xs">
                                    <a href="ajax/page_messages.html" class="ajax-link">
                                        <i class="fa fa-envelope"></i>
                                        <span class="badge">7</span>
                                    </a>
                                </li>
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle account" data-toggle="dropdown">
                                        <div class="avatar">
                                            <img src="img/avatar.jpg" class="img-rounded" alt="avatar" />
                                        </div>
                                        <i class="fa fa-angle-down pull-right"></i>
                                        <div class="user-mini pull-right">
                                            <span class="welcome">Welcome,</span>
                                            <span><% out.println(session.getAttribute("first_name") + " " + session.getAttribute("last_name"));%></span>
                                        </div>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a href="./mapmyride.jsp">
                                                <i class="fa fa-bicycle"></i>
                                                <span>MapMyRide</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="../logout.jsp">
                                                <i class="fa fa-power-off"></i>
                                                <span>Logout</span>
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>
    <!--End Header-->
