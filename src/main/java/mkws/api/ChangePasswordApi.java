/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.Model.MkwsUser;
import mkws.Model.Token;
import mkws.ServerEngine;
import mkws.TokenEvaluator;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "ChangePasswordApi", urlPatterns = {"/api/ChangePassword"})
public class ChangePasswordApi extends HttpServlet {

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
        TokenEvaluator te = new TokenEvaluator();
        PrintWriter out = response.getWriter();
        /* TODO output your page here. You may use following sample code. */
        try {
            /* TODO output your page here. You may use following sample code. */
            Token token = te.evaluateRequestForToken(request);
            if (token == null) {
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\" : \"There is no token or token is invalid.\"}");
                out.close();
                return;
            }
            if (!token.isIsActivated()) {
                response.setStatus(402);
                out.print("{\"result\": \"failed\", \"description\" : \"You should first activate your email address by clicking on the link sent in activation mail.\"}");
                out.close();
                return;
            }
            ServerEngine server = new ServerEngine();
            server.setUserId(token.getUserId());
           
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            
            if((newPassword==null)||(newPassword.length()<6)){
             response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\" : \"Your passwords length should be at least 6 characters.\"}");
                out.close();
                return;   
            }
            MkwsUser user = server.getUserByCredentials(token.getUname(), oldPassword);
            
            boolean result = server.changeUserPasswordTo(user.getId(),newPassword);
            
            if (result||(user==null)){
                response.setStatus(200);
                out.println("{\"result\":\"success\", \"description\":\"Your password is changed\"}");
            }else{
                response.setStatus(401);
                out.println("{\"result\":\"failed\", \"description\":\"There was an error during changing your password.\"}");
                
            }
            
        } catch (SignatureException | IncorrectClaimException | MissingClaimException ex){
            response.setStatus(401);
                out.println("{\"result\":\"failed\", \"description\":\"Unknown error.\"}");
        } catch (NullPointerException ex){
             response.setStatus(401);
                out.println("{\"result\":\"failed\", \"description\":\"NullPointer error. "+ex.getMessage()+"\"}");
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
