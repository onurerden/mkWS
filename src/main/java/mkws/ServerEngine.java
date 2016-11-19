/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import mkws.Model.MKMission;
import mkws.Model.MMRUser;
import mkws.Model.MMRWorkout;
import mkws.Model.MkwsUser;
import mkws.Model.OAuthToken;
import mkws.Model.Waypoint;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author oerden
 */
public class ServerEngine implements IDeviceServer {
    
    @Override
    public String touchServer(String uid, String deviceType) {
        
        int deviceId = -1;
        String queryString = "";
        MKSession sessionInfo = new MKSession();
        sessionInfo.setDeviceId(-1);

        //Device device = Device.other;
        DeviceTypes device = DeviceTypes.OTHER;
        
        try {
            device = DeviceTypes.valueOf(deviceType.toUpperCase());
            sessionInfo.setDeviceType(device);
            System.out.println(device.getName());
        } catch (Exception ex) {
            System.out.println("DeviceType alınamadı.");
        }
        
        switch (device) {
            case MK: {
                queryString = "SELECT * from kopter where UID = '" + uid + "'";
                System.out.println("Case MK");
                break;
            }
            case MP: {
                queryString = "SELECT * from followmedevices where UID = '" + uid + "'";
                System.out.println("Case MP");
                break;
            }
        }
        
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            
            rs_1 = st_1.executeQuery(queryString);
            
            switch (device) {
                case MK: {
                    deviceId = -2;
                    sessionInfo.setDeviceId(-2);
                    
                    if (rs_1.next()) {
                        MKopter kopter = new MKopter();
                        kopter.id = rs_1.getInt("id");
                        kopter.name = rs_1.getString("name");
                        kopter.isActive = rs_1.getBoolean("active");
                        kopter.uid = rs_1.getString("uid");
                        
                        System.out.println("query device uid = " + kopter.uid + " device type mk");
                        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        Statement st_2 = con_1.createStatement();
                        st_2.executeUpdate("UPDATE kopter SET latestTouch = NOW() WHERE id = " + kopter.id);
                        
                        deviceId = kopter.id;
                        sessionInfo.setDeviceId(kopter.id);
                    }
                    break;
                }
                case MP: {
                    System.out.println("Case MP");
                    deviceId = -3;
                    sessionInfo.setDeviceId(-3);
                    if (rs_1.next()) {
                        MobilePhone phone = new MobilePhone();
                        phone.id = rs_1.getInt("id");
                        phone.name = rs_1.getString("name");
                        phone.uid = rs_1.getString("uid");
                        phone.registerDate = rs_1.getTimestamp("registerDate");
                        
                        System.out.println("query device uid = " + phone.uid + " device type phone");
                        
                        deviceId = phone.id;
                        sessionInfo.setDeviceId(deviceId);
                    }
                    break;
                }
                default: {
                    deviceId = -4;
                    sessionInfo.setDeviceId(-4);
                    break;
                }
            }
            con_1.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        sessionInfo.setSessionId(createSession(sessionInfo.getDeviceId(), sessionInfo.getDeviceType()));
        Gson json = new Gson();
        System.out.println(json.toJson(sessionInfo).toString());
        return json.toJson(sessionInfo);
    }
    
    @Override
    public int registerDevice(String uid, String name, String deviceType) {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int deviceId = -1;
        Gson jsonObject = new Gson();
        deviceId = jsonObject.fromJson(touchServer(uid, deviceType), MKSession.class).getDeviceId();
        if (name.length() == 0) {
            name = "defaultName";
        }
        
        if ((deviceId == -2) || (deviceId == -3)) {
            String queryString = "";
            
            DeviceTypes device = DeviceTypes.OTHER;
            try {
                device = DeviceTypes.valueOf(deviceType.toUpperCase());
            } catch (Exception ex) {
                System.out.println("Device Enumeration cannot be done.");
            }
            //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            switch (device) {
                case MK: {
                    queryString = "INSERT INTO `kopter`(`UID`, `name`, `active`) VALUES ('" + uid + "', '" + name + "', 1)";
                    break;
                }
                case MP: {
                    queryString = "INSERT INTO `followmedevices`(`UID`, `name`, `registerDate`) VALUES ('" + uid + "', '" + name + "', NOW())";
                    break;
                }
            }
            
            Credentials cr = new Credentials();
            Connection con_1;
            Statement st_1;
            //ResultSet rs_1 = null;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
                st_1 = con_1.createStatement();
                
                st_1.executeUpdate(queryString, Statement.RETURN_GENERATED_KEYS);
                deviceId = jsonObject.fromJson(touchServer(uid, deviceType), MKSession.class).getDeviceId();
                
                con_1.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return deviceId;
    }
    
    @Override
    public int sendStatus(String jsonStatus) {
        int result = -1;
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        System.out.println(jsonStatus);
        String queryString = "";
        
        KopterStatus status = new KopterStatus();
        
        Gson jsonObject = new Gson();
        status = jsonObject.fromJson(jsonStatus, KopterStatus.class);
        if (status.getKopterVario() > 32768) {
            status.setKopterVario(status.getKopterVario() - 65536);
        }
        status.setFlagsNC(addZerosToByte(status.getFlagsNC()));
        status.setFcStatusFlags1(addZerosToByte(status.getFcStatusFlags1()));
        status.setFcStatusFlags2(addZerosToByte(status.getFcStatusFlags2()));

        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        queryString = "INSERT INTO `kopterstatus`(`kopterId`, `altitude`,"
                + "`latitude`, `longitude`, `heading`, `kopterErrorCode`, `gsmSignalStrength`, "
                + "`kopterVoltage`, `gpsSatCount`, `batteryCurrent`, `batteryCapacity`, "
                + "`kopterSpeed`, `kopterRcSignal`, `kopterVario`, `ncFlags`, `fcFlags1`,"
                + "`fcFlags2`, `updateTime`,`sessionId`,`blTempList`,`targetLatitude`,`targetLongitude` ) VALUES ("
                + status.getKopterId() + ", "
                + status.getKopterAltitude() + ", "
                + status.getKopterLatitude() + ", "
                + status.getKopterLongitude() + ", "
                + status.getKopterHeading() + ", "
                + status.getKopterErrorCode() + ", "
                + status.getGsmSignalStrength() + ", "
                + status.getKopterVoltage() + ", "
                + status.getGpsSatCount() + ", "
                + status.getBatteryCurrent() + ", "
                + status.getBatteryCapacity() + ", "
                + status.getKopterSpeed() + ", "
                + status.getKopterRcSignal() + ", "
                + status.getKopterVario() + ", '"
                + status.getFlagsNC() + "', '"
                + status.getFcStatusFlags1() + "', '"
                + status.getFcStatusFlags2() + "', "
                + "NOW(), "
                + status.getSessionId() + ", '"
                + status.getBlTempList() + "',"
                + status.getTargetLatitude() + ","
                + status.getTargetLongitude() + ""
                + ")";
        System.out.println(queryString);
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            System.out.println(queryString);
            result = -2;
            result = st_1.executeUpdate(queryString);
            con_1.close();
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    @Override
    public String getTask(int deviceId) {
//        return "not implemented";

        String output = "-1";
        String queryString;
        
        Credentials cr = new Credentials();
        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        int matchedDevice = getMatchedDevice(deviceId);
        switch (matchedDevice) {
            case -3: {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    
                    con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
                    st_1 = con_1.createStatement();
                    
                    queryString = "SELECT * from waypoints "
                            + "WHERE missionId = (SELECT max(missionId) from koptermission WHERE kopterId = " + deviceId
                            + ")ORDER BY waypoints.id DESC";
                    System.out.println(queryString);
                    rs_1 = st_1.executeQuery(queryString);
                    MKMission mission = new MKMission();
                    mission.mkId = deviceId;
                    mission.waypoints = new ArrayList<>();
                    while (rs_1.next()) {
                        Waypoint wp = new Waypoint();
                        wp.targetLat = rs_1.getDouble("latitude");
                        wp.targetLon = rs_1.getDouble("longitude");
                        wp.targetHeight = rs_1.getInt("height");
                        wp.targetAngle = rs_1.getInt("angle");
                        wp.autoTrigger = rs_1.getInt("autoTrigger");
                        wp.isPOI = rs_1.getBoolean("isPOI");
                        wp.radius = rs_1.getInt("radius");
                        wp.targetSpeed = rs_1.getInt("speed");
                        wp.vario = rs_1.getInt("vario");
                        wp.waitingTime = rs_1.getInt("waitingTime");
                        wp.waypointEvent = rs_1.getInt("wpEvent");
                        wp.missionId = rs_1.getInt("missionId");
                        
                        mission.waypoints.add(wp);
                        
                    }
                    
                    FollowMeData fmData = new FollowMeData();
                    fmData.setMission(mission);
                    
                    fmData.event = 4;
                    Gson json = new Gson();
                    
                    if (mission.waypoints.size() > 0) {
                        if (!isMissionSent(mission.waypoints.get(0).missionId)) {
                            output = json.toJson(fmData);
                            setMissionSent(mission.waypoints.get(0).missionId);
                        } else {
                            fmData.setDoNothing(true);
                            output = json.toJson(fmData);
                        }
                    }
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            break;
            
            default: {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
                    st_1 = con_1.createStatement();
                    
                    queryString = "SELECT * from followme WHERE followMeDeviceId = " + matchedDevice
                            + " AND sent = 0 "
                            + " AND followme.time > DATE_SUB(NOW(), INTERVAL 1 MINUTE)"
                            + " ORDER BY time DESC LIMIT 1";
                    System.out.println(queryString);
                    rs_1 = st_1.executeQuery(queryString);
                    FollowMeData data = new FollowMeData();
                    
                    if (rs_1.next()) {
                        //data.kopterID = rs_1.getInt("kopterId");
                        System.out.println("Getting Data");
                        data.setBearing(rs_1.getInt("bearing"));
                        data.setLat(rs_1.getDouble("latitude"));
                        data.setLon(rs_1.getDouble("longitude"));
                        data.setEvent(rs_1.getInt("event"));
                        data.setTime(rs_1.getTimestamp("time"));
                        data.setFollowMeDeviceId(rs_1.getInt("followMeDeviceId"));
                        System.out.println("Data Acquired");
                    } else {
                        data.setEvent(3);
                        data.setName("ComingHome");
                    }
                    
                    Gson json = new Gson();
                    
                    output = json.toJson(data);
                    System.out.println(output);
                    con_1.close();
                } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    output = "-2";
                    Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        
        return output;
    }
    
    @Override
    public int sendFollowMeData(String json) {
        FollowMeData data;
      //  Gson gson = new Gson();
        Gson gson = new GsonBuilder()
   .setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        
        try {
            data = gson.fromJson(json, FollowMeData.class);
        } catch (JsonSyntaxException ex) {
            System.out.println("FollowMeData parse error: " + ex.toString());
            return -2;
        }
        int result = sendFollowMeData(data);
        return result;
    }
    
    public int sendFollowMeData(FollowMeData data) {
        
        Credentials cr = new Credentials();
        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        String queryString;
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            
            queryString = "INSERT INTO followme "
                    + "(`latitude`, `longitude`, `bearing`, `event`, `time`, `followMeDeviceId`, `sent`,`routeId`,`sessionId`, `speed`, `altitude` ) "
                    + "VALUES ("
                    + "'" + data.getLat() + "', "
                    + "'" + data.getLon() + "', "
                    + "'" + data.getBearing() + "', "
                    + "'" + data.getEvent() + "', ";
            if (data.getTime() != null) {
                queryString = queryString + "'" + data.getTime().toString() + "', ";
            } else {
                queryString = queryString + "NOW(), ";
            }
            queryString = queryString + "'" + data.getFollowMeDeviceId() + "', "
                    + "'0', "
                    + "'" + data.getRouteId() + "', "
                    + "'" + data.getSessionId() + "', "
                    + "'" + data.getSpeed() + "', "
                    + "'" + data.getAltitude() + "')";
            
            System.out.println(queryString);
            st_1.executeUpdate(queryString);
            con_1.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception: " + ex.getMessage());
            return -1;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        
        return 0;
    }
    
    private int getMatchedDevice(int deviceId) {
        int matchedFollowMeDevice = -1;
        boolean hasMission = false;
        
        Credentials cr = new Credentials();
        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        String queryString = "SELECT followMeDeviceId, hasMission "
                + "from devicematching "
                + "where id=(SELECT max(id) from devicematching where kopterId=" + deviceId + ")";
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            
            rs_1 = st_1.executeQuery(queryString);
            
            while (rs_1.next()) {
                matchedFollowMeDevice = rs_1.getInt(1);
                hasMission = rs_1.getBoolean("hasMission");
                
            }
            System.out.println("Follow followMeDevice: " + matchedFollowMeDevice);
            con_1.close();
            
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (hasMission) {
            return -3;
        }
        return matchedFollowMeDevice;
    }
    
    @Override
    public String getKopterStatus(int deviceId) {
        System.out.println("Looking for KopterStatus " + deviceId);
        String kopterStatusString = "-2";
        KopterStatus status = new KopterStatus();
        
        Credentials cr = new Credentials();
        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        String queryString = "SELECT * from kopterstatus WHERE kopterId = " + deviceId + " ORDER BY `kopterstatus`.`id` DESC LIMIT 1";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            System.out.println(queryString);
            rs_1 = st_1.executeQuery(queryString);
            System.out.println("KopterStatus found for " + deviceId);
            
            if (rs_1.next()) {
                status.setKopterId(rs_1.getInt("kopterId"));
                status.setKopterAltitude(rs_1.getInt("altitude"));
                status.setKopterLatitude(rs_1.getDouble("latitude"));
                status.setKopterLongitude(rs_1.getDouble("longitude"));
                status.setKopterHeading(rs_1.getInt("heading"));
                status.setKopterErrorCode(rs_1.getInt("kopterErrorCode"));
                status.setGsmSignalStrength(rs_1.getInt("gsmSignalStrength"));
                status.setKopterVoltage(rs_1.getInt("kopterVoltage"));
                status.setGpsSatCount(rs_1.getInt("gpsSatCount"));
                status.setBatteryCurrent(rs_1.getInt("batteryCurrent"));
                status.setBatteryCapacity(rs_1.getInt("batteryCapacity"));
                status.setKopterSpeed(rs_1.getDouble("kopterSpeed"));
                status.setKopterRcSignal(rs_1.getInt("kopterRcSignal"));
                status.setKopterVario(rs_1.getInt("kopterVario"));
                status.setFlagsNC(rs_1.getString("ncFlags"));
                status.setUpdateTime(rs_1.getTimestamp("updateTime"));
                status.setSessionId(rs_1.getInt("sessionId"));
                status.setFcStatusFlags1(rs_1.getString("fcFlags1"));
                status.setFcStatusFlags2(rs_1.getString("fcFlags2"));
                status.setBlTempList(rs_1.getString("blTempList"));
                
                System.out.println("All values Set");
                
                Gson gson = new Gson();
                kopterStatusString = gson.toJson(status, KopterStatus.class);
                
            }
            con_1.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return "-1";
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return kopterStatusString;
        
    }
    
    @Override
    public String getFollowMeData(int deviceId) {
        String jsonString = "";
        System.out.println("Looking for FollowMeData " + deviceId);
        FollowMeData data = new FollowMeData();
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        ResultSet rs_1;
        String queryString;
        if (deviceId == 0) {
//            queryString = "SELECT * from followme f INNER JOIN "
//                    + "(SELECT max(id) AS maksimum_id from followme "
//                    + "GROUP BY followMeDeviceId) AS s "
//                    + "ON s.maksimum_id = f.id";
            queryString = "SELECT * from followme "
                    + "INNER JOIN (SELECT max(id) AS maksimum_id from followme "
                    + "GROUP BY followMeDeviceId) AS s ON s.maksimum_id = followme.id "
                    + "INNER JOIN mk.followmedevices AS d ON followme.followMeDeviceId = d.id "
                    + "WHERE followme.time > DATE_SUB(CURRENT_TIMESTAMP(),INTERVAL 1 MINUTE)";
            
        } else {
            queryString = "SELECT * from followme "
                    + "INNER JOIN followmedevices AS d "
                    + "ON followme.followMeDeviceId = d.id "
                    + "WHERE followme.followMeDeviceId = " + deviceId
                    + " ORDER BY followme.`id` DESC LIMIT 1";
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            System.out.println(queryString);
            rs_1 = st_1.executeQuery(queryString);
            System.out.println("FollowMeData found for " + deviceId);
            if (deviceId != 0) {
                if (rs_1.next()) {
                    data.setBearing(rs_1.getInt("bearing"));
                    data.setEvent(rs_1.getInt("event"));
                    data.setLat(rs_1.getDouble("latitude"));
                    data.setLon(rs_1.getDouble("longitude"));
                    data.setTime(rs_1.getTimestamp("time"));
                    data.setFollowMeDeviceId(deviceId);
                    data.setName(rs_1.getString("name"));
                    data.setRouteId(rs_1.getInt("routeId"));
                    data.setSessionId(rs_1.getInt("sessionId"));
                    
                    Gson gson = new Gson();
                    jsonString = gson.toJson(data, FollowMeData.class);
                }
                
            } else {
                ArrayList<FollowMeData> datas = new ArrayList<FollowMeData>();
                while (rs_1.next()) {
                    data = new FollowMeData();
                    data.setBearing(rs_1.getInt("bearing"));
                    data.setEvent(rs_1.getInt("event"));
                    data.setLat(rs_1.getDouble("latitude"));
                    data.setLon(rs_1.getDouble("longitude"));
                    data.setTime(rs_1.getTimestamp("time"));
                    data.setFollowMeDeviceId(rs_1.getInt("followMeDeviceId"));
                    data.setName(rs_1.getString("name"));
                    data.setRouteId(rs_1.getInt("routeId"));
                    data.setSessionId(rs_1.getInt("sessionId"));
                    datas.add(data);
                }
                
                Gson gson = new Gson();
                jsonString = gson.toJson(datas);
            }
            con_1.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return "-1";
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return "-2";
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return "-3";
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return "-4";
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonString;
    }
    
    @Override
    public int getRouteId(int deviceId) {
        int routeId = -1;
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        ResultSet rs_1;
        String queryString = "INSERT INTO route SET followMeDeviceId = " + deviceId + " , time = NOW()";
        Statement st_2;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Instance Created");
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            System.out.println("Connection Created");
            st_1 = con_1.createStatement();
            System.out.println("StatementCreated:");
            System.out.println(queryString);
            st_1.executeUpdate(queryString);
            System.out.println("routeId for deviceId = " + deviceId + " is created.");
            
            st_2 = con_1.createStatement();
            queryString = "SELECT MAX(id) AS maxId from mk.route WHERE followMeDeviceId = " + deviceId;
            System.out.println(queryString);
            rs_1 = st_2.executeQuery(queryString);
            
            while (rs_1.next()) {
                routeId = rs_1.getInt("maxId");
                System.out.println("New routeId for deviceId = " + deviceId + " is " + routeId);
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return routeId;
    }
    
    @Override
    public int setTask(int kopterId, int followMeDeviceId) {
        
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        ResultSet rs_1;
        String queryString;
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            queryString = "INSERT INTO devicematching SET kopterId =" + kopterId + ", followMeDeviceId =" + followMeDeviceId;
            
            st_1.executeUpdate(queryString);
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return 1;
    }
    
    @Override
    public int sendLog(String logJson) {
        int status = -2;
        LogMessage msg = new LogMessage();
        Gson gson = new Gson();
        
        try {
            msg = gson.fromJson(logJson, LogMessage.class);
        } catch (Exception ex) {
            System.out.println("LogMessage Parse error: " + ex.toString());
            return -2;
        }
        
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        
        msg.setLogMessage(msg.getLogMessage().replace("'", "\\'"));
        String query = "INSERT INTO mk.logs SET logLevel = '" + msg.getLogLevel()
                + "', logMessage = '" + msg.getLogMessage() + "'";
        
        
        try {
            DeviceTypes deviceType = DeviceTypes.valueOf(msg.getDeviceType().toUpperCase());
            switch (deviceType) {
                case MK: {
                    query = query + ", " +  "kopterId = '" + msg.getDeviceId() + "'";
                    break;
                }
                case MP: {
                    query = query + ", " + "followMeDeviceId = '" + msg.getDeviceId() + "'";
                    break;
                }
                case SERVER: {
                    query = query + "";
                    break;
                }
                default: {
                    query = query + "";
                    break;
                }
            }
            
            
        } catch (NullPointerException ex) {
            System.out.println("NullPointerException at sendLog: " + ex.toString());
        }
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            System.out.println(query);
            st_1.execute(query);
            status = 0;
            
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex) {
            System.out.println("Exception while sendLog: " + ex.toString());
            status = -1;
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                
            }
        }
        
        return status;
    }

//    public enum Device {
//
//        mk, mp, other;
//    }
    @Override
    public int endRoute(int routeId) {
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        
        String query = "UPDATE  `route` SET isEnded =1 WHERE id =" + routeId;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
        return 0;
    }
    
    private int createSession(int deviceId, DeviceTypes type) {
        int sessionId = -1;
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        boolean isKopter = type.equals(DeviceTypes.MK);
        
        String query = "INSERT INTO mk.session SET deviceId= '" + deviceId + "',"
                + " deviceType = '" + type.getName().toString() + "',"
                + " isKopter = " + isKopter;
        System.out.println(query);
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
            
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                
            }
        }
        query = "SELECT * from mk.session WHERE deviceId='" + deviceId + "'" + " AND deviceType = '" + type.getName().toString()
                + "' ORDER BY id DESC LIMIT 1";
        System.out.println(query);
        Connection con_2 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            con_2 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            Statement st_2 = con_2.createStatement();
            ResultSet rs_2 = st_2.executeQuery(query);
            rs_2.next();
            sessionId = rs_2.getInt("id");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            sessionId = -2;
            
        } finally {
            try {
                con_2.close();
            } catch (SQLException ex) {
                sessionId = -3;
            }
        }
        
        return sessionId;
    }
    
    private String addZerosToByte(String preString) {
        if (preString.length() > 8) {
            preString = "00000000";
            return preString;
        }
        int i = preString.length();
        String preZero = "";
        for (int a = 0; a < 8 - i; a++) {
            preZero = preZero + "0";
        }
        return preZero + preString;
    }
    
    @Override
    public String ping() {
        System.out.println("I am the one who will send 'Pongs'");
        return null;
        
    }
    
    public ArrayList<KopterStatus> getKopterSessionData(int sessionId) {
        ArrayList<KopterStatus> sessionList = new ArrayList<>();
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        
        String query = "SELECT * from kopterstatus WHERE sessionId=" + sessionId;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            ResultSet rs_1 = st_1.executeQuery(query);
            while (rs_1.next()) {
                KopterStatus newStatus = new KopterStatus();
                newStatus.setKopterAltitude(rs_1.getInt("altitude"));
                newStatus.setKopterLatitude(rs_1.getDouble("latitude"));
                newStatus.setKopterLongitude(rs_1.getDouble("longitude"));
                newStatus.setKopterHeading(rs_1.getInt("heading"));
                newStatus.setKopterErrorCode(rs_1.getInt("kopterErrorCode"));
                newStatus.setGsmSignalStrength(rs_1.getInt("gsmSignalStrength"));
                newStatus.setKopterVoltage(rs_1.getInt("kopterVoltage"));
                newStatus.setGpsSatCount(rs_1.getInt("gpsSatCount"));
                newStatus.setBatteryCurrent(rs_1.getInt("batteryCurrent"));
                newStatus.setBatteryCapacity(rs_1.getInt("batteryCapacity"));
                newStatus.setKopterSpeed(rs_1.getInt("kopterSpeed"));
                newStatus.setKopterRcSignal(rs_1.getInt("kopterRcSignal"));
                newStatus.setKopterVario(rs_1.getInt("kopterVario"));
                newStatus.setUpdateTime(rs_1.getTimestamp("updateTime"));
                newStatus.setTargetLatitude(rs_1.getDouble("targetLatitude"));
                newStatus.setTargetLongitude(rs_1.getDouble("targetLongitude"));
                newStatus.setSessionId(sessionId);
                
                sessionList.add(newStatus);
            }
            
        } catch (Exception ex) {
            System.out.println("Error at collecting session data");
        }
        
        return sessionList;
        
    }
    
    ;

    @Override
    public int saveGPXContent(String gpxString, String uid) {
        
        System.out.println(uid + " bir gpx dosyası gönderdi.");
        
        DOMParser parser = new DOMParser();
        int routeId = 0;
        boolean success = true;
        try {

            //parser.parse(gpxString);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(gpxString.getBytes(StandardCharsets.UTF_8));
            Document doc = dBuilder.parse(stream); //parser.getDocument();

            MKSession session = new MKSession();
            Gson jsonObject = new Gson();
            session = jsonObject.fromJson(touchServer(uid, "MP"), MKSession.class);
            if (session.getSessionId() < 0) {
                return -1;
            }
            if (session.getDeviceId() < 0) {
                return -2;
            }
            routeId = getRouteId(session.getDeviceId());
            
            NodeList nl = doc.getElementsByTagName("*");
            Node n;
            for (int i = 0; i < nl.getLength(); i++) {
                
                n = nl.item(i);
                // System.out.print(n.getNodeName() + " ");
            }
            
            NodeList nList = doc.getElementsByTagName("trkpt");
            
            System.out.println("----------------------------");
            int temp = 0;
            for (temp = 0; temp < nList.getLength(); temp++) {
                
                Node nNode = nList.item(temp);

                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    FollowMeData fmData = new FollowMeData();
                    Element eElement = (Element) nNode;
                    
                    try {
                        
                        fmData.setLat(Double.valueOf(eElement.getAttribute("lat")));
                        fmData.setLon(Double.valueOf(eElement.getAttribute("lon")));
                         if(eElement.getElementsByTagName("ele").item(0)!=null){
                        fmData.setAltitude(Double.valueOf(eElement.getElementsByTagName("ele").item(0).getTextContent()));
                         }
                        if(eElement.getElementsByTagName("speed").item(0)!=null){
                        fmData.setSpeed(Double.valueOf(eElement.getElementsByTagName("speed").item(0).getTextContent()));
                        }
                        fmData.setRouteId(routeId);
                        fmData.setSessionId(session.getSessionId());
                        fmData.setFollowMeDeviceId(session.getDeviceId());
                        
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String date = eElement.getElementsByTagName("time").item(0).getTextContent().replace("T", " ").replace("Z","");
                        Date parsedDate = dateFormat.parse(date);
                        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                        
                        fmData.setTime(timestamp);
                                                
                        System.out.println("Latitude: " + fmData.getLat());
                        System.out.println("Longitude: " + fmData.getLat());
                        System.out.println("Elevation: " + fmData.getAltitude());
                        System.out.println("Speed: " + fmData.getSpeed());
                        sendFollowMeData(fmData);
                    } catch (NumberFormatException | DOMException ex) {
                        System.out.println("Error Message:" + ex.toString());
                        success = false;
                    } catch (Exception ex) {
                        System.out.println("Error Message:" + ex.toString());
                        success = false;
                    }
                    
                }
            }
            if (success) {
                LogMessage log = new LogMessage();
                log.setLogMessage(session.getDeviceId() + " id\'li followMe cihazı " + temp + " adet konum içeren GPX dosyasını başarıyla yükledi. RouteId: " + routeId);
                log.setDeviceId(session.getDeviceId());
                log.setDeviceType("MP");
                log.setLogLevel(2);
                endRoute(routeId);
                System.out.println("GPX dosyası başarıyla girildi.");
                sendLog(new Gson().toJson(log));
            } else {
                return -3;
            }
            
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception thrown while GPX import: " + ex.getMessage());
            return -3;
        }
        
        return 1;
    }
    
    @Override
    public int saveMMRauthorizationCodeForDevice(int deviceId, String deviceType, String code) {
        deviceType = deviceType.toLowerCase();
        DeviceTypes dt = DeviceTypes.OTHER;
        try {
            dt = DeviceTypes.valueOf(deviceType.toUpperCase());
            System.out.println(dt.getName());
        } catch (Exception ex) {
            System.out.println("DeviceType alınamadı.");
        }
        
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        String query;
        switch (dt) {
            case MP: {
                
                query = "INSERT INTO mmrauthorization (code,followMeDeviceId ) VALUES (\""
                        + code + "\","
                        + deviceId + ")";
                break;
            }
            case MK: {
                query = "INSERT INTO mk.mmrauthorization (code,kopterId ) VALUES (\""
                        + code + "\","
                        + deviceId + ")";
                break;
            }
            default: {
                return -1;
            }
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Error while saving MMR Authorization code: " + ex.getMessage());
            return -1;
        }
        return 1;
    }
    
    public int saveMMRauthorizationCodeForUser(Integer userId, String code) {
                
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        String query;
        
                query = "INSERT INTO mk.mmrauthorization (code,userId ) VALUES (\""
                        + code + "\","
                        + userId + ")";
                
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Error while saving MMR Authorization code: " + ex.getMessage());
            return -1;
        }
        return 1;
    }
    
    public int saveMMRauthorizationToken(int deviceId, String deviceType, OAuthToken token) {
        deviceType = deviceType.toLowerCase();
        DeviceTypes dt = DeviceTypes.OTHER;
        try {
            dt = DeviceTypes.valueOf(deviceType.toUpperCase());
            System.out.println(dt.getName());
        } catch (Exception ex) {
            System.out.println("DeviceType alınamadı.");
        }
        
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        String query;
        
        switch (dt) {
            case MP: {
                
                query = "INSERT INTO mmrauthorization (code,followMeDeviceId,access_token,refresh_token,expires_in) VALUES (\""
                        + token.getAuthorizationCode() + "\","
                        + deviceId + ",\""
                        + token.getAccess_token() + "\",\""
                        + token.getRefresh_token() + "\","
                        + token.getExpires_in()
                        + ")";
                break;
            }
            case MK: {
                query = "INSERT INTO mk.mmrauthorization (code,kopterId,access_token,refresh_token,expires_in ) VALUES (\""
                        + token.getAuthorizationCode() + "\","
                        + deviceId + ",\""
                        + token.getAccess_token() + "\",\""
                        + token.getRefresh_token() + "\","
                        + token.getExpires_in()
                        + ")";
                break;
            }
            default: {
                return -1;
            }
        }
        try {
            System.out.println(query);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Error while saving MMR Authorization code: " + ex.getMessage());
            return -1;
        }
        return 1;
    }
    
    public int saveMMRauthorizationTokenForUser(int userId, OAuthToken token) {
                
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        String query;
        
       
                
                query = "INSERT INTO mmrauthorization (code,userId,access_token,refresh_token,expires_in) VALUES (\""
                        + token.getAuthorizationCode() + "\","
                        + userId + ",\""
                        + token.getAccess_token() + "\",\""
                        + token.getRefresh_token() + "\","
                        + token.getExpires_in()
                        + ")";
                      
        try {
            System.out.println(query);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Error while saving MMR Authorization code: " + ex.getMessage());
            return -1;
        }
        return 1;
    }
    
    public String mmrAccessTokenForUser(int userId) throws Exception{
        OAuthToken token = new OAuthToken();
        Credentials cr = new Credentials();
        token.setClientId(cr.getMmrClientIdForWeb());
        token.setClientSecret(cr.getMmrClientSecretForWeb());
        String queryString = "SELECT code, access_token, refresh_token, scope, expires_in FROM mk.mmrauthorization WHERE userId = " + userId + " ORDER BY id DESC LIMIT 1";
        Connection con_1 = null;
        Statement st_1 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            ResultSet rs = st_1.executeQuery(queryString);
            
            if (rs.next()) {
                token.setAuthorizationCode(rs.getString("code"));
                token.setAccess_token(rs.getString("access_token"));
                token.setRefresh_token(rs.getString("refresh_token"));
                token.setScope(rs.getString("scope"));
                token.setExpires_in(String.valueOf(rs.getInt("expires_in")));
            } else {
                System.out.println("no authorization record found for userId: " + userId);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Error on querying MMR OAuth2 Code");
            return "Error on querying MMR OAuth2 Code";
        }
        if (!token.getRefresh_token().equals("Not Set")) {
            String newToken = token.refreshToken();
            saveMMRauthorizationTokenForUser(userId, token);
            return newToken;
        }
        
        if (!token.getAuthorizationCode().equals("Not Set")) {
            String newToken = token.getNewAccessToken();
            saveMMRauthorizationTokenForUser(userId, token);
            return newToken;
        }
        
        return "No Authorization Code";
        
    }
    public String mmrAccessToken(int deviceId, String deviceType) throws Exception {
        OAuthToken token = new OAuthToken();
        Credentials cr = new Credentials();
        token.setClientId(cr.getMmrClientId());
        token.setClientSecret(cr.getMmrClientSecret());
        
        DeviceTypes dt = DeviceTypes.OTHER;
        
        try {
            dt = DeviceTypes.valueOf(deviceType.toUpperCase());
            System.out.println(dt.getName());
        } catch (Exception ex) {
            System.out.println("DeviceType alınamadı.");
        }
        String queryString = "";
        switch (dt) {
            case MP: {
                queryString = "SELECT code, access_token, refresh_token, scope, expires_in FROM mk.mmrauthorization WHERE followMeDeviceId = " + deviceId;
                break;
            }
            case MK: {
                queryString = "SELECT code, access_token, refresh_token, scope, expires_in FROM mk.mmrauthorization WHERE kopterId = " + deviceId;
                break;
            }
            default:
                throw new Exception();
        }
        queryString = queryString + " ORDER BY id DESC LIMIT 1";
        
        Connection con_1 = null;
        Statement st_1 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            ResultSet rs = st_1.executeQuery(queryString);
            
            if (rs.next()) {
                token.setAuthorizationCode(rs.getString("code"));
                token.setAccess_token(rs.getString("access_token"));
                token.setRefresh_token(rs.getString("refresh_token"));
                token.setScope(rs.getString("scope"));
                token.setExpires_in(String.valueOf(rs.getInt("expires_in")));
            } else {
                System.out.println("no authorization record found for device" + deviceId);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Error on querying MMR OAuth2 Code");
            return "Error on querying MMR OAuth2 Code";
        }
        if (!token.getRefresh_token().equals("Not Set")) {
            String newToken = token.refreshToken();
            saveMMRauthorizationToken(deviceId, deviceType, token);
            return newToken;
        }
        
        if (!token.getAuthorizationCode().equals("Not Set")) {
            String newToken = token.getNewAccessToken();
            saveMMRauthorizationToken(deviceId, deviceType, token);
            return newToken;
        }
        
        return "No Authorization Code";
    }
    
    public String getMMRUserInfo(int deviceId, String deviceType) {
        String responseString = "";
        Credentials cr = new Credentials();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            //HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/activity_type/");
            HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/user/self/");
            
            httpget.addHeader("Api-Key", cr.getMmrClientId());
            httpget.addHeader("Content-Type", cr.getMmrClientId());
            httpget.addHeader("Authorization", "Bearer " + mmrAccessToken(deviceId, deviceType));
            
            MultipartEntity mpEntity = new MultipartEntity();
            //  mpEntity.addPart("grant_type", new StringBody("authorization_code"));
            //  mpEntity.addPart("code", new StringBody(getAuthorizationCode()));
            //  mpEntity.addPart("client_id", new StringBody(getClientId()));
            //  mpEntity.addPart("client_secret", new StringBody(getClientSecret()));

            //  httppost.setEntity(mpEntity);
            System.out.println("executing request " + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity resEntity = response.getEntity();
            responseString = EntityUtils.toString(resEntity, "UTF-8");
            
            if (responseString.contains("error")) {
                System.out.println("Error while getting Activity Types: " + responseString);
                return "Error while getting Activity Types: " + responseString;
            }
            System.out.println(responseString);
            
        } catch (Exception ex) {
            
        }
        
        return responseString;
    }
    
    public String getMMRUserProfilePicture(int mmrUserId,int userId){
    Credentials cr = new Credentials();
        String responseString="";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            //HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/activity_type/");
            HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/user_profile_photo/"+mmrUserId+"/");
            
            httpget.addHeader("Api-Key", cr.getMmrClientIdForWeb());
            httpget.addHeader("Content-Type", cr.getMmrClientIdForWeb());
            httpget.addHeader("Authorization", "Bearer " + mmrAccessTokenForUser(userId));
            
            MultipartEntity mpEntity = new MultipartEntity();
            
            System.out.println("executing request " + httpget.getRequestLine());
            
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity resEntity = response.getEntity();
            responseString = EntityUtils.toString(resEntity, "UTF-8");
            Gson gson = new Gson();
            
            MMRUser user = gson.fromJson(responseString, MMRUser.class);
            System.out.println("MMR User name is " + user.getDisplay_name());
            System.out.println(responseString);
            if (responseString.contains("error")) {
                System.out.println("Error while getting MMR user info: " + responseString);
                return "Error while getting MMR user info: " + responseString;
            }
            //System.out.println(responseString);
            
        } catch (Exception ex) {
            
        }
        
        return responseString;
        
    }
    public String getMMRUserInfo(int userId) {
        
        Credentials cr = new Credentials();
        String responseString="";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            //HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/activity_type/");
            HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/user/self/");
            
            httpget.addHeader("Api-Key", cr.getMmrClientIdForWeb());
            httpget.addHeader("Content-Type", cr.getMmrClientIdForWeb());
            httpget.addHeader("Authorization", "Bearer " + mmrAccessTokenForUser(userId));
            
            MultipartEntity mpEntity = new MultipartEntity();
            //  mpEntity.addPart("grant_type", new StringBody("authorization_code"));
            //  mpEntity.addPart("code", new StringBody(getAuthorizationCode()));
            //  mpEntity.addPart("client_id", new StringBody(getClientId()));
            //  mpEntity.addPart("client_secret", new StringBody(getClientSecret()));

            //  httppost.setEntity(mpEntity);
            System.out.println("executing request " + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity resEntity = response.getEntity();
            responseString = EntityUtils.toString(resEntity, "UTF-8");
            Gson gson = new Gson();
            
            MMRUser user = gson.fromJson(responseString, MMRUser.class);
            Gson gson2 = new Gson();
            user.setPhotoLinks(gson.fromJson(getMMRUserProfilePicture(user.getId(), userId),MMRUser.MMRUserProfilePhoto.class));
                   
            System.out.println("MMR User name is " + user.getDisplay_name());
            
            if (responseString.contains("error")) {
                System.out.println("Error while getting MMR user info: " + responseString);
                return "Error while getting MMR user info: " + responseString;
            }
            //System.out.println(responseString);
            gson2 = new Gson();
            responseString=gson2.toJson(user);
            System.out.println(responseString);
        } catch (Exception ex) {
            System.out.println("Error while constructing responseString");
        }
        
        return responseString;
    }
    
    @Override
    public int endRoute(int routeId, boolean sendToMMR) {
        int result = endRoute(routeId);
        
        return result;
    }
    
    public int sendMMRWorkout(int routeId) {
        
        MMRWorkout workout = new MMRWorkout();
        workout.populateWorkout(routeId);
        //MMRUser user = new Gson().fromJson(getMMRUserInfo(workout.getDeviceId(), "MP"), MMRUser.class);
        Credentials cr = new Credentials();
        int result = 0;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            
            HttpPost httppost = new HttpPost("https://oauth2-api.mapmyapi.com/v7.1/workout/");
            httppost.addHeader("Api-Key", cr.getMmrClientId());
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("Authorization", "Bearer " + mmrAccessToken(workout.getDeviceId(), "MP"));
            
            StringEntity entity = new StringEntity(new Gson().toJson(workout));
            httppost.setEntity(entity);
            
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            String responseString = EntityUtils.toString(resEntity, "UTF-8");
            System.out.println(responseString);
            System.out.println("MMR response code is:" + response.getStatusLine().getStatusCode());
            if ((int) response.getStatusLine().getStatusCode() / 100 == 2) {
                
                result = 1;
            } else {
                LogMessage msg = new LogMessage();
                msg.setLogLevel(1);
                msg.setDeviceType(DeviceTypes.SERVER.getName());
                msg.setLogMessage("MMR response code is: " + response.getStatusLine().getStatusCode() + " while sending Route: " + routeId + ".\n"
                        + "Response is: \n"
                        + responseString);
                Gson gson = new Gson();
                sendLog(gson.toJson(msg));
            }
            
        } catch (Exception ex) {
            System.out.println("Error while sendMMRWorkout: " + ex.getMessage());
        }
        return result;
    }
    public int sendMMRWorkout(int routeId,int userId) {
        
        MMRWorkout workout = new MMRWorkout();
        workout.populateWorkout(routeId);
        //MMRUser user = new Gson().fromJson(getMMRUserInfo(workout.getDeviceId(), "MP"), MMRUser.class);
        Credentials cr = new Credentials();
        int result = 0;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            
            HttpPost httppost = new HttpPost("https://oauth2-api.mapmyapi.com/v7.1/workout/");
            httppost.addHeader("Api-Key", cr.getMmrClientIdForWeb());
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("Authorization", "Bearer " + mmrAccessTokenForUser(userId));
            
            StringEntity entity = new StringEntity(new Gson().toJson(workout));
            httppost.setEntity(entity);
            
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            String responseString = EntityUtils.toString(resEntity, "UTF-8");
            System.out.println(responseString);
            System.out.println("MMR response code is:" + response.getStatusLine().getStatusCode());
            if ((int) response.getStatusLine().getStatusCode() / 100 == 2) {
                
                result = 1;
            } else {
                LogMessage msg = new LogMessage();
                msg.setLogLevel(1);
                msg.setDeviceType(DeviceTypes.SERVER.getName());
                msg.setLogMessage("MMR response code is: " + response.getStatusLine().getStatusCode() + " while sending Route: " + routeId + ".\n"
                        + "Response is: \n"
                        + responseString);
                Gson gson = new Gson();
                sendLog(gson.toJson(msg));
            }
            
        } catch (Exception ex) {
            System.out.println("Error while sendMMRWorkout: " + ex.getMessage());
        }
        return result;
    }
    
    @Override
    public int saveMission(MKMission mission, String name, String comments) {
        
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1;
        String query;
        
        query = "INSERT into mk.missions (`name`,`comments`) values (\"" + name + "\",\"" + comments + "\");"
                + "SET @last_id_in_table1 = LAST_INSERT_ID();";
        for (Waypoint wp : mission.waypoints) {
            query = query + "INSERT into mk.waypoints (`missionId`, `latitude`,`longitude`,`height`,`speed`,`angle`,`wpEvent`,`radius`,`waitingTime`,`autoTrigger`,`vario`,`isPOI`) "
                    + "VALUES (@last_id_in_table1, "
                    + wp.targetLat + ","
                    + wp.targetLon + ","
                    + wp.targetHeight + ","
                    + wp.targetSpeed + ","
                    + wp.targetAngle + ","
                    + wp.waypointEvent + ","
                    + wp.radius + ","
                    + wp.waitingTime + ","
                    + wp.autoTrigger + ","
                    + wp.vario + ","
                    + wp.isPOI
                    + ");";
        }
        if (mission.mkId != 0) {
            query = query + "INSERT into mk.koptermission (`missionId`, `kopterId`) VALUES (@last_id_in_table1, " + mission.mkId + ");";
            query = query + "INSERT into mk.devicematching (`kopterId`, `hasMission`) VALUES (" + mission.mkId + ", true);";
        }
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.execute(query);
            con_1.close();
        } catch (SQLException ex) {
            return -1;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        
        return 1;
    }
    
    @Override
    public String getMission(int kopterId) {
        
        return new String();
    }
    
    private void setMissionSent(int missionId) {
        try {
            Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "UPDATE koptermission SET isSent = 1 WHERE missionId = " + missionId;
            st_1.execute(query);
            con_1.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public boolean deleteRoute(int routeId){
        try {
            Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "UPDATE route SET isDeleted = TRUE WHERE id = " + routeId;
            st_1.execute(query);
            con_1.close();
            LogMessage msg = new LogMessage();
            msg.setDeviceType(DeviceTypes.SERVER.getName());
            msg.setLogLevel(2);
            msg.setLogMessage("Route "+ routeId + " is deleted.");
            Gson gson = new Gson();
            sendLog(gson.toJson(msg));
            
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    private boolean isMissionSent(int missionId) {
        boolean isSent = false;
        Credentials cr = new Credentials();
        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        String queryString = "SELECT isSent from koptermission WHERE missionId = " + missionId;
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            
            rs_1 = st_1.executeQuery(queryString);
            while (rs_1.next()) {
                isSent = rs_1.getBoolean("isSent");
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            
        }
        return isSent;
    }
    
    public boolean deleteMapMyRideToken(int userId){
        OAuthToken token = new OAuthToken();
        token.setAuthorizationCode("Not Set");
        token.setClientSecret("Not Set");
        token.setRefresh_token("Not Set");
        token.setExpires_in("0");
        token.setScope("Not Set");
        if(saveMMRauthorizationTokenForUser(userId, token)==1){
            return true;
        }else {
            return false;
        }     
        
    }
    
    public boolean addMkwsUser(String first_name, String last_name, String email, String uname, String password, boolean isAdmin){
        Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
        
            String query = "INSERT INTO mk.members (`first_name`,`last_name`,`email`,`uname`,`pass`,`isAdmin`,`regdate`) VALUES ("
                    + "\""+ first_name+"\""+", "
                    + "\""+last_name +"\""+", "
                    + "\""+email +"\""+ ", "
                    + "\""+uname +"\""+ ", "
                    +  "\""+password +"\""+ ", "
                    + isAdmin + ", NOW())";
            System.out.println(query);
            
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.execute(query);
            con_1.close();
        } catch (SQLException ex) {
            return false;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
    public boolean deleteUser(int userId){
       
        try {
            Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "DELETE FROM mk.members WHERE id = " + userId;
            st_1.execute(query);
            con_1.close();
            LogMessage msg = new LogMessage();
            msg.setDeviceType(DeviceTypes.SERVER.getName());
            msg.setLogLevel(2);
            msg.setLogMessage("User "+ userId + " is deleted.");
            Gson gson = new Gson();
            sendLog(gson.toJson(msg));
            
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    public MkwsUser editMkwsUser(int id, String first_name, String last_name, String email, String password,boolean isAdmin){
        
        try {
            Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "UPDATE mk.members SET ";
                    if (first_name!=null){
                        query = query + "first_name = " + first_name +", ";
                    }
                    if (last_name!=null){
                        query = query + "last_name = " + last_name +", ";
                    }
                    if (email!=null){
                        query = query + "email = " + email +", ";
                    }
                    if (password!=null){
                        query = query + "pass = " + password +", ";
                    }
                    query = query + "isAdmin = " + isAdmin; 
                    System.out.println(query);
            st_1.execute(query);
            con_1.close();
        } catch (SQLException ex) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return getUserInfoById(id);
    }
    
    public MkwsUser getUserInfoById(int id){
        
        MkwsUser user =  new MkwsUser();
        
        try {
            Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1=null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.members WHERE id= " + id;
            System.out.println(query);
            rs_1 = st_1.executeQuery(query);
           
            while (rs_1.next()){
            
            user.setId(rs_1.getInt("id"));
            user.setFirst_name(rs_1.getString("first_name"));
            user.setLast_name(rs_1.getString("last_name"));
            user.setUname(rs_1.getString("uname"));
            user.setPass(rs_1.getString("pass"));
            user.setRegdate(rs_1.getTimestamp("regdate"));
            user.setEmail(rs_1.getString("email"));
            user.setIsAdmin(rs_1.getBoolean("isAdmin"));
            }
            
             con_1.close();
            LogMessage msg = new LogMessage();
            
            msg.setDeviceType(DeviceTypes.SERVER.getName());
            msg.setLogLevel(2);
            msg.setLogMessage("User "+ id + " is edited.");
            Gson gson = new Gson();
            sendLog(gson.toJson(msg));
            
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        return user;
    }
    
}
