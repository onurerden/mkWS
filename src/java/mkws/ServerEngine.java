/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tomcat.jni.Time;

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
            Connection con_1 = null;
            Statement st_1 = null;
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
            status.kopterVario = status.getKopterVario() - 65536;
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

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String getTask(int deviceId) {
//        return "not implemented";

        String output = "-1";
        String queryString = "";

        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();

            queryString = "SELECT * from followme WHERE followMeDeviceId = " + getMatchedDevice(deviceId)
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
        } catch (SQLException ex) {
            output = "-2";
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            output = "-2";
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            output = "-2";
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            output = "-2";
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return output;
    }

    @Override
    public int sendFollowMeData(String json) {
        FollowMeData data = new FollowMeData();
        Gson gson = new Gson();

        try {
            data = gson.fromJson(json, FollowMeData.class);
        } catch (Exception ex) {
            System.out.println("FollowMeData parse error: " + ex.toString());
            return -2;
        }

        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        String queryString = "";
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();

            queryString = "INSERT INTO followme "
                    + "(`latitude`, `longitude`, `bearing`, `event`, `time`, `followMeDeviceId`, `sent`,`routeId`,`sessionId` ) "
                    + "VALUES ("
                    + "'" + data.getLat() + "', "
                    + "'" + data.getLon() + "', "
                    + "'" + data.getBearing() + "', "
                    + "'" + data.getEvent() + "', "
                    + "NOW(), "
                    + "'" + data.getFollowMeDeviceId() + "', "
                    + "'0', "
                    + "'" + data.getRouteId() + "', "
                    + "'" + data.getSessionId() + "')";

            System.out.println(queryString);
            st_1.executeUpdate(queryString);
            con_1.close();

        } catch (SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    private int getMatchedDevice(int deviceId) {
        int matchedFollowMeDevice = -1;

        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        String queryString = "SELECT followMeDeviceId "
                + "from devicematching "
                + "where id=(SELECT max(id) from devicematching where kopterId=" + deviceId +")";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();

            rs_1 = st_1.executeQuery(queryString);

            while (rs_1.next()) {
                matchedFollowMeDevice = rs_1.getInt(1);
            }
            System.out.println("Follow followMeDevice: " + matchedFollowMeDevice);
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

        return matchedFollowMeDevice;
    }

    @Override
    public String getKopterStatus(int deviceId) {
        System.out.println("Looking for KopterStatus " + deviceId);
        String kopterStatusString = "-2";
        KopterStatus status = new KopterStatus();

        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        String queryString = "SELECT * from kopterstatus WHERE kopterId = " + deviceId + " ORDER BY `kopterstatus`.`id` DESC LIMIT 1";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            System.out.println(queryString);
            rs_1 = st_1.executeQuery(queryString);
            System.out.println("KopterStatus found for " + deviceId);

            if (rs_1.next()) {
                status.kopterId = rs_1.getInt("kopterId");
                status.kopterAltitude = rs_1.getInt("altitude");
                status.kopterLatitude = rs_1.getDouble("latitude");
                status.kopterLongitude = rs_1.getDouble("longitude");
                status.kopterHeading = rs_1.getInt("heading");
                status.kopterErrorCode = rs_1.getInt("kopterErrorCode");
                status.gsmSignalStrength = rs_1.getInt("gsmSignalStrength");
                status.kopterVoltage = rs_1.getInt("kopterVoltage");
                status.gpsSatCount = rs_1.getInt("gpsSatCount");
                status.batteryCurrent = rs_1.getInt("batteryCurrent");
                status.batteryCapacity = rs_1.getInt("batteryCapacity");
                status.kopterSpeed = rs_1.getDouble("kopterSpeed");
                status.kopterRcSignal = rs_1.getInt("kopterRcSignal");
                status.kopterVario = rs_1.getInt("kopterVario");
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
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
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
        Statement st_1 = null;
        ResultSet rs_1 = null;
        String queryString = "";
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
        Statement st_1 = null;
        ResultSet rs_1 = null;
        String queryString = "INSERT INTO route SET followMeDeviceId = " + deviceId + " , time = NOW()";
        Statement st_2 = null;
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
                System.out.println("New routeId for deviceId = " + deviceId + "is " + routeId);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        } catch (IllegalAccessException ex) {
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
        Statement st_1 = null;
        ResultSet rs_1 = null;
        String queryString = "";
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
        String query = "INSERT INTO mk.logs SET logLevel = '" + msg.logLevel 
                + "', logMessage = '" + msg.logMessage + "', ";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();

            DeviceTypes deviceType = DeviceTypes.valueOf(msg.deviceType.toUpperCase());
            switch (deviceType) {
                case MK: {
                    query = query + "kopterId = '" + msg.deviceId + "'";
                    break;
                }
                case MP: {
                    query = query + "followMeDeviceId = '" + msg.deviceId + "'";
                    break;
                }
                default: {
                    query = query + "";
                    break;}
                }
                System.out.println(query);
                st_1.execute(query);
                status = 0;
            

        } catch (ClassNotFoundException ex) {

        } catch (IllegalAccessException ex) {
            status = -1;
        } catch (InstantiationException ex) {
            status = -1;
        } catch (SQLException ex) {
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
        Connection con_1=null;
        Statement st_1=null;
        
        String query ="UPDATE  `route` SET isEnded =1 WHERE id =" + routeId;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            st_1.executeUpdate(query);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
        return 0;
    }

    private int createSession(int deviceId, DeviceTypes type) {
        int sessionId = -1;
        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        String query = "INSERT INTO mk.session SET deviceId= '" + deviceId + "', deviceType = '" + type.getName().toString() + "'";
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
        con_1 = null;
        st_1 = null;
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
}
