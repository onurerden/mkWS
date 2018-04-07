<%-- 
    Document   : addUser
    Created on : Aug 18, 2016, 2:56:53 PM
    Author     : onurerden
--%>

<%@page import="com.google.gson.Gson"%>
<%@page import="mkws.DeviceTypes"%>
<%@page import="mkws.LogMessage"%>
<%@page import="mkws.ServerEngine"%>
<%
    ServerEngine server = new ServerEngine();
    if ((Boolean)session.getAttribute("isAdmin")){    
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
    
    boolean result = server.addMkwsUser(first_name, last_name, email, uname, password, isAdmin,false);
    if (result){
         LogMessage msg = new LogMessage();
         msg.setDeviceType(DeviceTypes.SERVER.getName());
         msg.setLogLevel(2);
         msg.setLogMessage("User "+ uname + " is added.");
            Gson gson = new Gson();
            server.sendLog(gson.toJson(msg));
        response.sendRedirect("users.jsp?success=\"User <em>"+uname+"</em> was added successfully.\"");
        
    
    }else{
    response.sendRedirect("users.jsp?error=\"An error occured while adding the user <em> "+ uname+"\" </em> to the mkWS.");
    }
    }else {
        response.sendRedirect("users.jsp");
    }
    %>