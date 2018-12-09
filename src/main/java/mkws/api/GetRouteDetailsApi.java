/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.Model.RouteModel;
import mkws.Model.Token;
import mkws.ServerEngine;
import mkws.TokenEvaluator;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "GetRouteDetails", urlPatterns = {"/api/GetRouteDetails"})
public class GetRouteDetailsApi extends HttpServlet {

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
            int routeId = 0;
            try{
            routeId=Integer.valueOf(request.getParameter("routeId"));
            }catch (Exception ex){
                response.setStatus(401);
                out.print("{\"result\": \"failed\", \"description\" : \"Invalid routeId.\"}");
                out.close();
                return;
            }
            ServerEngine server = new ServerEngine();
            TokenEvaluator te = new TokenEvaluator();
            Token token = te.evaluateRequestForToken(request);
            
            if (token == null) {
                response.setStatus(401);
                out.print("{\"result\": \"failed\", \"description\" : \"There is no token or token is invalid.\"}");
                out.close();
                return;
            }            
            server.setUserId(token.getUserId());
            RouteModel route = server.getRouteDetails(routeId, token.isIsAdmin());
            
            Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd' 'HH:mm:ss")
                .create();
            
            out.print(gson.toJson(route));

        }catch (Exception ex){
            response.setStatus(401);
                out.print("{\"result\": \"failed\", \"description\" : \""+ ex.getLocalizedMessage() +" \"}");
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
