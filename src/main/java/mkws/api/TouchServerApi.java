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
import mkws.MKSession;
import mkws.Model.Token;
import mkws.ServerEngine;
import mkws.TokenEvaluator;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "TouchServerApi", urlPatterns = {"/api/TouchServer"})
public class TouchServerApi extends HttpServlet {

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
        TokenEvaluator te = new TokenEvaluator();
        try {
            /* TODO output your page here. You may use following sample code. */
            Token token = te.evaluateRequestForToken(request);
              if (token == null) {
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\" : \"There is no token or token is invalid.\"}");
                out.close();
                return;
            }
            String requestIdByUID = "";
            String requestedDeviceType = "";
            requestIdByUID = request.getParameter("uid");
            requestedDeviceType = request.getParameter("deviceType");
            String output = "0";
            ServerEngine server = new ServerEngine();
            server.setUserId(token.getUserId());
            output = server.touchServer(requestIdByUID, requestedDeviceType);
            Gson gson=new Gson();
            MKSession session = gson.fromJson(output, MKSession.class);
            if(session.getDeviceId()<0){
                response.setStatus(401);
            }
            out.print(output);
        } catch (SignatureException | IncorrectClaimException | MissingClaimException ex) {
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\" : \"" + ex.getLocalizedMessage() + "\"");
            }catch (NumberFormatException ex){
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\" : \"" + ex.getLocalizedMessage() + "\"");
            }
        
        finally {
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
