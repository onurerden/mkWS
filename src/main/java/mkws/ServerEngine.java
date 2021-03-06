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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import mkws.Model.FacebookUserModel;
import mkws.Model.FeedbackModel;
import mkws.Model.FollowMeDataModel;
import mkws.Model.MKMission;
import mkws.Model.MMRUser;
import mkws.Model.MMRWorkout;
import mkws.Model.MailTemplate;
import mkws.Model.MkwsUser;
import mkws.Model.OAuthToken;
import mkws.Model.RouteModel;
import mkws.Model.Waypoint;
import mkws.webbeans.GetRouteDetails;
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
import org.apache.logging.log4j.Level;
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

    private int userId = 1;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public ServerEngine() {
        // BasicConfigurator.configure();
    }

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
            //   System.out.println(device.getName());
        } catch (Exception ex) {
            System.out.println("DeviceType alınamadı.");
        }

        switch (device) {
            case MK: {
                queryString = "SELECT * from kopter where UID = '" + uid + "'";
                //  System.out.println("Case MK");
                break;
            }
            case MP: {
                queryString = "SELECT * from followmedevices where UID = '" + uid + "'";
                //  System.out.println("Case MP");
                break;
            }
        }

        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        try {

            con_1 = getConnection();
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

                        //        System.out.println("query device uid = " + kopter.uid + " device type mk");
                        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        Statement st_2 = con_1.createStatement();
                        st_2.executeUpdate("UPDATE kopter SET latestTouch = NOW() WHERE id = " + kopter.id);

                        deviceId = kopter.id;
                        sessionInfo.setDeviceId(kopter.id);
                    }
                    break;
                }
                case MP: {
                    // System.out.println("Case MP");
                    deviceId = -3;
                    sessionInfo.setDeviceId(-3);
                    if (rs_1.next()) {
                        MobilePhone phone = new MobilePhone();
                        phone.id = rs_1.getInt("id");
                        phone.name = rs_1.getString("name");
                        phone.uid = rs_1.getString("uid");
                        phone.registerDate = rs_1.getTimestamp("registerDate");

                        Statement st_2 = con_1.createStatement();
                        st_2.executeUpdate("UPDATE members SET latestTouch = NOW() WHERE id = " + this.userId);

                        // System.out.println("query device uid = " + phone.uid + " device type phone");
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
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex.getMessage());
        }
        sessionInfo.setSessionId(createSession(sessionInfo.getDeviceId(), sessionInfo.getDeviceType()));
        Gson json = new Gson();
        // System.out.println(json.toJson(sessionInfo).toString());
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
                    queryString = "INSERT INTO `followmedevices`(`UID`, `name`, `userId`, `registerDate`) VALUES ('" + uid + "', '" + name + "','" + userId + "', NOW())";
                    break;
                }
            }

            Connection con_1;
            Statement st_1;
            //ResultSet rs_1 = null;
            try {

                con_1 = getConnection();
                st_1 = con_1.createStatement();

                st_1.executeUpdate(queryString, Statement.RETURN_GENERATED_KEYS);
                deviceId = jsonObject.fromJson(touchServer(uid, deviceType), MKSession.class).getDeviceId();

                con_1.close();
            } catch (SQLException ex) {
                LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            }

        }
        return deviceId;
    }

    @Override
    public int sendStatus(String jsonStatus) {
        int result = -1;

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
        // System.out.println(queryString);
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            // System.out.println(queryString);
            result = -2;
            result = st_1.executeUpdate(queryString);
            con_1.close();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }
        return result;
    }

    @Override
    public String getTask(int deviceId) {
//        return "not implemented";

        String output = "-1";
        String queryString;

        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        int matchedDevice = getMatchedDevice(deviceId);
        switch (matchedDevice) {
            case -3: {
                try {

                    con_1 = getConnection();
                    st_1 = con_1.createStatement();

                    queryString = "SELECT * from waypoints "
                            + "WHERE missionId = (SELECT max(missionId) from koptermission WHERE kopterId = " + deviceId
                            + ")ORDER BY waypoints.id DESC";
                    //   System.out.println(queryString);
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

                } catch (SQLException ex) {
                    LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
                }

            }
            break;

            default: {
                try {

                    con_1 = getConnection();
                    st_1 = con_1.createStatement();

                    queryString = "SELECT * from followme WHERE followMeDeviceId = " + matchedDevice
                            + " AND sent = 0 "
                            + " AND followme.time > DATE_SUB(NOW(), INTERVAL 1 MINUTE)"
                            + " ORDER BY time DESC LIMIT 1";
                    //   System.out.println(queryString);
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
                } catch (SQLException ex) {
                    output = "-2";
                    LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
                }
                break;
            }
        }

        return output;
    }

    public ArrayList<Integer> getRoutesToBeProcessed() {
        ArrayList list = new ArrayList<>();

        Connection con_1;
        Statement st_1;
        String queryString;
        queryString = "SELECT * from mk.mabeyn WHERE status = 0 AND datatype = 1";
        ResultSet rs;
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            rs = st_1.executeQuery(queryString);

            while (rs.next()) {
                list.add(rs.getInt("id"));
            }

            con_1.close();
        } catch (SQLException ex) {
            return null;
        }

        return list;

    }

    public String readFromMabeyn(int mabeynId) {
        String jsonString = null;
        Connection con_1;
        Statement st_1;
        String queryString;
        queryString = "SELECT * from mk.mabeyn WHERE id = " + mabeynId;
        ResultSet rs;
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            rs = st_1.executeQuery(queryString);

            while (rs.next()) {
                jsonString = rs.getString("data");
                // System.out.println(jsonString);
            }

            con_1.close();
        } catch (SQLException ex) {
            return null;
        }
        return jsonString;

    }

    public void markRouteAsProcessed(int mabeynId) {
        Connection con;
        PreparedStatement st;
        String queryString = "UPDATE mk.mabeyn SET status = 1, dateProcessed = NOW() WHERE id = ?";
        try {
            con = getConnection();
            st = con.prepareStatement(queryString);

            st.setInt(1, mabeynId);
            st.executeUpdate();

        } catch (SQLException ex) {
            LogMessage msg = new LogMessage();
            msg.setLogLevel(1);
            msg.setLogMessage("Route in mabeyn cannot be marked as processed.");
            sendLog(msg);
        }
    }

    public void clearProcessedRoutes() {
        Connection con;
        PreparedStatement ps;
        String query = "DELETE FROM mk.mabeyn WHERE status = 1 AND dateProcessed < NOW() - INTERVAL 1 DAY";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.execute();
            if (ps.getUpdateCount() > 0) {
                LogMessage msg = new LogMessage();
                msg.setLogLevel(2);
                msg.setDeviceType(DeviceTypes.SERVER.getName());
                msg.setLogMessage(ps.getUpdateCount() + " item deleted from mabeyn database.");
                sendLog(msg);
            }

            con.close();
        } catch (SQLException ex) {

        }
    }

    public void markRouteAsFailed(int mabeynId) {
        Connection con;
        PreparedStatement st;
        String queryString = "UPDATE mk.mabeyn SET status = 2 WHERE id = ?";
        try {
            con = getConnection();
            st = con.prepareStatement(queryString);

            st.setInt(1, mabeynId);
            st.executeUpdate();

        } catch (SQLException ex) {
            LogMessage msg = new LogMessage();
            msg.setDeviceType("SERVER");
            msg.setLogLevel(2);
            msg.setLogMessage("Route in mabeyn cannot be marked as failed.");
            sendLog(msg);
        }
    }

    @Override
    public int sendFollowMeDataToMabeyn(String data, String ip, int userid) {

        Connection con_1;
        Statement st_1;
        String queryString;
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();

            queryString = "INSERT INTO mk.mabeyn "
                    + "(`data`, `datatype`, `status`, `ip`, `userid` ) "
                    + "VALUES ("
                    + "'" + data + "', "
                    + "" + 1 + ", "
                    + "" + 0 + ", "
                    + "'" + ip + "', "
                    + "" + userid + ") ";

            st_1.executeUpdate(queryString);
            con_1.close();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            System.out.println("Exception: " + ex.getMessage());
            return -1;
        }

        return 0;
    }

    @Override
    public int sendFollowMeData(String json) {
        FollowMeData data;
        FollowMeData[] dataArray;
        int result = -2;
        //  Gson gson = new Gson();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd' 'HH:mm:ss")
                .create();

        try {
            dataArray = gson.fromJson(json, FollowMeData[].class);
            int routeId=0;
            for (FollowMeData d : dataArray) {
                result = sendFollowMeData(d);
                routeId=d.getRouteId();
            }
            updateRouteNow(routeId);
            return result;
        } catch (JsonSyntaxException ex) {
            System.out.println("It is not FollowMeData Array: " + ex.toString());
        }

        try {
            data = gson.fromJson(json, FollowMeData.class);
            Runnable r = new FollowMeDataStorerThread(data);
            new Thread(r).start();

            return 0;
        } catch (JsonSyntaxException ex) {
            System.out.println("It is not FollowMeData: " + ex.toString());
        }

        return result;
    }

    public int sendFollowMeData(FollowMeData data) {

        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        String queryString;
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        try {

            con_1 = getConnection();
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

            //System.out.println(queryString);
            st_1.executeUpdate(queryString);
            con_1.close();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.WARN, "Exception at sendfollowmedata. " + ex.getMessage(), ex);
            System.out.println("Exception: " + ex.getMessage());
            return -1;
        }

        return 0;
    }

    private int getMatchedDevice(int deviceId) {
        int matchedFollowMeDevice = -1;
        boolean hasMission = false;

        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        String queryString = "SELECT followMeDeviceId, hasMission "
                + "from devicematching "
                + "where id=(SELECT max(id) from devicematching where kopterId=" + deviceId + ")";

        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();

            rs_1 = st_1.executeQuery(queryString);

            while (rs_1.next()) {
                matchedFollowMeDevice = rs_1.getInt(1);
                hasMission = rs_1.getBoolean("hasMission");

            }
            System.out.println("Follow followMeDevice: " + matchedFollowMeDevice);
            con_1.close();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
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

        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        String queryString = "SELECT * from kopterstatus WHERE kopterId = " + deviceId + " ORDER BY `kopterstatus`.`id` DESC LIMIT 1";
        try {

            con_1 = getConnection();
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
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            return "-1";
        }

        return kopterStatusString;

    }

    @Override
    public String getFollowMeData(int deviceId) {
        String jsonString = "";
        System.out.println("Looking for FollowMeData " + deviceId);
        FollowMeData data = new FollowMeData();

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

            con_1 = getConnection();
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
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, "Exception at getFollowMeData. " + ex.getMessage(), ex);
            return "-1";
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, "Exception at getFollowMeData. " + ex.getMessage(), ex);
            }
        }
        return jsonString;
    }

    @Override
    public int getRouteId(int deviceId, int type) {
        int routeId = -1;

        Connection con_1 = null;
        Statement st_1;
        String queryString = "INSERT INTO route SET followMeDeviceId = " + deviceId + " , time = NOW() , userId = " + userId
                + ", type = " + type;
        //Statement st_2;
        try {

            //System.out.println("Instance Created");
            con_1 = getConnection();
            //System.out.println("Connection Created");
            st_1 = con_1.createStatement();

            // System.out.println("StatementCreated:");
            //System.out.println(queryString);
            st_1.executeUpdate(queryString, Statement.RETURN_GENERATED_KEYS);
            //System.out.println("routeId for deviceId = " + deviceId + " is created.");

            //st_2 = con_1.createStatement();
            //queryString = "SELECT MAX(id) AS maxId from mk.route WHERE followMeDeviceId = " + deviceId;
            //System.out.println(queryString);
            //rs_1 = st_2.executeQuery(queryString);
            //  while (rs_1.next()) {
            //     routeId = rs_1.getInt("maxId");
            //     System.out.println("New routeId for deviceId = " + deviceId + " is " + routeId);
            //    }
            ResultSet rs = st_1.getGeneratedKeys();
            rs.next();
            routeId = rs.getInt(1);
            
            System.out.println("generated key is: " + routeId);

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
            return -1;

        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            }
        }

        return routeId;
    }

    public RouteModel getRouteInfo(int routeId) {

        Connection con_1 = null;
        Statement st_1;
        ResultSet rs_1;
        String queryString;
        RouteModel route = new RouteModel();
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.route WHERE id= " + routeId;
            System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                route.setRouteId(rs_1.getInt("id"));
                route.setRouteLength(rs_1.getDouble("length"));
                route.setIsDeleted(rs_1.getBoolean("isDeleted"));
                route.setIsEnded(rs_1.getBoolean("isEnded"));
                route.setRouteMeanSpeed(rs_1.getDouble("meanSpeed"));
                route.setTime(rs_1.getTimestamp("time"));
                route.setUserId(rs_1.getInt("userId"));
                route.setTitle(rs_1.getString("title"));
                route.setType(rs_1.getInt("type"));

            }

            con_1.close();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }
        return route;
    }

    @Override
    public int setTask(int kopterId, int followMeDeviceId) {

        Connection con_1 = null;
        Statement st_1;
        ResultSet rs_1;
        String queryString;
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            queryString = "INSERT INTO devicematching SET kopterId =" + kopterId + ", followMeDeviceId =" + followMeDeviceId;

            st_1.executeUpdate(queryString);

        } catch (SQLException ex) {

        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            }
        }

        return 1;
    }

    public int sendLog(LogMessage msg) {
        Connection con_1 = null;
        Statement st_1 = null;
        int status = -2;
        msg.setLogMessage(msg.getLogMessage().replace("'", "\\'"));
        String query = "INSERT INTO mk.logs SET logLevel = '" + msg.getLogLevel()
                + "', logMessage = '" + msg.getLogMessage() + "'";

        try {
            DeviceTypes deviceType = DeviceTypes.valueOf(msg.getDeviceType().toUpperCase());
            switch (deviceType) {
                case MK: {
                    query = query + ", " + "kopterId = '" + msg.getDeviceId() + "'";
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
            System.out.println("NullPointerException at sendLog. Possibly no deviceType declared. Exception:" + ex.getMessage());
        }

        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            // System.out.println(query);
            st_1.execute(query);
            status = 0;

        } catch (SQLException ex) {
            System.out.println("Exception while sendLog: " + ex.toString());
            status = -1;
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection on sendLog method: " + ex.toString());
            }
        }
        return status;
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

        Connection con_1 = null;
        Statement st_1 = null;

        msg.setLogMessage(msg.getLogMessage().replace("'", "\\'"));
        String query = "INSERT INTO mk.logs SET logLevel = '" + msg.getLogLevel()
                + "', logMessage = '" + msg.getLogMessage() + "'";

        try {
            DeviceTypes deviceType = DeviceTypes.valueOf(msg.getDeviceType().toUpperCase());
            switch (deviceType) {
                case MK: {
                    query = query + ", " + "kopterId = '" + msg.getDeviceId() + "'";
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
            System.out.println("NullPointerException at sendLog. Possibly no deviceType declared. Exception:" + ex.getMessage());
        }

        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            // System.out.println(query);
            st_1.execute(query);
            status = 0;

        } catch (SQLException ex) {
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

        Connection con_1 = null;
        Statement st_1 = null;
        GetRouteDetails routeDetails = getRouteDetailsWithBean(routeId);

        double length = routeDetails.getRouteLength();

        long duration = routeDetails.getTimeList().get(routeDetails.getTimeList().size() - 1).getTime() / 1000 - routeDetails.getTimeList().get(0).getTime() / 1000;
        double average = 0;
        if (duration != 0) {
            average = length * 1000.0 / duration;
        }

        String query = "UPDATE  `route` SET isEnded =1, "
                + "length = " + length + ", "
                + "meanSpeed= " + average + " "
                + "WHERE id =" + routeId;
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (SQLException ex) {
            System.out.println("End route failed." + ex.getMessage());
            return -1;
        }

        return 0;
    }

    @Override
    public int endRoute(int routeId, double length) {

        Connection con_1 = null;
        Statement st_1 = null;
        GetRouteDetails routeDetails = getRouteDetailsWithBean(routeId);

        if (length == -1) {
            length = routeDetails.getRouteLength();
        }

        long duration = routeDetails.getTimeList().get(routeDetails.getTimeList().size() - 1).getTime() / 1000 - routeDetails.getTimeList().get(0).getTime() / 1000;

        double average = length * 1000.0 / duration;
        String query = "UPDATE  `route` SET isEnded =1, "
                + "length = " + length + ", "
                + "meanSpeed= " + average + " "
                + "WHERE id =" + routeId;
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            return -1;
        }

        return 0;
    }

    @Override
    public int endRoute(int routeId, int duration) {

        Connection con_1 = null;
        Statement st_1 = null;
        GetRouteDetails routeDetails = getRouteDetailsWithBean(routeId);
        double length = routeDetails.getRouteLength();

        double average = length * 1000.0 / duration;
        String query = "UPDATE  `route` SET isEnded =1, length = " + length + ", meanSpeed= " + average + " , duration = " + duration + " WHERE id =" + routeId;
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            return -1;
        }

        return 0;
    }

    @Override
    public int endRoute(int routeId, int duration, double length) {

        Connection con_1 = null;
        Statement st_1 = null;
        GetRouteDetails routeDetails = getRouteDetailsWithBean(routeId);

        if (length == -1) {
            length = routeDetails.getRouteLength();
        }

        double average = length * 1000.0 / duration;
        String query = "UPDATE  `route` SET isEnded =1, "
                + "length = " + length + ", "
                + "meanSpeed= " + average + " , "
                + "duration = " + duration + " "
                + "WHERE id =" + routeId;
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            return -1;
        }

        return 0;
    }

    private int createSession(int deviceId, DeviceTypes type) {
        int sessionId = -1;

        Connection con_1 = null;
        Statement st_1;
        boolean isKopter = type.equals(DeviceTypes.MK);

        String query = "INSERT INTO mk.session SET deviceId= '" + deviceId + "',"
                + " deviceType = '" + type.getName().toString() + "',"
                + " isKopter = " + isKopter;
        //  System.out.println(query);

        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            try {
                con_1.close();
            } catch (SQLException ex) {

            }
        }
        query = "SELECT * from mk.session WHERE deviceId='" + deviceId + "'" + " AND deviceType = '" + type.getName().toString()
                + "' ORDER BY id DESC LIMIT 1";
        //   System.out.println(query);
        Connection con_2 = null;
        try {

            con_2 = getConnection();
            Statement st_2 = con_2.createStatement();
            ResultSet rs_2 = st_2.executeQuery(query);
            rs_2.next();
            sessionId = rs_2.getInt("id");
        } catch (SQLException ex) {
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

        Connection con_1 = null;
        Statement st_1;

        String query = "SELECT * from kopterstatus WHERE sessionId=" + sessionId;
        try {

            con_1 = getConnection();
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
            con_1.close();
        } catch (Exception ex) {
            System.out.println("Error at collecting session data");
        }

        return sessionList;

    }

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
            routeId = getRouteId(session.getDeviceId(), 1);

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
                        if (eElement.getElementsByTagName("ele").item(0) != null) {
                            fmData.setAltitude(Double.valueOf(eElement.getElementsByTagName("ele").item(0).getTextContent()));
                        }
                        if (eElement.getElementsByTagName("speed").item(0) != null) {
                            fmData.setSpeed(Double.valueOf(eElement.getElementsByTagName("speed").item(0).getTextContent()));
                        }
                        fmData.setRouteId(routeId);
                        fmData.setSessionId(session.getSessionId());
                        fmData.setFollowMeDeviceId(session.getDeviceId());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String date = eElement.getElementsByTagName("time").item(0).getTextContent().replace("T", " ").replace("Z", "");
                        Date parsedDate = dateFormat.parse(date);
                        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

                        fmData.setTime(timestamp);

                        System.out.println("Latitude: " + fmData.getLat());
                        System.out.println("Longitude: " + fmData.getLat());
                        System.out.println("Elevation: " + fmData.getAltitude());
                        System.out.println("Speed: " + fmData.getSpeed());
                        sendFollowMeData(fmData);
                    } catch (NumberFormatException | DOMException | ParseException ex) {
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
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
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

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error while saving MMR Authorization code: " + ex.getMessage());
            return -1;
        }
        return 1;
    }

    public int saveMMRauthorizationCodeForUser(Integer userId, String code) {

        Connection con_1 = null;
        Statement st_1;
        String query;

        query = "INSERT INTO mk.mmrauthorization (code,userId ) VALUES (\""
                + code + "\","
                + userId + ")";

        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
        } catch (SQLException ex) {
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

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error while saving MMR Authorization code: " + ex.getMessage());
            return -1;
        }
        return 1;
    }

    public int saveMMRauthorizationTokenForUser(int userId, OAuthToken token) {

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

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error while saving MMR Authorization code: " + ex.getMessage());
            return -1;
        }
        return 1;
    }

    public String mmrAccessTokenForUser(int userId) throws Exception {
        OAuthToken token = new OAuthToken();
        Credentials cr = new Credentials();
        token.setClientId(cr.getMmrClientIdForWeb());
        token.setClientSecret(cr.getMmrClientSecretForWeb());
        String queryString = "SELECT code, access_token, refresh_token, scope, expires_in FROM mk.mmrauthorization WHERE userId = " + userId + " ORDER BY id DESC LIMIT 1";
        Connection con_1 = null;
        Statement st_1 = null;
        try {

            con_1 = getConnection();
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
        } catch (SQLException ex) {
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

            con_1 = getConnection();
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
        } catch (SQLException ex) {
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
            //HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/user/self/");
            HttpGet httpget = new HttpGet("https://api.ua.com/v7.1/user/self/");

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

    public String getMMRUserProfilePicture(int mmrUserId, int userId) {
        Credentials cr = new Credentials();
        String responseString = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            //HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/activity_type/");
            //HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/user_profile_photo/" + mmrUserId + "/");
            HttpGet httpget = new HttpGet("https://api.ua.com/v7.1/user_profile_photo/" + mmrUserId + "/");

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
        String responseString = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            //HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/activity_type/");
            //HttpGet httpget = new HttpGet("https://oauth2-api.mapmyapi.com/v7.1/user/self/");
            HttpGet httpget = new HttpGet("https://api.ua.com/v7.1/user/self/");

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
            user.setPhotoLinks(gson.fromJson(getMMRUserProfilePicture(user.getId(), userId), MMRUser.MMRUserProfilePhoto.class));

            System.out.println("MMR User name is " + user.getDisplay_name());

            if (responseString.contains("error")) {
                System.out.println("Error while getting MMR user info: " + responseString);
                return "Error while getting MMR user info: " + responseString;
            }
            //System.out.println(responseString);
            gson2 = new Gson();
            responseString = gson2.toJson(user);
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

            //HttpPost httppost = new HttpPost("https://oauth2-api.mapmyapi.com/v7.1/workout/");
            HttpPost httppost = new HttpPost("https://api.ua.com/v7.1/workout/");
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

    public int sendMMRWorkout(int routeId, int userId) {

        MMRWorkout workout = new MMRWorkout();
        workout.populateWorkout(routeId);
        //MMRUser user = new Gson().fromJson(getMMRUserInfo(workout.getDeviceId(), "MP"), MMRUser.class);
        Credentials cr = new Credentials();
        int result = 0;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            // HttpPost httppost = new HttpPost("https://oauth2-api.mapmyapi.com/v7.1/workout/");
            HttpPost httppost = new HttpPost("https://api.ua.com/v7.1/workout/");
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

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.execute(query);
            con_1.close();
        } catch (SQLException ex) {
            return -1;
        }

        return 1;
    }

    @Override
    public String getMission(int kopterId) {

        return new String();
    }

    private void setMissionSent(int missionId) {
        try {

            Connection con_1 = null;
            Statement st_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "UPDATE koptermission SET isSent = 1 WHERE missionId = " + missionId;
            st_1.execute(query);
            con_1.close();
        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }

    }

    public boolean deleteRoute(int routeId) {

        try {

            Connection con_1 = null;
            Statement st_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "UPDATE route SET isDeleted = TRUE WHERE id = " + routeId;
            st_1.execute(query);
            con_1.close();
            LogMessage msg = new LogMessage();
            msg.setDeviceType(DeviceTypes.SERVER.getName());
            msg.setLogLevel(2);
            msg.setLogMessage("Route " + routeId + " is deleted.");
            Gson gson = new Gson();
            sendLog(gson.toJson(msg));

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            return false;
        }

        return true;
    }

    public boolean editRoute(RouteModel route) {

        try {

            Connection con_1 = null;
            Statement st_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "UPDATE route SET time = "
                    + "\"" + route.getTime() + "\" , "
                    + "length = " + route.getRouteLength() + ", "
                    + "title = \"" + route.getTitle()+ "\", "
                    + "duration = " + route.getDuration() + ", "
                    + "type = " + route.getType() + ", "
                    + "isEnded = TRUE "
                    + "WHERE id = " + route.getRouteId();
            st_1.execute(query);
            con_1.close();
            LogMessage msg = new LogMessage();
            msg.setDeviceType(DeviceTypes.SERVER.getName());
            msg.setLogLevel(2);
            msg.setLogMessage("Route " + route.getRouteId() + " is edited.");
            Gson gson = new Gson();
            sendLog(gson.toJson(msg));
        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
            return false;
        }
        return true;
    }

    private boolean isMissionSent(int missionId) {
        boolean isSent = false;

        Connection con_1;
        Statement st_1;
        ResultSet rs_1;
        String queryString = "SELECT isSent from koptermission WHERE missionId = " + missionId;

        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();

            rs_1 = st_1.executeQuery(queryString);
            while (rs_1.next()) {
                isSent = rs_1.getBoolean("isSent");
            }
        } catch (SQLException ex) {

        }
        return isSent;
    }

    public boolean deleteMapMyRideToken(int userId) {
        OAuthToken token = new OAuthToken();
        token.setAuthorizationCode("Not Set");
        token.setClientSecret("Not Set");
        token.setRefresh_token("Not Set");
        token.setExpires_in("0");
        token.setScope("Not Set");
        if (saveMMRauthorizationTokenForUser(userId, token) == 1) {
            return true;
        } else {
            return false;
        }

    }

    public boolean addMkwsUser(String first_name, String last_name, String email, String uname, String password, boolean isAdmin, boolean isActivated) {
        if (first_name.isEmpty() || last_name.isEmpty() || email.isEmpty() || uname.isEmpty() || password.isEmpty()) {
            return false;
        }

        Connection con_1 = null;
        Statement st_1 = null;

        String query = "INSERT INTO mk.members (`first_name`,`last_name`,`email`,`uname`,`pass`,`isAdmin`,`isActivated`,`regdate`) VALUES ("
                + "\"" + first_name + "\"" + ", "
                + "\"" + last_name + "\"" + ", "
                + "\"" + email + "\"" + ", "
                + "\"" + uname + "\"" + ", "
                + "MD5(\"" + password + "\")" + ", "
                + isAdmin + ", " + isActivated + ", NOW())";
        System.out.println(query);

        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.execute(query);
            con_1.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    public boolean deleteUser(int userId) {

        try {

            Connection con_1 = null;
            Statement st_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "DELETE FROM mk.members WHERE id = " + userId;
            st_1.execute(query);
            con_1.close();
            LogMessage msg = new LogMessage();
            msg.setDeviceType(DeviceTypes.SERVER.getName());
            msg.setLogLevel(2);
            msg.setLogMessage("User " + userId + " is deleted.");
            Gson gson = new Gson();
            sendLog(gson.toJson(msg));

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }

        return true;
    }

    public MkwsUser editMkwsUser(int id, String first_name, String last_name, String email, String password, boolean isAdmin) {

        try {

            Connection con_1 = null;
            Statement st_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "UPDATE mk.members SET ";
            if (first_name != null) {
                query = query + "first_name = \"" + first_name + "\", ";
            }
            if (last_name != null) {
                query = query + "last_name = \"" + last_name + "\", ";
            }
            if (email != null) {
                query = query + "email = \"" + email + "\", ";
            }
            if (password != null) {
                query = query + "pass = \"" + password + "\", ";
            }
            query = query + "isAdmin = " + isAdmin;
            query = query + " WHERE id = " + id;

            System.out.println(query);
            st_1.execute(query);
            con_1.close();
        } catch (SQLException ex) {
        }

        return getUserInfoById(id);
    }

    public MkwsUser getUserInfoById(int id) {

        MkwsUser user = new MkwsUser();

        try {

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.members WHERE id= " + id;
            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {

                user.setId(rs_1.getInt("id"));
                user.setFirst_name(rs_1.getString("first_name"));
                user.setLast_name(rs_1.getString("last_name"));
                user.setUname(rs_1.getString("uname"));
                user.setPass("__MASKED__");
                user.setRegdate(rs_1.getTimestamp("regdate"));
                user.setEmail(rs_1.getString("email"));
                user.setIsAdmin(rs_1.getBoolean("isAdmin"));
                user.setIsActivated(rs_1.getBoolean("isActivated"));
            }

            con_1.close();
//         LogMessage msg = new LogMessage();

//            msg.setDeviceType(DeviceTypes.SERVER.getName());
//            msg.setLogLevel(2);
//            msg.setLogMessage("User " + id + " is edited.");
//            Gson gson = new Gson();
//            sendLog(gson.toJson(msg));
//            
        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }

        return user;
    }

    public MkwsUser getUserByCredentials(String userName, String password) {
        MkwsUser user = new MkwsUser();

        try {

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.members WHERE uname=\"" + userName + "\" AND pass= MD5(\"" + password + "\")";
            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {

                user.setId(rs_1.getInt("id"));
                user.setFirst_name(rs_1.getString("first_name"));
                user.setLast_name(rs_1.getString("last_name"));
                user.setUname(rs_1.getString("uname"));
                user.setPass(rs_1.getString("pass"));
                user.setRegdate(rs_1.getTimestamp("regdate"));
                user.setEmail(rs_1.getString("email"));
                user.setIsAdmin(rs_1.getBoolean("isAdmin"));
                user.setIsActivated(rs_1.getBoolean("isActivated"));
            }

            con_1.close();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }

        if (user.getUname() == null) {
            return null;
        }
        return user;
    }

    public MkwsUser getUserByEmail(String email) {
        MkwsUser user = new MkwsUser();

        try {

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.members WHERE email=\"" + email + "\"";
            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                System.out.println("User found:");
                user.setId(rs_1.getInt("id"));
                user.setFirst_name(rs_1.getString("first_name"));
                user.setLast_name(rs_1.getString("last_name"));
                user.setUname(rs_1.getString("uname"));
                user.setPass(rs_1.getString("pass"));
                user.setRegdate(rs_1.getTimestamp("regdate"));
                user.setEmail(rs_1.getString("email"));
                user.setIsAdmin(rs_1.getBoolean("isAdmin"));
                user.setIsActivated(rs_1.getBoolean("isActivated"));
            }

            con_1.close();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }

        if (user.getUname() == null) {
            return null;
        }
        return user;
    }

    public String createTokenForUser(int userId) {
        Credentials cr = new Credentials();

        String jwtStr = Jwts.builder()
                .setSubject("user")
                .setIssuer("mkws")
                .claim("userId", userId)
                .claim("isAdmin", getUserInfoById(userId).isIsAdmin())
                .claim("uname", getUserInfoById(userId).getUname())
                .claim("isActivated", getUserInfoById(userId).isIsActivated())
                .signWith(
                        SignatureAlgorithm.HS256,
                        TextCodec.BASE64.decode(
                                // This generated signing key is
                                // the proper length for the
                                // HS256 algorithm.
                                cr.getJjwtKey()
                        )
                )
                .compact();
        return jwtStr;
    }

    public ArrayList getRoutes(int lowerThan) {
        ArrayList<RouteModel> list = new ArrayList<>();

        try {

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.route WHERE isDeleted=false AND userId=" + this.userId;
            if (lowerThan != 0) {
                query = query + " AND id<" + lowerThan;
            }
            query = query + " ORDER BY id DESC LIMIT 10";

            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                RouteModel model = new RouteModel();
                model.setRouteId(rs_1.getInt("id"));
                model.setIsEnded(rs_1.getBoolean("isEnded"));
                model.setIsDeleted(rs_1.getBoolean("isDeleted"));
                model.setFollowMeDeviceId(rs_1.getInt("followMeDeviceId"));
                model.setRouteLength(rs_1.getDouble("length"));
                model.setTime(rs_1.getTimestamp("time"));
                model.setUserId(rs_1.getInt("userId"));
                model.setRouteMeanSpeed(rs_1.getDouble("meanSpeed"));
                model.setDuration(rs_1.getInt("duration"));
                model.setUpdateTime(rs_1.getTimestamp("updateTime"));
                model.setTitle(rs_1.getString("title"));
                model.setType(rs_1.getInt("type"));
                //model.setSessionId(rs_1.getInt("sessionId"));

                list.add(model);

            }

        } catch (Exception ex) {

        }

        return list;

    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    //getRouteDetailsWithBean followMe dataları üzerinden hesap yapar, 
    //yavaş çalışır. hızlı cevap için getRouteDetails kullanılmalıdır.
    public GetRouteDetails getRouteDetailsWithBean(int routeId) {
        GetRouteDetails route = new GetRouteDetails();
        route.setRouteId(routeId);
        return route;
    }

    public RouteModel getRouteDetails(int routeId, boolean isAdmin) {
        RouteModel route = new RouteModel();

        try {

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.route WHERE isDeleted=false AND id=" + routeId;
            if (!isAdmin) {
                query = query + " AND userId=" + this.userId;
            }
            query = query + " LIMIT 1";

            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {

                route.setRouteId(rs_1.getInt("id"));
                route.setIsEnded(rs_1.getBoolean("isEnded"));
                route.setIsDeleted(rs_1.getBoolean("isDeleted"));
                route.setFollowMeDeviceId(rs_1.getInt("followMeDeviceId"));
                route.setRouteLength(rs_1.getDouble("length"));
                route.setTime(rs_1.getTimestamp("time"));
                route.setUserId(rs_1.getInt("userId"));
                route.setRouteMeanSpeed(rs_1.getDouble("meanSpeed"));
                route.setDuration(rs_1.getInt("duration"));
                route.setType(rs_1.getInt("type"));
                route.setTitle(rs_1.getString("title"));
                route.setUpdateTime(rs_1.getTimestamp("updateTime"));

            }
            con_1.close();

            con_1 = null;
            st_1 = null;
            rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            query = "SELECT * FROM mk.followme WHERE routeId=" + routeId
                    + " ORDER BY time ASC";
            rs_1 = st_1.executeQuery(query);
            ArrayList followMeData = new ArrayList();
            while (rs_1.next()) {
                FollowMeDataModel fm = new FollowMeDataModel();
                fm.setLat(rs_1.getDouble("latitude"));
                fm.setLng(rs_1.getDouble("longitude"));
                fm.setBearing(rs_1.getInt("bearing"));
                fm.setEvent(rs_1.getInt("event"));
                fm.setTime(rs_1.getTimestamp("time"));
                fm.setFollowMeDeviceId(rs_1.getInt("followMeDeviceId"));
                fm.setRouteId(rs_1.getInt("routeId"));
                fm.setSessionId(rs_1.getInt("sessionId"));
                fm.setSpeed(rs_1.getDouble("speed"));
                fm.setAltitude(rs_1.getDouble("altitude"));

                followMeData.add(fm);
            }
            route.setFollowMeData(followMeData);

        } catch (SQLException ex) {
            System.out.println("Error while getting route details: " + ex.getLocalizedMessage());
            return null;
        }
        return route;
    }

    public boolean activateUser(int userId) {

        Connection con_1 = null;
        Statement st_1 = null;

        String query = "UPDATE  `members` SET isActivated =1 WHERE id = " + userId;
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (SQLException ex) {
            LogMessage message = new LogMessage();
            message.setLogLevel(1);
            message.setDeviceType(DeviceTypes.SERVER.getName());
            message.setLogMessage("User cannot be activated. " + ex.getLocalizedMessage());
            Gson gson = new Gson();
            sendLog(gson.toJson(message));
            return false;
        }
        return true;
    }

    public String createTokenForActivation(int userId) {
        Credentials cr = new Credentials();

        String jwtStr = Jwts.builder()
                .setSubject("activation")
                .setIssuer("mkws")
                .claim("userId", userId)
                .signWith(
                        SignatureAlgorithm.HS256,
                        TextCodec.BASE64.decode(
                                // This generated signing key is
                                // the proper length for the
                                // HS256 algorithm.
                                cr.getJjwtKey()
                        )
                )
                .compact();
        return jwtStr;
    }

    public MailTemplate getMailTemplate(int templateId) {
        MailTemplate template = new MailTemplate();
        try {

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.mailtemplates WHERE id=" + templateId;
            rs_1 = st_1.executeQuery(query);
            while (rs_1.next()) {
                template.setId(rs_1.getInt("id"));
                template.setVersion(rs_1.getInt("version"));
                template.setText(rs_1.getString("text"));
                template.setDescription(rs_1.getString("description"));
                template.setDate(rs_1.getTimestamp("date"));
                template.setSubject(rs_1.getString("subject"));
            }

        } catch (SQLException ex) {

        }
        return template;

    }

    public boolean resetPassword(int userId) {
        String newPassword = randomString(8);
        changeUserPasswordTo(userId, newPassword);
        MkwsUser user = getUserInfoById(userId);

        MailTemplate mail = getMailTemplate(3);
        mail.setText(mail.getText().replaceAll("<%userName%>", user.getFirst_name() + " " + user.getLast_name()));
        mail.setText(mail.getText().replaceAll("<%newPassword%>", newPassword));
        MailSender sender = new MailSender();
        sender.sendMail(user.getEmail(), mail.getText(), mail.getSubject());

        return true;
    }

    public boolean sendResetPasswordEmail(String email) {
        MkwsUser user = getUserByEmail(email);
        if (user == null) {
            LogMessage message = new LogMessage();
            message.setDeviceType(DeviceTypes.SERVER.getName());
            message.setLogLevel(2);
            message.setLogMessage("There is no user with email: " + email);
            sendLog(message);
            return true;
        }
        MailTemplate mail = getMailTemplate(2);
        MailSender sender = new MailSender();
        String rstmail = mail.getText();
        rstmail = rstmail.replaceAll("<%userName%>", user.getFirst_name() + " " + user.getLast_name());
        rstmail = rstmail.replaceAll("<%resetPasswordToken%>", createTokenForResetPassword(user.getId()));
        sender.sendMail(email, rstmail, mail.getSubject());

        return true;
    }

    public String createTokenForResetPassword(int userId) {
        String token = "";
        Credentials cr = new Credentials();

        Date date = new Date(System.currentTimeMillis() + 259200);
        token = Jwts.builder()
                .setSubject("passwordReset")
                .setIssuer("mkws")
                .claim("userId", userId)
                .signWith(
                        SignatureAlgorithm.HS256,
                        TextCodec.BASE64.decode(
                                // This generated signing key is
                                // the proper length for the
                                // HS256 algorithm.
                                cr.getJjwtKey()
                        )
                )
                .setExpiration(date)
                .compact();

        return token;

    }

    public boolean changeUserPasswordTo(int userId, String newPassword) {

        Connection con_1 = null;
        Statement st_1 = null;

        String query = "UPDATE  `members` SET pass =MD5(\"" + newPassword + "\") WHERE id = " + userId;
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (SQLException ex) {
            LogMessage message = new LogMessage();
            message.setLogLevel(1);
            message.setDeviceType(DeviceTypes.SERVER.getName());
            message.setLogMessage("User password cannot be updated. " + ex.getLocalizedMessage());
            Gson gson = new Gson();
            sendLog(gson.toJson(message));
            return false;
        }
        return true;
    }

    public FacebookUserModel getUserInfoFromFacebook(String access_token, String fields) {
        FacebookUserModel fbUser = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            HttpGet httpget = new HttpGet("https://graph.facebook.com/me?fields=" + fields + "&access_token=" + access_token);

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity resEntity = response.getEntity();
            String responseString = EntityUtils.toString(resEntity, "UTF-8");

            Gson gson = new Gson();
            fbUser = gson.fromJson(responseString, FacebookUserModel.class);

        } catch (IOException ex) {

        }
        return fbUser;
    }

    Connection getConnection() {
        Connection con_1 = null;
        try {

            Credentials cr = new Credentials();
            Class.forName("com.mysql.jdbc.Driver");
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
        } catch (SQLException | ClassNotFoundException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }
        return con_1;
    }

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public boolean sendFeedback(String feedbackMessage) {

        Connection con_1 = null;
        Statement st_1 = null;

        String query = "INSERT INTO `feedback` (`memberId`, `message`) VALUES (" + this.userId + ", \"" + feedbackMessage + "\")";
        try {

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (SQLException ex) {
            LogMessage message = new LogMessage();
            message.setLogLevel(1);
            message.setDeviceType(DeviceTypes.SERVER.getName());
            message.setLogMessage("Feedback cannot be saved. " + ex.getLocalizedMessage());
            Gson gson = new Gson();
            sendLog(gson.toJson(message));
            return false;
        }
        return true;
    }

    public ArrayList getFeedbacks() {
        ArrayList<FeedbackModel> list = new ArrayList<>();

        try {

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM mk.v_feedback";

            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                FeedbackModel model = new FeedbackModel();
                model.setFeedbackId(rs_1.getInt("id"));
                model.setUserId(rs_1.getInt("memberId"));
                model.setIsRead(rs_1.getBoolean("isRead"));
                model.setUserName(rs_1.getString("uname"));
                model.setMessage(rs_1.getString("message"));
                model.setTime(rs_1.getTimestamp("time"));
                model.setPlatformId(rs_1.getInt("platformId"));

                list.add(model);

            }

        } catch (SQLException ex) {

        }

        return list;
    }

    public void clearifyNotEndedRoutes() {
        String routesQuery = "select route.id,route.time from route "
                + "Inner JOIN followme "
                + "ON route.id =followme.routeId "
                + "WHERE route.isEnded=false AND route.isDeleted=false AND (select COUNT(*) from followme where followme.routeId=route.id AND followme.time > DATE_SUB(NOW(), INTERVAL 1 HOUR))=0 "
                + "GROUP BY route.id";
        ArrayList<RouteModel> routeList = new ArrayList<>();

        try {
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();

            //System.out.println(query);
            rs_1 = st_1.executeQuery(routesQuery);

            while (rs_1.next()) {
                RouteModel model;
                model = getRouteInfo(rs_1.getInt("id"));
                routeList.add(model);

            }
            con_1.close();

        } catch (SQLException ex) {
            System.out.println("clearifyNotEndedRoutes sql sorgusu yapılamadı." + ex.getMessage());
        }

        for (RouteModel route : routeList) {
            endRoute(route.getRouteId());
            System.out.println("Route " + route.getRouteId() + " is ended by hourly job.");
            LogMessage msg = new LogMessage();
            msg.setDeviceType(DeviceTypes.SERVER.getName());
            msg.setLogLevel(2);
            msg.setLogMessage("Route " + route.getRouteId() + "is ended by hourly job.");
            sendLog(msg);

        }

    }

    public Integer getUsersLastRouteId() {
        int routeId = 0;
        try {

            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;

            con_1 = getConnection();
            st_1 = con_1.createStatement();
            String query = "SELECT routeId FROM mk.v_routes WHERE userId=" + userId + " ORDER BY routeId DESC LIMIT 1";
            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                routeId = rs_1.getInt("routeId");

            }

            con_1.close();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.INFO, ex);
        }

        return routeId;
    }

    public int saveUserProfilePhoto(InputStream fileContent, int userId, String fileType) {
        Connection con = getConnection();
        String updateSQL = "INSERT INTO userprofilepictures "
                + "(userId,picture,filetype) "
                + "VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(updateSQL);
            pstmt.setInt(1, userId);
            pstmt.setBlob(2, fileContent);
            pstmt.setString(3, fileType);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.WARN, ex);
            return -1;
        }
        return 0;
    }

    private void updateRouteNow(int routeId) {
 Connection con = getConnection();
        String updateSQL = "UPDATE mk.route SET updateTime = NOW() WHERE id=?";
        
        try {
            PreparedStatement pstmt = con.prepareStatement(updateSQL);
            pstmt.setInt(1, routeId);
          
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            LogManager.getLogger(ServerEngine.class.getName()).log(Level.WARN, ex);
            return;
        }
            
    }

    class FollowMeDataStorerThread implements Runnable {

        private FollowMeData data;

        public FollowMeDataStorerThread(FollowMeData data) {
            this.data = data;

        }

        public void run() {
            ServerEngine server = new ServerEngine();
            server.sendFollowMeData(data);
        }
    }
}