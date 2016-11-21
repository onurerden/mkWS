<%-- 
    Document   : logout
    Created on : Jun 23, 2016, 10:32:54 AM
    Author     : oerden
--%>

<%
session.setAttribute("userid", null);
session.setAttribute("isAdmin", false);
session.setAttribute("access_token", "");
session.invalidate();
response.sendRedirect("index.html?logout=yes");
%>