/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mkws.Model.FollowMeDataModel;
import mkws.Model.RouteModel;
import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
@WebServlet(name = "samplefiledownload", urlPatterns = {"/samplefiledownload"})
public class samplefiledownload extends HttpServlet {

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
        response.setContentType("text/gpx");
        response.setHeader("Content-Disposition", "attachment; filename=\"sampleFile.gpx\"");
        try {
            /* TODO output your page here. You may use following sample code. */
            OutputStream outputStream = response.getOutputStream();
            ServerEngine server = new ServerEngine();
            RouteModel model = server.getRouteDetails(920, true);

            String outputResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n"
                    + "\n"
                    + "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:gpxx=\"http://www.garmin.com/xmlschemas/GpxExtensions/v3\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" creator=\"Oregon 400t\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd\">\n"
                    + "  <metadata>\n"
                    + "    <link href=\"http://www.garmin.com\">\n"
                    + "      <text>Garmin International</text>\n"
                    + "    </link>\n"
                    + "    <time>2009-10-17T22:58:43Z</time>\n"
                    + "  </metadata>\n"
                    + "  <trk>\n"
                    + "    <name>Example GPX Document</name>\n"
                    + "    <trkseg>";
            outputStream.write(outputResult.getBytes());
            for (FollowMeDataModel fm : model.getFollowMeData()) {
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(fm.getTime());
                outputResult = "<trkpt lat=" + fm.getLat() + " lon=" + fm.getLng() + ">\n"
                        + "        <ele>" + fm.getAltitude() + "</ele>\n"
                        //   + "        <speed>" + fm.getSpeed() + " </speed>\n"
                        + "        <time>" + timeStamp + "</time>\n"
                        + "<extensions>\n"
                        + "   <gpxtpx:TrackPointExtension><gpxtpx:speed>"+fm.getSpeed()+"</gpxtpx:speed> \n"
                        + "   <gpxtpx:course>"+fm.getBearing()+"</gpxtpx:course>\n"
                        + "   </gpxtpx:TrackPointExtension>\n"
                        + "   </extensions>      </trkpt>";
                outputStream.write(outputResult.getBytes());
            }

            outputResult = " </trkseg>\n"
                    + "  </trk>\n"
                    + "</gpx>";
            outputStream.write(outputResult.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception ex) {
            System.out.println("exception:" + ex.getMessage());
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
