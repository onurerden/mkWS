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
    private double routeLength = 0;
    private String mapBounds = "";

    class Koordinat {

        public double enlem = 0;
        public double boylam = 0;
    }

    List<Koordinat> noktalar = new ArrayList<Koordinat>();

    public void setRouteId(int i) {

        this.routeId = i;
        List<String> points = new ArrayList<String>();

        Credentials cr = new Credentials();
        this.routePoints = "";
        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        Koordinat minBounds = new Koordinat();
        minBounds.boylam = 99;
        minBounds.enlem = 99;
        Koordinat maxBounds = new Koordinat();
        maxBounds.boylam = -99;
        maxBounds.enlem = -99;
        try {
            /* TODO output your page here. You may use following sample code. */

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT latitude, longitude, followMeDeviceId from followme WHERE routeId = " + routeId + " "
                    + "ORDER BY followme.id ASC";

            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                points.add("[" + rs_1.getString("latitude") + "," + rs_1.getString("longitude") + "],");
                this.routePoints = this.routePoints + "[" + rs_1.getString("latitude") + "," + rs_1.getString("longitude") + "],";
                Koordinat yeniNokta = new Koordinat();
                yeniNokta.enlem = Double.parseDouble(rs_1.getString("latitude"));
                yeniNokta.boylam = Double.parseDouble(rs_1.getString("longitude"));
                noktalar.add(yeniNokta);

//Set Bounds
                if (yeniNokta.enlem < minBounds.enlem) {
                    minBounds.enlem = yeniNokta.enlem;
                }
                if (yeniNokta.boylam < minBounds.boylam) {
                    minBounds.boylam = yeniNokta.boylam;
                }
                if (yeniNokta.enlem > maxBounds.enlem) {
                    maxBounds.enlem = yeniNokta.enlem;
                }
                if (yeniNokta.boylam > maxBounds.boylam) {
                    maxBounds.boylam = yeniNokta.boylam;
                }

                this.mapBounds= "[["+minBounds.enlem+","+minBounds.boylam+"],"
                        + "["+maxBounds.enlem+","+maxBounds.boylam+"]]";

//  this.deviceName = rs_1.getString("name");
                //  this.deviceId = rs_1.getInt("followMeDeviceId");
                //  this.routeCreationDate = rs_1.getTimestamp("time");
            }

            if (!points.isEmpty()) {
                routeStartPoint = points.get(0).toString();
                routeEndPoint = points.get(points.size() - 1).toString();
                prepareRouteDetails(this.routeId);
                this.deviceName = deviceNameFromId(this.deviceId);
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

    public String getDeviceName() {
        return this.deviceName;
    }

    public Timestamp getRouteCreationDate() {
        return this.routeCreationDate;
    }

    public double getRouteLength() {
        return this.routeLength;
    }

    private void calculateRouteLength() {
        for (int i = 0; i < noktalar.size() - 1; i++) {
            this.routeLength = this.routeLength + distanceBetweenPoints(noktalar.get(i).enlem, noktalar.get(i).boylam, noktalar.get(i + 1).enlem, noktalar.get(i + 1).boylam);
        }
        System.out.println("Route Length: " + this.routeLength);
    }

    private double distanceBetweenPoints(double latFirst, double lngFirst, double latSecond, double lngSecond) {

        double R = 6371.0; // metres
        double d = 0.0;
        double fiFirst = Math.toRadians(latFirst);
        double fiSecond = Math.toRadians(latSecond);
        double deltaFi = Math.toRadians(latSecond - latFirst);
        double deltaLambda = Math.toRadians(lngSecond - lngFirst);

        double a = Math.sin(deltaFi / 2) * Math.sin(deltaFi / 2)
                + Math.cos(fiFirst) * Math.cos(fiSecond)
                * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        d = R * c;
        return d;
    }

    private String deviceNameFromId(int id) {
        String name = "";
        Credentials cr = new Credentials();

        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        try {
            /* TODO output your page here. You may use following sample code. */

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT name from followmedevices where id = " + id;
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                name = rs_1.getString("name");
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {

        }
        return name;
    }

    private void prepareRouteDetails(int id) {

        Credentials cr = new Credentials();

        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        try {
            /* TODO output your page here. You may use following sample code. */

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT followMeDeviceId, time from route where id = " + id;
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                this.deviceId = rs_1.getInt("followMeDeviceId");
                this.routeCreationDate = rs_1.getTimestamp("time");
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {

        }
        calculateRouteLength();

    }
    public String getMapBounds(){
        return this.mapBounds;
    }
}
