<%-- 
    Document   : sidebar
    Created on : 26.Eyl.2014, 17:21:57
    Author     : oerden
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="navbar-default sidebar" role="navigation">
    <div class="sidebar-nav navbar-collapse">
        <ul class="nav" id="side-menu">
            <li class="sidebar-search">
                <div class="input-group custom-search-form">
                    <!--                    <input type="text" class="form-control" placeholder="Ara...">-->
                    <form action="charts.jsp" method="GET">

                        <div id="searchbox">
                            <input name="hisse" class="typeahead form-control" type="text" placeholder="Ara..."/>
                        </div>
                        <span class="input-group-btn">
                            <button class="btn btn-default" type="submit">
                                <i class="fa fa-search"></i>
                            </button>
                        </span>

                    </form>
                </div>

                <!-- /input-group -->
            </li>

            <li>
                <c:choose>
                    <c:when test="${pageName=='AnaSayfa'}">
                        <a class="active" href="index.jsp"><i class="fa fa-table fa-fw"></i> Tablolar</a>
                    </c:when>
                    <c:otherwise>
                        <a href="index.jsp"><i class="fa fa-table fa-fw"></i> Tablolar</a>
                    </c:otherwise>                                                        
                </c:choose>

            </li>
            <li>
                <c:choose>
                    <c:when test="${pageName=='Çizelgeler'}">
                        <a class="active" href="charts.jsp"><i class="fa fa-bar-chart-o fa-fw"></i> Çizelgeler</a>
                    </c:when>
                    <c:otherwise>
                        <a href="charts.jsp"><i class="fa fa-bar-chart-o fa-fw"></i> Çizelgeler</a>
                    </c:otherwise>                                                        
                </c:choose>

            </li>
            <li>
                <c:choose>
                    <c:when test="${pageName=='Loglar'}">
                        <a class="active" href="logs.jsp"><i class="fa fa-list fa-fw"></i> Loglar</a>
                    </c:when>
                    <c:otherwise>
                        <a href="logs.jsp"><i class="fa fa-list fa-fw"></i> Loglar</a>
                    </c:otherwise>                                                        
                </c:choose>

            </li>

        </ul>
    </div>
    <!-- /.sidebar-collapse -->
</div>


