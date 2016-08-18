<%-- 
    Document   : addUser
    Created on : Aug 18, 2016, 2:56:53 PM
    Author     : onurerden
--%>

<%@page import="mkws.ServerEngine"%>
<%
    if ((boolean) session.getAttribute("isAdmin")){
    ServerEngine server = new ServerEngine();
    String first_name = request.getParameter("first_name"); 
    String last_name = request.getParameter("last_name"); 
    String uname = request.getParameter("uname"); 
    String email = request.getParameter("email"); 
    String password = request.getParameter("password"); 
    boolean isAdmin = false;
    try{
    if (request.getParameter("isAdmin").equals("on")){
       isAdmin=true;
    }
    }catch (Exception ex){
        
    }
    
    System.out.println("isAdmin value is: " + request.getParameter("isAdmin"));
    
    boolean result = server.addMkwsUser(first_name, last_name, email, uname, password, isAdmin);
    if (result){
        response.sendRedirect("users.jsp?success=\""+uname+"\"");
        
    
    }else{
    response.sendRedirect("users.jsp?error=\""+ uname+"\"");
    }
    }else {
        response.sendRedirect("users.jsp");
    }
    %>