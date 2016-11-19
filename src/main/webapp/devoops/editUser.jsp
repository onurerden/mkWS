<%-- 
    Document   : editUser
    Created on : Aug 18, 2016, 2:56:53 PM
    Author     : onurerden
--%>

<%@page import="mkws.Model.MkwsUser"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="mkws.DeviceTypes"%>
<%@page import="mkws.LogMessage"%>
<%@page import="mkws.ServerEngine"%>
<%
    if ((Boolean)session.getAttribute("isAdmin")){
    
    String first_name = request.getParameter("first_name"); 
    String last_name = request.getParameter("last_name"); 
    String password = request.getParameter("password");
    String uname = request.getParameter("uname");
    int id = request.getParameter("userId"); 
    String email = request.getParameter("email"); 
    System.out.println(uname);
    
    boolean isAdmin = false;
    try{
    if (request.getParameter("isAdmin").equals("on")){
       isAdmin=true;
    }
    }catch (Exception ex){
        
    }
    
    System.out.println("isAdmin value is: " + request.getParameter("isAdmin"));
    
    ServerEngine server = new ServerEngine();
    MkwsUser result = server.editMkwsUser(id, first_name, last_name, email, password, isAdmin);
    if (result.getId()!=0){
         LogMessage msg = new LogMessage();
         msg.setDeviceType(DeviceTypes.SERVER.getName());
         msg.setLogLevel(2);
         msg.setLogMessage("User "+ result.getUname() + " is edited.");
            Gson gson = new Gson();
            server.sendLog(gson.toJson(msg));
        response.sendRedirect("users.jsp?success=\"User <em>"+result.getUname()+"</em> was edited successfully.\"");
        
    
    }else{
    response.sendRedirect("users.jsp?error=\"An error occured while adding the user <em> "+ result.getUname()+"\" </em> to the mkWS.");
    }
    }else {
        response.sendRedirect("users.jsp");
    }
    %>