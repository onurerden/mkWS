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
import mkws.Model.Token;
import mkws.ServerEngine;
import mkws.TokenEvaluator;


/**
 *
 * @author onurerden
 */
@WebServlet(name = "DeleteRouteApi", urlPatterns = {"/api/DeleteRoute"})
public class DeleteRouteApi extends HttpServlet {

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
boolean sessionUserExists=true;
boolean tokenExists = true;
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            if (request.getSession().getAttribute("userid") == null) {
               // response.setStatus(401);
               // out.println("{\"result\": \"failed\", \"description\" : \"mkWS authentication failed.\"}");
               // out.close();
               // return;
                sessionUserExists=false;
            }
            Token token = te.evaluateRequestForToken(request);
            if (token == null) {
           //     response.setStatus(401);
           //     out.println("{\"result\": \"failed\", \"description\" : \"There is no token or token is invalid.\"}");
           //     out.close();
           //     return;
                tokenExists=false;
            }
            if(!tokenExists||!sessionUserExists){
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\" : \"There is no token or token is invalid.\"}");
                out.close();
                return;
            }
            
            int routeId = 0;
            try {
                routeId = Integer.valueOf(request.getParameter("routeId"));
                ServerEngine engine = new ServerEngine();
                engine.setUserId(token.getUserId());
                if (token.isIsAdmin() || (engine.getRouteInfo(routeId).getUserId() == token.getUserId())) {

                    if (engine.deleteRoute(routeId)) {
                        out.println("{\"result\": \"success\"}");
                    } else {
                        response.setStatus(401);
                        out.println("{\"result\": \"failed\"}");
                    }
                } else {
                    response.setStatus(401);
                    out.println("{\"result\": \"You don't have permission to delete this route.\"}");
                }
            } catch (NumberFormatException ex) {
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\": \"" + ex.getMessage() + "\"}");
            }

        } catch (Exception ex) {
System.out.println(ex.getLocalizedMessage());
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
