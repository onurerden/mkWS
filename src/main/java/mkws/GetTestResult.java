/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oerden
 */
@WebServlet(name = "GetTestResult", urlPatterns = {"/GetTestResult"})
public class GetTestResult extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public static String url = "jdbc:mysql://localhost/mk";
    public static String user = "onur";
    public static String password = "19861986";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/json");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */

            int kopterID = 1;
            try {
                kopterID = Integer.parseInt(request.getParameter("kopterID"));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(url, user, password);
            st_1 = con_1.createStatement();
            String sql_1 = "SELECT * from followme where kopterID = " + kopterID + " AND sent = 0";
            rs_1 = st_1.executeQuery(sql_1);

            FollowMeData data = new FollowMeData();
            //data.kopterID = kopterID;
            while (rs_1.next()) {
                data.lat = rs_1.getDouble("latitude");
                data.lng = rs_1.getDouble("longitude");
                data.bearing = rs_1.getInt("bearing");
            }

            Gson jsonobject = new Gson();

            String json = jsonobject.toJson(data);

            out.println(json);
            System.out.println(json);
            con_1.close();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetTestResult.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        } catch (InstantiationException ex) {
            Logger.getLogger(GetTestResult.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GetTestResult.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        } catch (SQLException ex) {
            Logger.getLogger(GetTestResult.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
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
