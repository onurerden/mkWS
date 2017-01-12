/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.Model.FacebookUserModel;
import mkws.Model.MkwsUser;
import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "RegisterUserWithFacebook", urlPatterns = {"/api/RegisterUserWithFacebook"})
public class RegisterUserWithFacebookApi extends HttpServlet {

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
            out.println("User name = " + user.getName());
            out.println("User email= " + user.getEmail());
            out.println("User Id= " + user.getId());
            
            MkwsUser mkwsUser = server.getUserByEmail(user.getEmail());
            out.println("MkwsUser First Name = " + mkwsUser.getFirst_name());
            out.println("MkwsUser Last Name = " + mkwsUser.getLast_name());
            out.println("MkwsUser mkwsId = " + mkwsUser.getId());
            out.println("MkwsUser user Name = " + mkwsUser.getUname());
            
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

}
