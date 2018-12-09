package mkws.webbeans;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mkws.Credentials;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author onurerden
 */
public class GetRouteDetails {
public GetRouteDetails(){
    //BasicConfigurator.configure();
}
    private int routeId = -1;
    private String routePoints = "";
    private String routeAltitudeValues = "";
    private String routeSpeedValues = "";
    private String routeSpeedKmhValues = "";

    private final String mapCenter = "";
    private String routeStartPoint = "";
    private String routeEndPoint = "";
    private String deviceName = "";
    private int deviceId = -1;
    private Timestamp routeCreationDate;
    private Timestamp updateTime;
    private double routeLength = 0;
    private String mapBounds = "";
    private double maxSpeed = 0.0;
    private double maxAltitude = 0.0;
    private double minAltitude = 10000;
    private int routeType =0;
    
    private double userId = 1;

    public List<String> points = new ArrayList<>();
    private List<Double> altitudeList = new ArrayList<>();
    private List<Double> speedList = new ArrayList<>();
    private List<Double> latitudeList = new ArrayList<>();
    private List<Double> longitudeList = new ArrayList<>();
    private List<Double> distanceList = new ArrayList<>();
    private List<Timestamp> timeList = new ArrayList<>();

    /**
     * @return the routeAltitudeValues
     */
    public String getRouteAltitudeValues() {
        return routeAltitudeValues;
    }

    /**
     * @param routeAltitudeValues the routeAltitudeValues to set
     */
    public void setRouteAltitudeValues(String routeAltitudeValues) {
        this.routeAltitudeValues = routeAltitudeValues;
    }

    /**
     * @return the routeSpeedValues
     */
    public String getRouteSpeedValues() {
        return routeSpeedValues;
    }

    /**
     * @param routeSpeedValues the routeSpeedValues to set
     */
    public void setRouteSpeedValues(String routeSpeedValues) {
        this.routeSpeedValues = routeSpeedValues;
    }

    /**
     * @return the maxSpeed
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @param maxSpeed the maxSpeed to set
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * @return the maxAltitude
     */
    public double getMaxAltitude() {
        return maxAltitude;
    }

    /**
     * @param maxAltitude the maxAltitude to set
     */
    public void setMaxAltitude(double maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    /**
     * @return the minAltitude
     */
    public double getMinAltitude() {
        return minAltitude;
    }

    /**
     * @param minAltitude the minAltitude to set
     */
    public void setMinAltitude(double minAltitude) {
        this.minAltitude = minAltitude;
    }

    /**
     * @return the routeSpeedKmhValues
     */
    public String getRouteSpeedKmhValues() {
        return routeSpeedKmhValues;
    }

    /**
     * @param routeSpeedKmhValues the routeSpeedKmhValues to set
     */
    public void setRouteSpeedKmhValues(String routeSpeedKmhValues) {
        this.routeSpeedKmhValues = routeSpeedKmhValues;
    }

    /**
     * @return the altitudeList
     */
    public List<Double> getAltitudeList() {
        return altitudeList;
    }

    /**
     * @param altitudeList the altitudeList to set
     */
    public void setAltitudeList(List<Double> altitudeList) {
        this.altitudeList = altitudeList;
    }

    /**
     * @return the speedList
     */
    public List<Double> getSpeedList() {
        return speedList;
    }

    /**
     * @param speedList the speedList to set
     */
    public void setSpeedList(List<Double> speedList) {
        this.speedList = speedList;
    }

    /**
     * @return the latitudeList
     */
    public List<Double> getLatitudeList() {
        return latitudeList;
    }

    /**
     * @param latitudeList the latitudeList to set
     */
    public void setLatitudeList(List<Double> latitudeList) {
        this.latitudeList = latitudeList;
    }

    /**
     * @return the longitudeList
     */
    public List<Double> getLongitudeList() {
        return longitudeList;
    }

    /**
     * @param longitudeList the longitudeList to set
     */
    public void setLongitudeList(List<Double> longitudeList) {
        this.longitudeList = longitudeList;
    }

    /**
     * @return the distanceList
     */
    public List<Double> getDistanceList() {
        return distanceList;
    }

    /**
     * @param distanceList the distanceList to set
     */
    public void setDistanceList(List<Double> distanceList) {
        this.distanceList = distanceList;
    }

    /**
     * @return the timeList
     */
    public List<Timestamp> getTimeList() {
        return timeList;
    }

    /**
     * @param timeList the timeList to set
     */
    public void setTimeList(List<Timestamp> timeList) {
        this.timeList = timeList;
    }

    /**
     * @return the userId
     */
    public double getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(double userId) {
        this.userId = userId;
    }

    /**
     * @return the routeType
     */
    public int getRouteType() {
        return routeType;
    }

    /**
     * @param routeType the routeType to set
     */
    public void setRouteType(int routeType) {
        this.routeType = routeType;
    }

    class Koordinat {

        public double enlem = 0;
        public double boylam = 0;
        public double altitude = 0.0;
        public double speed = 0.0;
    }

    class AltitudeChartValues {

        public double y = 0;
        public double x = 0; //id
    }

    class SpeedChartValues {

        public double x = 0; //id
        public double y = 0;
    }

    List<Koordinat> noktalar = new ArrayList<>();

    public void setRouteId(int i) {

        List<AltitudeChartValues> altitudeValues = new ArrayList<>();
        List<SpeedChartValues> speedValues = new ArrayList<>();
        List<SpeedChartValues> speedKmhValues = new ArrayList<>();
        

        this.routeId = i;

        Credentials cr = new Credentials();

        this.routePoints = "";
        Connection con_1 = null;
        Statement st_1;
        ResultSet rs_1;
        Koordinat minBounds = new Koordinat();
        minBounds.boylam = 99;
        minBounds.enlem = 99;
        Koordinat maxBounds = new Koordinat();
        maxBounds.boylam = -99;
        maxBounds.enlem = -99;

        try {
            /* TODO output your page here. You may use following sample code. */

            Class.forName("com.mysql.jdbc.Driver");
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT latitude, longitude, speed, altitude, followMeDeviceId, time from followme WHERE routeId = " + routeId + " "
                    + "ORDER BY followme.time ASC";

            rs_1 = st_1.executeQuery(query);
            int a = -2;
            double cumulativeDistance = 0.0;
            
            
            while (rs_1.next()) {
                
                a++;
                points.add("[" + rs_1.getString("latitude") + "," + rs_1.getString("longitude") + "],");
                timeList.add(rs_1.getTimestamp("time"));
                
                
                try {

                    cumulativeDistance = cumulativeDistance + distanceBetweenPoints(noktalar.get(a).enlem, noktalar.get(a).boylam,
                            noktalar.get(a - 1).enlem, noktalar.get(a - 1).boylam);
//                    getDistanceList().add(cumulativeDistance);

                } catch (Exception ex) {

                    System.out.println("Exception while preparing altitude graph data: " + ex.getMessage());
                           LogManager.getLogger(GetRouteDetails.class
                        .getName()).log(Level.INFO, "LogManager: Exception while preparing altitude graph data: "+ ex.getMessage());
                } finally {
                    getDistanceList().add(0.0);
                    AltitudeChartValues value = new AltitudeChartValues();
                    AltitudeChartValues value2 = new AltitudeChartValues();
                    value.x = cumulativeDistance;
                    value.y = rs_1.getDouble("altitude");
                    altitudeValues.add(value);
                    getAltitudeList().add(value.y);
                }
                ////

                try {
                    SpeedChartValues value = new SpeedChartValues();
                    value.x = cumulativeDistance;
                    value.y = rs_1.getDouble("speed");

                    SpeedChartValues valueKmh = new SpeedChartValues();
                    valueKmh.x = cumulativeDistance;
                    valueKmh.y = rs_1.getDouble("speed") * 3.6;
                    getSpeedList().add(value.y);

                    if (value.y != 0) {
                        speedKmhValues.add(valueKmh);
                        speedValues.add(value);
                    }
                } catch (Exception ex) {
                    System.out.println("Exception while preparing speed graph data: " + ex.toString());
//                   SpeedChartValues value = new SpeedChartValues();
//                value.x=a;
//                value.y=0.0;
//                speedValues.add(value);

                }

                this.routePoints = this.routePoints + "[" + rs_1.getString("latitude") + "," + rs_1.getString("longitude") + "],";
                // this.routeAltitudeValues = this.routeAltitudeValues + "[" + rs_1.getString("altitude") + "],";
                //this.routeSpeedValues = this.routeSpeedValues + "[" + rs_1.getString("speed") + "],";

                Koordinat yeniNokta = new Koordinat();
                yeniNokta.enlem = Double.parseDouble(rs_1.getString("latitude"));
                yeniNokta.boylam = Double.parseDouble(rs_1.getString("longitude"));
                try {
                    yeniNokta.speed = Double.parseDouble(rs_1.getString("speed"));
                    if (yeniNokta.speed > getMaxSpeed()) {
                        setMaxSpeed(yeniNokta.speed);
                    }
                } catch (SQLException | NumberFormatException | NullPointerException ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
                try {
                    yeniNokta.altitude = Double.parseDouble(rs_1.getString("altitude"));
                    if (getMaxAltitude() < yeniNokta.altitude) {
                        setMaxAltitude(yeniNokta.altitude);
                    }
                    if (getMinAltitude() > yeniNokta.altitude) {
                        setMinAltitude(yeniNokta.altitude);
                    }

                } catch (SQLException | NumberFormatException | NullPointerException ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
                getLatitudeList().add(yeniNokta.enlem);
                getLongitudeList().add(yeniNokta.boylam);
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

                this.mapBounds = "[[" + minBounds.enlem + "," + minBounds.boylam + "],"
                        + "[" + maxBounds.enlem + "," + maxBounds.boylam + "]]";

//  this.deviceName = rs_1.getString("name");
                //  this.deviceId = rs_1.getInt("followMeDeviceId");
                //  this.routeCreationDate = rs_1.getTimestamp("time");
            }
            this.setRouteAltitudeValues(new Gson().toJson(altitudeValues));
            this.setRouteSpeedValues(new Gson().toJson(speedValues));
            this.setRouteSpeedKmhValues(new Gson().toJson(speedKmhValues));
            

            if (!points.isEmpty()) {
                routeStartPoint = points.get(0).toString();
                routeEndPoint = points.get(points.size() - 1).toString();
                prepareRouteDetails(this.routeId);
                this.deviceName = deviceNameFromId(this.deviceId);
            }
            

        } catch ( SQLException e) {

        } catch (ClassNotFoundException ex) {
                    LogManager.getLogger(GetRouteDetails.class
                        .getName()).log(Level.INFO, ex.getMessage());
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
        distanceList.clear();
        for (int i = 0; i < noktalar.size() - 1; i++) {
           try{
            this.routeLength = this.routeLength + distanceBetweenPoints(noktalar.get(i).enlem, noktalar.get(i).boylam, noktalar.get(i + 1).enlem, noktalar.get(i + 1).boylam);
          distanceList.add(this.routeLength);
           }catch (Exception ex){
               System.out.println("Exception handled " + ex.getMessage());
           }
       
        }
        System.out.println("Route Length: " + this.routeLength);
        
    }

    private double distanceBetweenPoints(double latFirst, double lngFirst, double latSecond, double lngSecond) {

        double R = 6371.0; // metres
        double d;
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
        Statement st_1;
        ResultSet rs_1;
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

        } finally {
            try {
                con_1.close();

            } catch (SQLException ex) {
                      LogManager.getLogger(GetRouteDetails.class
                        .getName()).log(Level.INFO, ex.getMessage());
            }
        }
        return name;
    }

    private void prepareRouteDetails(int id) {

        Credentials cr = new Credentials();

        Connection con_1 = null;
        Statement st_1;
        ResultSet rs_1;
        try {
            /* TODO output your page here. You may use following sample code. */

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT followMeDeviceId, time, type, updateTime from route where id = " + id;
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                this.deviceId = rs_1.getInt("followMeDeviceId");
                this.routeCreationDate = rs_1.getTimestamp("time");
                this.updateTime=rs_1.getTimestamp("updateTime");
                this.setRouteType(rs_1.getInt("type"));
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {

        } finally {
            try {
                con_1.close();

            } catch (SQLException ex) {
                LogManager.getLogger(GetRouteDetails.class
                        .getName()).log(Level.INFO, ex.getMessage());
            }
        }
        calculateRouteLength();

    }
    
   

    public String getMapBounds() {
        return this.mapBounds;
    }
}