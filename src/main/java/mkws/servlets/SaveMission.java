/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.FollowMeData;
import mkws.Model.MKMission;
import mkws.ServerEngine;

/**
 *
 * @author oerden
 */
@WebServlet(name = "SaveMission", urlPatterns = {"/SaveMission"})
public class SaveMission extends HttpServlet {

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
        MKMission mission = null;
        String missionString = request.getParameter("mission");
        
        Gson gson = new Gson();
        try (PrintWriter out = response.getWriter()) {
            try {
                mission = gson.fromJson(missionString, MKMission.class);
            } catch (JsonSyntaxException ex) {
                System.out.println("MissionData parse error: " + ex.toString());
                out.println("-3");
                return;
            }
            if ((mission.waypoints ==null )|| (mission.waypoints.size()<1)){
                out.println("-4");
                return;
            }

            /* TODO output your page here. You may use following sample code. */
            ServerEngine engine = new ServerEngine();
            int result = engine.saveMission(mission, "Name", "Comment");
            out.println(result);

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            
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
