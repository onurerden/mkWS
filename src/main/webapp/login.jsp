<%-- 
    Document   : login
    Created on : Jun 23, 2016, 10:02:09 AM
    Author     : oerden
--%>

<%@ page import ="java.sql.*" %>
<%
    String userid = request.getParameter("username");    
    String pwd = request.getParameter("password");
    Class.forName("com.mysql.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mk",
            "adminHr8UXvV", "UrtgKUvL3deC");
    Statement st = con.createStatement();
    ResultSet rs;
    rs = st.executeQuery("select * from members where uname='" + userid + "' and pass='" + pwd + "'");
    if (rs.next()) {
        String first_name = rs.getString("first_name");
        String last_name = rs.getString("last_name");
        session.setAttribute("userid", userid);
        session.setAttribute("first_name",first_name);
        session.setAttribute("last_name",last_name);
        //out.println("welcome " + userid);
        //out.println("<a href='logout.jsp'>Log out</a>");
        response.sendRedirect("homepage.jsp");
    } else {
        out.println("Invalid login credentials. <a href='index.html'>Try again</a>.");
    }
%>