/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author oerden
 */
@WebServlet(name = "GetRegisteredData", urlPatterns = {"/GetRegisteredData"})
public class GetRegisteredData extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public static String url ;
    public static String user ;
    public static String password;
    public String requestedData = "other";

    public enum Terminal {

        mk, mp, other
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json");
        PrintWriter out = response.getWriter();
        Credentials cr = new Credentials();

        try {
            /* TODO output your page here. You may use following sample code. */
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(),cr.dbUserName , cr.dbPassword);
            st_1 = con_1.createStatement();

            Terminal term = Terminal.other;
            try {
                term = Terminal.valueOf(request.getParameter("device").toLowerCase());
            } catch (Exception ex) {

            }

            switch (term) {
                case mk: {
                    String sql_1 = "SELECT * from kopter";
                    rs_1 = st_1.executeQuery(sql_1);

                    List<MKopter> kopterList = new ArrayList();

                    while (rs_1.next()) {
                        MKopter kopter = new MKopter();
                        kopter.id = rs_1.getInt("id");
                        kopter.uid = rs_1.getString("uid");
                        kopter.name = rs_1.getString("name");
                        kopter.isActive = rs_1.getBoolean("active");
                        kopter.latestTouch = rs_1.getTimestamp("latestTouch");

                        kopterList.add(kopter);
                    }

                    Gson jsonobject = new Gson();
                    String json = jsonobject.toJson(kopterList);
                    out.println(json);
                    break;
                }
                case mp: {
                    String sql_1 = "SELECT * from followmedevices";
                    rs_1 = st_1.executeQuery(sql_1);

                    List<MobilePhone> phoneList = new ArrayList();

                    while (rs_1.next()) {
                        MobilePhone phone = new MobilePhone();
                        phone.id = rs_1.getInt("id");
                        phone.name = rs_1.getString("name");
                        phone.uid = rs_1.getString("uid");
                        phone.registerDate = rs_1.getTimestamp("registerDate");

                        phoneList.add(phone);
                    }

                    Gson jsonobject = new Gson();
                    String json = jsonobject.toJson(phoneList);
                    out.println(json);

                    break;
                }
                case other: {
                    out.println("wrong device type");
                    break;
                }

            }
            con_1.close();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetRegisteredData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(GetRegisteredData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GetRegisteredData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GetRegisteredData.class.getName()).log(Level.SEVERE, null, ex);
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
