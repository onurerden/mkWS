/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.Model.RouteModel;
import mkws.Model.Token;
import mkws.ServerEngine;
import mkws.TokenEvaluator;
import static mkws.api.SendFollowMeDataApi.getBody;


/**
 *
 * @author onurerden
 */
@WebServlet(name = "EditRouteApi", urlPatterns = {"/api/EditRoute"})
public class EditRouteApi extends HttpServlet {

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
                System.out.println("No Session");
                sessionUserExists=false;
            }
            Token token = te.evaluateRequestForToken(request);
            if (token == null) {
           //     response.setStatus(401);
           //     out.println("{\"result\": \"failed\", \"description\" : \"There is no token or token is invalid.\"}");
           //     out.close();
           //     return;
                System.out.println("No token");
                tokenExists=false;
            }
            if (!token.isIsActivated()){
                  response.setStatus(402);
                out.print("{\"result\": \"failed\", \"description\" : \"You should first activate your email address by clicking on the link sent in activation mail.\"}");
                out.close();
                return;
              }
            if(!tokenExists && !sessionUserExists){
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\" : \"There is no token or token is invalid.\"}");
                out.close();
                return;
            }
            
            
            try {
                String routeString = getBody(request);
                ServerEngine engine = new ServerEngine();
                engine.setUserId(token.getUserId());
                Gson gson = new Gson();
                RouteModel route = gson.fromJson(routeString, RouteModel.class);
                if (token.isIsAdmin() || (engine.getRouteInfo(route.getRouteId()).getUserId() == token.getUserId())) {

                    if (engine.editRoute(route)) {
                        out.println("{\"result\": \"success\"}");
                    } else {
                        response.setStatus(401);
                        out.println("{\"result\": \"failed\"}");
                    }
                } else {
                    response.setStatus(401);
                    out.println("{\"result\": \"You don't have permission to edit this route.\"}");
                }
            } catch (NumberFormatException ex) {
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\": \"" + ex.getMessage() + "\"}");
            } catch (JsonParseException ex){
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
            
    // <editor-fold defaultstate="collapsed" desc="inputStream'ı stringe çeviren method.">
    public static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "UTF-8")) {
            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0) {
                    break;
                }
                out.append(buffer, 0, rsz);
            }
        } catch (UnsupportedEncodingException ex) {
            /* ... */
        } catch (IOException ex) {
            /* ... */
        }
        return out.toString();
    }// </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="Body'i extract eden method.">
    public static String getBody(HttpServletRequest request) throws IOException {

    String body = null;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;

    try {
        InputStream inputStream = request.getInputStream();
        if (inputStream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } else {
            stringBuilder.append("");
        }
    } catch (IOException ex) {
        throw ex;
    } finally {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                throw ex;
            }
        }
    }

    body = stringBuilder.toString();
   // System.out.println(body);
    return body;
}
}// </editor-fold>
