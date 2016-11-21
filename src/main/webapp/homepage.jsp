<%-- 
    Document   : index
    Created on : 08.Ara.2014, 12:12:26
    Author     : oerden
--%>

<%@page contentType="text/html" pageEncoding="windows-1250"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
        <title>Redirecting...</title>
        <link href="devoops/plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet">
    </head>
    <body>
        <h1>MK Portal!</h1>
        <a href="/GetRegisteredData?device=mk">Kopters</a><br>
        <a href="/GetRegisteredData?device=mp">Devices</a><br>
        <a href="/latestRoutes.jsp">Latest Routes</a><br>
        <a href="devoops/index.jsp">devoops</a><br>

        <script>
            window.location = "devoops";

            sessionStorage.setItem('access_token', getUrlParameter("token"));

            function getUrlParameter(sParam) {
                var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                        sURLVariables = sPageURL.split('&'),
                        sParameterName,
                        i;

                for (i = 0; i < sURLVariables.length; i++) {
                    sParameterName = sURLVariables[i].split('=');

                    if (sParameterName[0] === sParam) {
                        return sParameterName[1] === undefined ? true : sParameterName[1];
                    }
                }
            };

        </script>  

    </body>


</html>
