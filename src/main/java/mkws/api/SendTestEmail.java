/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.Credentials;
import mkws.LogMessage;
import mkws.ServerEngine;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "SendTestEmail", urlPatterns = {"/SendTestEmail"})
public class SendTestEmail extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            Credentials cr = new Credentials();
            Email email = new SimpleEmail();
            Email email2= new SimpleEmail();
            email.setHostName(cr.getEmailHost());
            email2.setHostName(cr.getEmailHost());
            email.setSmtpPort(465);
            email2.setSmtpPort(25);
            email.setAuthenticator(new DefaultAuthenticator(cr.getEmailUserName(), cr.getEmailUserPassword()));
            email2.setAuthenticator(new DefaultAuthenticator(cr.getEmailUserName(), cr.getEmailUserPassword()));
            email.setSSLOnConnect(false);
            try {
                email.setFrom("no-reply@followmeapp.xyz");

                email.setSubject("FollowMe App Test Mail");
                email.setMsg("This is a test mail with ssl... :-)");
                email.addTo("posta@onurerden.com");
                email.send();
                email2.setFrom("no-reply@followmeapp.xyz");

                email2.setSubject("FollowMe App Test Mail");
                email2.setMsg("This is a test mail with non-ssl ... :-)");
                email2.addTo("posta@onurerden.com");
                email2.send();
                email.send();
            } catch (EmailException ex) {
                Logger.getLogger(SendTestEmail.class.getName()).log(Level.SEVERE, null, ex);
                ServerEngine server = new ServerEngine();
                LogMessage log = new LogMessage();
                log.setLogLevel(1);
                log.setLogMessage(ex.getLocalizedMessage());
                Gson gson = new Gson();
                server.sendLog(gson.toJson(log));
            }

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SendTestEmail</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Mail sent by " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
