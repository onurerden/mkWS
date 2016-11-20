/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import com.google.gson.Gson;
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
import mkws.Model.Token;
import mkws.ServerEngine;
import mkws.TokenEvaluator;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "GetUserInfoApi", urlPatterns = {"/api/GetUserInfo"})
public class GetUserInfoApi extends HttpServlet {

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

        ServerEngine server = new ServerEngine();
        PrintWriter out = response.getWriter();
        try {
            Token token = te.evaluateRequestForToken(request);
            if (token == null) {
                response.setStatus(401);
                out.println("token hatası: token yok");
                out.close();
                return;
            }
            server.setUserId(token.getUserId());
            /* TODO output your page here. You may use following sample code. */
            //System.out.println("parameter is: " + request.getParameter("userId"));
            int userId = 0;
            if (request.getParameter("userId").isEmpty()) {
                userId = token.getUserId();
            } else {
                userId = Integer.valueOf(request.getParameter("userId"));
            }
            Gson gson = new Gson();
            out.println(gson.toJson(server.getUserInfoById(userId)));

        }catch (SignatureException | IncorrectClaimException | MissingClaimException ex) {
            response.setStatus(401);
            out.println("token hatası: " + ex.getLocalizedMessage());
        } finally{
            out.close();
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
