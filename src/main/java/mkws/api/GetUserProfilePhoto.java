/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.api;

import mkws.Credentials;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Onur
 */
@WebServlet(name = "GetUserProfilePhoto", urlPatterns = {"/api/GetUserProfilePhoto"})
public class GetUserProfilePhoto extends HttpServlet {

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
        byte[] binaryStream = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            int userId = -1;
            try {
                userId = Integer.valueOf(request.getParameter("userId"));
            } catch (NumberFormatException ex) {
                System.out.println("No id is submitted");
            }

            Credentials cr = new Credentials();

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.userprofilepictures WHERE userId=" + userId + " ORDER BY ID DESC LIMIT 1";

            rs_1 = st_1.executeQuery(query);
            String imageType = "jpg";
            boolean foundImage = false;
            while (rs_1.next()) {
                foundImage=true;
                binaryStream = rs_1.getBytes("picture");
                imageType = rs_1.getString("fileType");
            }
            if(foundImage){
            if (imageType.equals("pdf")) {
                response.setContentType("application/" + imageType);
            } else {
                response.setContentType("image/" + imageType);
            }

            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(binaryStream);
                bos.flush();
            }
            }else {
                response.sendRedirect("../devoops/img/avatar.jpg");
            }
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(GetUserProfilePhoto.class.getName()).log(Level.SEVERE, null, ex);
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
