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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import mkws.Model.Token;
import mkws.ServerEngine;
import mkws.TokenEvaluator;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "SaveUserProfilePhoto", urlPatterns = {"/api/SaveUserProfilePhoto"})
@MultipartConfig
public class SaveUserProfilePhoto extends HttpServlet {

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
        try {
            Token token = te.evaluateRequestForToken(request);
            if (token == null) {
                response.setStatus(401);
                out.println("{\"result\": \"failed\", \"description\" : \"There is no token or token is invalid.\"}");
                out.close();
                return;
            }
            System.out.println(request.getParts().size() + " adet part var");
            for(int i=0;i<request.getParts().size();i++){
                System.out.println("Part name: " + ((Part)request.getParts().toArray()[i]).getName());
            }
            //String uid = request.getParameter("uid");
            //  System.out.println(uid+ "'den request geldi.");
            //String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
            Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
             String ext2= "jpg";
            InputStream fileContent = filePart.getInputStream();
            try{
            String fileName = getSubmittedFileName(filePart);
            System.out.println(filePart.getSize() + " bytes received. FileName is " + fileName);
            ext2 = FilenameUtils.getExtension(fileName);
                System.out.println("Extension is: " +ext2);
            }catch (Exception ex){
                System.out.println("Exception thrown while getting file name.");
            }
            //String contentString = slurp(fileContent, 2048);
            //System.out.println(contentString);

            ServerEngine server = new ServerEngine();
            server.setUserId(token.getUserId());

            
            int result = server.saveUserProfilePhoto(fileContent, token.getUserId(),ext2);
            if (result==0){
                out.println("{\"result\": \"success\", \"description\" : \"File Uploaded.\"}");
            }          

        } catch (SignatureException | IncorrectClaimException | MissingClaimException ex) {
            response.setStatus(401);
            out.println("{\"result\": \"failed\", \"description\" : \"" + ex.getLocalizedMessage() + "\"}");
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

    private static String getSubmittedFileName(Part part) {
        System.out.println(part.getContentType());
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

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

}
