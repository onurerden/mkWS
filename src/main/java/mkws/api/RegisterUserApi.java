/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.MailSender;
import mkws.Model.MailTemplate;
import mkws.Model.MkwsUser;
import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "RegisterUserApi", urlPatterns = {"/api/RegisterUser"})
public class RegisterUserApi extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
              StringBuffer jb = new StringBuffer();
            String line = null;
            String name = request.getParameter("name");
            String last_name=request.getParameter("last_name");
            String password = request.getParameter("password");
            String email=request.getParameter("email");
            String passwordAgain=request.getParameter("passwordAgain");
            String uname=request.getParameter("uname");
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    jb.append(line);
                }
            } catch (Exception e) { /*report an error*/ 
            System.out.println(e.getMessage());
            }
            String[] nameValuePairs = jb.toString().split("&");
            for (String nameValuePair : nameValuePairs) {
                if (nameValuePair.startsWith("uname" + "=")) {
                    uname = nameValuePair.replaceAll("uname=", "");
              //      out.println("User Name is: " + userName);
                }
                if (nameValuePair.startsWith("password" + "=")) {
                    password = nameValuePair.replaceAll("password=", "");
                //    out.println("password is: " + password);
                }
                if (nameValuePair.startsWith("password" + "=")) {
                    password = nameValuePair.replaceAll("password=", "");
                //    out.println("password is: " + password);
                }

                if (nameValuePair.startsWith("passwordAgain" + "=")) {
                    passwordAgain = nameValuePair.replaceAll("passwordAgain=", "");
                //    out.println("password is: " + password);
                }

                if (nameValuePair.startsWith("name" + "=")) {
                    name = nameValuePair.replaceAll("name=", "");
                //    out.println("password is: " + password);
                }

                if (nameValuePair.startsWith("last_name" + "=")) {
                    last_name = nameValuePair.replaceAll("last_name=", "");
                //    out.println("password is: " + password);
                }
                if (nameValuePair.startsWith("email" + "=")) {
                    email = nameValuePair.replaceAll("email=", "");
                //    out.println("password is: " + password);
                }
                
            }
            if(!password.equals(passwordAgain)){
                response.setStatus(401);
                out.print("{\"result\": \"failed\", \"description\" : \"Passwords aren't equal.\"}");
                return;
                }
            
                ServerEngine server = new ServerEngine();                
                
               if( server.addMkwsUser(name, last_name, email, uname, password, false,false)){
                   MkwsUser user = server.getUserByCredentials(uname, password);
                   
            if(user!=null){
                MailSender sender = new MailSender();
                String actToken = server.createTokenForActivation(user.getId());
                MailTemplate mail = server.getMailTemplate(1);
                String activationmessage =mail.getText();
               activationmessage = activationmessage.replaceAll("<%userName%>", user.getFirst_name() + " " + user.getLast_name());
               activationmessage = activationmessage.replaceAll("<%activationToken%>", actToken);
               
                sender.sendMail(user.getEmail(), activationmessage,mail.getSubject() );
                //out.println("user entry granted");
                
               // String token = server.createTokenForUser(user.getId());
                out.print("{\"result\" : \"success\", \"description\" : \"User registration completed successfully. Please check your e-mail inbox in order to complete your activation.\" }");
            }else{
                response.setStatus(401);
                   out.print("{\"result\": \"failed\", \"description\" : \"User entry denied.\"}");
            }
                   
               }else{
                   response.setStatus(401);
                   out.print("{\"result\": \"failed\", \"description\" : \"User registration failed. Probably user is already registered.\"}");
               }
                        


            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
