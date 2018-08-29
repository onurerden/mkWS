<%-- 
    Document   : login
    Created on : Jun 23, 2016, 10:02:09 AM
    Author     : oerden
--%>

<%@page import="mkws.Model.Token"%>
<%@page import="mkws.ServerEngine"%>
<%@page import="mkws.Credentials"%>
<%@ page import ="java.sql.*" %>
<%
    String userid = request.getParameter("username");    
    String pwd = request.getParameter("password");
    Class.forName("com.mysql.jdbc.Driver");
    Credentials cr = new Credentials();
    Connection con = DriverManager.getConnection(cr.getMysqlConnectionString(),cr.getDbUserName(),cr.getDbPassword());
    Statement st = con.createStatement();
    ResultSet rs;
    rs = st.executeQuery("select * from members where uname='" + userid + "' and pass=MD5('" + pwd + "')");
    if (rs.next()) {
        String first_name = rs.getString("first_name");
        String last_name = rs.getString("last_name");
        boolean isAdmin = rs.getBoolean("isAdmin");
        session.setAttribute("userid", userid);
        session.setAttribute("isGuest",false);
        session.setAttribute("id", rs.getInt("id"));
        session.setAttribute("first_name",first_name);
        session.setAttribute("last_name",last_name);
        session.setAttribute("isAdmin",isAdmin);
        //out.println("welcome " + userid);
        //out.println("<a href='logout.jsp'>Log out</a>");
        ServerEngine server = new ServerEngine();
        String token = server.createTokenForUser(rs.getInt("id"));
        response.sendRedirect("homepage.jsp?token="+token);
    } else {

        response.sendRedirect("index.html?errorMessage=invalidCredentials");
        out.println("Invalid login credentials. <a href='index.html'>Try again</a>.");
    }
    con.close();
%>