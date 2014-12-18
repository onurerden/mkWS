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
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        Statement st_2 = con_1.createStatement();
                        st_2.executeUpdate("UPDATE kopter SET latestTouch = '" + timeStamp + "' WHERE id = " + kopter.id);

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
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            switch (device) {
                case mk: {
                    queryString = "INSERT INTO `kopter`(`UID`, `name`, `active`) VALUES ('" + uid + "', '" + name + "', 1)";
                    break;
                }
                case mp: {
                    queryString = "INSERT INTO `followmedevices`(`UID`, `name`, `registerDate`) VALUES ('" + uid + "', '" + name + "', '" + timeStamp + "')";;
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
        String queryString = "";
        
        KopterStatus status = new KopterStatus();
        Gson jsonObject = new Gson();
        status = jsonObject.fromJson(jsonStatus,KopterStatus.class);
        
        System.out.println(status.batteryCapacity);
        
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.dbUserName, cr.dbPassword);
            st_1 = con_1.createStatement();
            result = st_1.executeUpdate(queryString);
            
            
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

            queryString = "SELECT * from followme WHERE kopterId = " + deviceId + " AND sent = 0";
            rs_1 = st_1.executeQuery(queryString);
            FollowMeData data = new FollowMeData();
            data.kopterID = -2;

            if (rs_1.next()) {
                data.kopterID = rs_1.getInt("kopterId");
                data.bearing = rs_1.getInt("bearing");
                data.lat = rs_1.getDouble("latitude");
                data.lng = rs_1.getDouble("longitude");
                data.event = rs_1.getInt("event");
                data.time = rs_1.getTimestamp("time");
                data.followMeDeviceId = rs_1.getInt("followMeDeviceId");
            }

            Gson json = new Gson();

            output = json.toJson(data);

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

    public enum Device {

        mk, mp, other;
    }
}
