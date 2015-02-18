/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.webbeans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import mkws.Credentials;

/**
 *
 * @author onurerden
 */
public class GetRouteDetails {

    private int routeId = -1;
    private String routePoints = "";
    private String mapCenter = "";
    private String routeStartPoint = "";
    private String routeEndPoint = "";
    private String deviceName = "";
    private int deviceId = -1;
    private Timestamp routeCreationDate;

    public void setRouteId(int i) {

        this.routeId = i;
        List<String> points = new ArrayList<String>();
        Credentials cr = new Credentials();
        this.routePoints = "";
        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        try {
            /* TODO output your page here. You may use following sample code. */

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
            st_1 = con_1.createStatement();
            String query = "SELECT latitude, longitude, name, route.time FROM (followme "
                    + "INNER JOIN "
                    + "followmedevices "
                    + "ON "
                    + "followme.followMeDeviceId = followmedevices.id) "
                    + "INNER JOIN route "
                    + "ON followme.routeId = route.id "
                    + "WHERE routeId = " + routeId + " "
                    + "ORDER BY followme.id asc";

            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                points.add("[" + rs_1.getString("latitude") + "," + rs_1.getString("longitude") + "],");
                this.routePoints = this.routePoints + "[" + rs_1.getString("latitude") + "," + rs_1.getString("longitude") + "],";
                this.deviceName = rs_1.getString("name");
                this.deviceId = rs_1.getInt("followMeDeviceId");
                this.routeCreationDate = rs_1.getTimestamp("time");
            }

            if (!points.isEmpty()) {
                routeStartPoint = points.get(0).toString();
                routeEndPoint = points.get(points.size() - 1).toString();

            }

        } catch (ClassNotFoundException e) {

        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (SQLException e) {
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {

            }
        }

    }

    public int getRouteId() {
        return this.routeId;
    }

    public String getRoutePoints() {
        // this.routePoints = "[38.425218, 27.151823],[38.445388, 27.173281],[38.455874, 27.203665],";
        return this.routePoints;

    }

    public String getRouteStartPoint() {
        return this.routeStartPoint;
    }

    public String getRouteEndPoint() {
        return this.routeEndPoint;
    }

    public int getDeviceId() {
        return this.deviceId;
    }
    public String getDeviceName(){
        return this.deviceName;
    }
    public Timestamp getRouteCreationDate(){
        return this.routeCreationDate;
    }

}
