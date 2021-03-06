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
import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "GetBirolsLastRoute", urlPatterns = {"/api/GetBirolsLastRoute"})
public class GetBirolsLastRouteApi extends HttpServlet {

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
        response.setContentType("text/json");
        PrintWriter out = response.getWriter();
        //TokenEvaluator te = new TokenEvaluator();

        try {
            ServerEngine server = new ServerEngine();
            server.setUserId(410);
            Integer routeId = -1;
            try {
                routeId = server.getUsersLastRouteId();
            } catch (Exception ex) {

            }
            if (routeId < 0) {
                response.setStatus(401);
            }
            //response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, HEAD, OPTIONS");
            response.addHeader("Access-Control-Allow-Credentials","true");
            response.addHeader("Access-Control-Allow-Headers"
            , "Origin, X-Requested-With, Content-Type, Accept, Authorization, 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.print("{\"routeId\": " + routeId + " , \"token\":\"" + server.createTokenForUser(410) + "\"}");

        } catch (NumberFormatException e) {
            response.setStatus(500);
            out.print(-2);
        } catch (SignatureException | IncorrectClaimException | MissingClaimException ex) {
            response.setStatus(401);
            out.print("{\"result\": \"failed\", \"description\" : \"" + ex.getLocalizedMessage() + "\"");
        } finally {
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
