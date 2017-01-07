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
import mkws.Model.MkwsUser;
import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "Login", urlPatterns = {"/api/Login"})
public class Login extends HttpServlet {

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
   //   out.println("login test page");   

            StringBuffer jb = new StringBuffer();
            String line = null;
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    jb.append(line);
                }
            } catch (Exception e) {
            System.out.println(e.getMessage());
                        }
            
            
            String[] nameValuePairs = jb.toString().split("&");
            for (String nameValuePair : nameValuePairs) {
                if (nameValuePair.startsWith("userName" + "=")) {
                    userName = nameValuePair.replaceAll("userName=", "");
              //      out.println("User Name is: " + userName);
                }
                if (nameValuePair.startsWith("password" + "=")) {
                    password = nameValuePair.replaceAll("password=", "");
                //    out.println("password is: " + password);
                }

            }
            if(userName.isEmpty()||password.isEmpty()){
                response.setStatus(401);
                out.print("user entry denied");
                return;
            }
            ServerEngine server = new ServerEngine();
            MkwsUser user = server.getUserByCredentials(userName, password);
            if((user!=null)&&(user.isIsActivated())){
                //out.println("user entry granted");
                response.setStatus(200);
                String token = server.createTokenForUser(user.getId());
                out.println("{\"token\" : \""+token+"\"}");
            }else if((user!=null)&&(!user.isIsActivated())){
                response.setStatus(402);
                out.print("{\"result\": \"failed\", \"description\" : \"You should first activate your email address by clicking on the link sent in activation mail.\"}");
                out.close();
            }else{
                response.setStatus(401);
                out.print("user entry denied");
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
