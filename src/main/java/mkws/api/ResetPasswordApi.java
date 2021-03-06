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
import mkws.PasswordChangeEvaluator;
import mkws.ServerEngine;
import mkws.TokenEvaluator;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "ResetPasswordApi", urlPatterns = {"/api/ResetPassword"})
public class ResetPasswordApi extends HttpServlet {

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
            
        
            ServerEngine server = new ServerEngine();
            String token = request.getParameter("token");
            PasswordChangeEvaluator evaluator = new PasswordChangeEvaluator();
            if(evaluator.changePassword(token)){
                response.setStatus(200);
                out.println("Your new password has been sent to your email address.");
            }else{
                response.setStatus(401);
                out.println("There was an error about token");
                
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
