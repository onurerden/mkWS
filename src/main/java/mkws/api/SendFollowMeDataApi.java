/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureException;
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

import mkws.Model.Token;
import mkws.ServerEngine;
import mkws.TokenEvaluator;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "SendFollowMeDataApi", urlPatterns = {"/api/SendFollowMeData"})
public class SendFollowMeDataApi extends HttpServlet {

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
        int i = 0;
        try {
            /* TODO output your page here. You may use following sample code. */
            Token token = te.evaluateRequestForToken(request);
            if (token == null) {
                response.setStatus(401);
                out.print("{\"token hatası\": \"token yok\"}");
                out.close();
                return;
            }
            if (!token.isIsActivated()){
                  response.setStatus(402);
                out.print("{\"result\": \"failed\", \"description\" : \"You should first activate your email address by clicking on the link sent in activation mail.\"}");
                out.close();
                return;
              }
           // String jsonString = request.getParameter("jsonfollowme");
           // String jsonString = "" + request.getHeader("jsonfollowme");
            String jsonString = getBody(request);
            
          //  System.out.println(jsonString);
            
            String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
        //  System.out.println("forwerdedfor");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
        //  System.out.println("proxyclientip");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_CLIENT_IP");
        //  System.out.println("http client");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        //  System.out.println("http x forwarded for");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
        //  System.out.println("remote addr");
    }
            
            ServerEngine server = new ServerEngine();
            server.setUserId(token.getUserId());
            //i = server.sendFollowMeData(jsonString);
            i=server.sendFollowMeDataToMabeyn(jsonString, ip , server.getUserId());
            
            if (i < 0) {
                response.setStatus(401);
            }
            out.print(i);
            
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

        //inputStream'ı stringe çeviren method
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
    }
    
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
}
