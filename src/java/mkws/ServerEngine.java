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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oerden
 */
public class ServerEngine implements IDeviceServer {

    @Override
    public int touchServer(String uid, String deviceType) {
        int deviceId = -1;
        String queryString = "";

        Device device = Device.other;
        try {
            device = Device.valueOf(deviceType.toLowerCase());
        } catch (Exception ex) {

        }

        switch (device) {
            case mk: {
                queryString = "SELECT * from kopter where UID = '" + uid + "'";
                break;
            }
            case mp: {
                queryString = "SELECT * from followmedevices where UID = '" + uid + "'";
                break;
            }
        }

        Credentials cr = new Credentials();
        Connection con_1 = null;
        Statement st_1 = null;
        ResultSet rs_1 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
            st_1 = con_1.createStatement();

            rs_1 = st_1.executeQuery(queryString);

            switch (device) {
                case mk: {
                    deviceId = -2;
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
                    }
                    break;
                }
                case mp: {
                    deviceId = -3;
                    if (rs_1.next()) {
                        MobilePhone phone = new MobilePhone();
                        phone.id = rs_1.getInt("id");
                        phone.name = rs_1.getString("name");
                        phone.uid = rs_1.getString("uid");
                        phone.registerDate = rs_1.getTimestamp("registerDate");

                        System.out.println("query device uid = " + phone.uid + " device type phone");

                        deviceId = phone.id;
                    }
                    break;
                }
                default: {
                    deviceId = -4;
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

        return deviceId;
    }

    @Override
    public int registerDevice(String uid, String name, String deviceType) {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int deviceId = -1;
        deviceId = touchServer(uid, deviceType);

        if ((deviceId == -2) || (deviceId == -3)) {
            String queryString = "";

            Device device = Device.other;
            try {
                device = Device.valueOf(deviceType.toLowerCase());
            } catch (Exception ex) {
                System.out.println("Device Enumeration cannot be done.");
            }
            //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            switch (device) {
                case mk: {
                    queryString = "INSERT INTO `kopter`(`UID`, `name`, `active`) VALUES ('" + uid + "', '" + name + "', 1)";
                    break;
                }
                case mp: {
                    queryString = "INSERT INTO `followmedevices`(`UID`, `name`, `registerDate`) VALUES ('" + uid + "', '" + name + "', NOW())";;
                    break;
                }
            }

            Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
                st_1 = con_1.createStatement();

                st_1.executeUpdate(queryString, Statement.RETURN_GENERATED_KEYS);
                deviceId = touchServer(uid, deviceType);

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

        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        queryString = "INSERT INTO `kopterstatus`(`kopterId`, `altitude`,"
                + "`latitude`, `longitude`, `heading`, `kopterErrorCode`, `gsmSignalStrength`, "
                + "`kopterVoltage`, `gpsSatCount`, `batteryCurrent`, `batteryCapacity`, "
                + "`kopterSpeed`, `kopterRcSignal`, `kopterVario`, `ncFlags`, `fcFlags1`,"
                + "`fcFlags2`, `updateTime`) VALUES ("
                + status.kopterId + ", "
                + status.kopterAltitude + ", "
                + status.kopterLatitude + ", "
                + status.kopterLongitude + ", "
                + status.kopterHeading + ", "
                + status.kopterErrorCode + ", "
                + status.gsmSignalStrength + ", "
                + status.kopterVoltage + ", "
                + status.gpsSatCount + ", "
                + status.batteryCurrent + ", "
                + status.batteryCapacity + ", "
                + status.kopterSpeed + ", "
                + status.kopterRcSignal + ", "
                + status.kopterVario + ", '"
                + status.flagsNC + "', '"
                + status.fcStatusFlags1 + "', '"
                + status.fcStatusFlags2 + "', "
                +"NOW())";
        //System.out.println(queryString);
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
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
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
            st_1 = con_1.createStatement();

            queryString = "SELECT * from followme WHERE followMeDeviceId = " + getMatchedDevice(deviceId) + " AND sent = 0 ORDER BY time DESC LIMIT 1";
            System.out.println(queryString);
            rs_1 = st_1.executeQuery(queryString);
            FollowMeData data = new FollowMeData();

            if (rs_1.next()) {
                //data.kopterID = rs_1.getInt("kopterId");
                data.bearing = rs_1.getInt("bearing");
                data.lat = rs_1.getDouble("latitude");
                data.lng = rs_1.getDouble("longitude");
                data.event = rs_1.getInt("event");
                data.time = rs_1.getTimestamp("time");
                data.followMeDeviceId = rs_1.getInt("followMeDeviceId");
            }

            Gson json = new Gson();

            output = json.toJson(data);
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
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
            st_1 = con_1.createStatement();

            queryString = "INSERT INTO followme (`latitude`, `longitude`, `bearing`, `event`, `time`, `followMeDeviceId`, `sent`) VALUES ("
                    + "'" + data.lat + "', "
                    + "'" + data.lng + "', "
                    + "'" + data.bearing + "', "
                    + "'" + data.event + "', "
                    + "NOW(), "
                    + "'" + data.followMeDeviceId + "', "
                    + "'" + "0" + "')";

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
        String queryString = "SELECT followMeDeviceId from devicematching WHERE kopterId = " + deviceId + " ORDER BY 'id' DESC LIMIT 1";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
            st_1 = con_1.createStatement();

            rs_1 = st_1.executeQuery(queryString);

            while (rs_1.next()) {
                matchedFollowMeDevice = rs_1.getInt(1);
            }
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
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
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
                status.flagsNC = rs_1.getString("ncFlags");
                status.updateTime = rs_1.getTimestamp("updateTime");

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
                    + "WHERE followme.time > DATE_SUB(CURRENT_TIMESTAMP(),INTERVAL 1 HOUR)";
            
        } else {
            queryString = "SELECT * from followme "
                    + "INNER JOIN followmedevices AS d "
                    + "ON followme.followMeDeviceId = d.id "
                    + "WHERE followme.followMeDeviceId = " +deviceId
                    + " ORDER BY followme.`id` DESC LIMIT 1";
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
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
        }
        return jsonString;
    }

    public enum Device {

        mk, mp, other;
    }
}
