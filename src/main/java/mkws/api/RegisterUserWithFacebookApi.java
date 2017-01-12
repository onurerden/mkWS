/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.Model.FacebookUserModel;
import mkws.Model.MkwsUser;
import mkws.Model.Token;
import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "RegisterUserWithFacebook", urlPatterns = {"/api/RegisterUserWithFacebook"})
public class RegisterUserWithFacebookApi extends HttpServlet {
static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
static SecureRandom rnd = new SecureRandom();
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
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
          String access_token = request.getParameter("access_token");
          ServerEngine server = new ServerEngine();
            FacebookUserModel user = server.getUserInfoFromFacebook(access_token, "name,email,id");
            if(user.getEmail()==null){
                 response.setStatus(401);
            out.println("{\"result\":\"failed\", \"description\":\"User info cannot be retrieved.\"}");
            return;
            }       
            
            System.out.println("User name = " + user.getName());
            System.out.println("User email= " + user.getEmail());
            System.out.println("User Id= " + user.getId());
            
            MkwsUser mkwsUser = server.getUserByEmail(user.getEmail());
            
            System.out.println("MkwsUser First Name = " + mkwsUser.getFirst_name());
            System.out.println("MkwsUser Last Name = " + mkwsUser.getLast_name());
            System.out.println("MkwsUser mkwsId = " + mkwsUser.getId());
            System.out.println("MkwsUser user Name = " + mkwsUser.getUname());
            
            if(mkwsUser.getEmail()==null){
                
            server.addMkwsUser(user.getName().substring(0, user.getName().indexOf(" ")),
                    user.getName().substring(user.getName().indexOf(" ")+1,user.getName().length()-1), 
                    user.getEmail(), user.getName()+user.getId(),randomString(8) , false);
            server.activateUser(server.getUserByEmail(user.getEmail()).getId());
            }
            
            
            String token=server.createTokenForUser(mkwsUser.getId());
             out.println("{\"token\" : \""+token+"\"}");        
            
        }catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
            response.setStatus(401);
            out.println("{\"result\":\"failed\", \"description\":\"User info cannot be retrieved.\"}");
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

    String randomString( int len ){
   StringBuilder sb = new StringBuilder( len );
   for( int i = 0; i < len; i++ ) 
      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
   return sb.toString();
}
}
