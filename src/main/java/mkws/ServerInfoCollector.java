/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import mkws.Model.DashboardInfoModel;

/**
 *
 * @author onurerden
 */
public class ServerInfoCollector {

    public DashboardInfoModel getServerInfo() {
        DashboardInfoModel info = new DashboardInfoModel();
        try {
            info.setUserCount(getUserCount());
            info.setRouteCount(getRouteCount());
            info.setDbSize(getDBSize());

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;
    }
 
    private int getUserCount() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        int userCount= 0;
        //Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
            ServerEngine server = new ServerEngine();
            con_1=server.getConnection();
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT COUNT(id) from members";
            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                userCount = rs_1.getInt(1);
            }

            con_1.close();
            return userCount;
                    
    }
    
    private int getRouteCount() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        int userCount= 0;
       // Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
            ServerEngine server = new ServerEngine();
            con_1=server.getConnection();
          //  Class.forName("com.mysql.jdbc.Driver").newInstance();
          //  con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT COUNT(id) from route where isDeleted=FALSE";
            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                userCount = rs_1.getInt(1);
            }

            con_1.close();
            return userCount;
                    
    }
 
    private double getDBSize() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        double dbSize=0.0;
      //Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
            ServerEngine server = new ServerEngine();
            con_1=server.getConnection();
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT table_schema \"DB Name\", " +
"Round(Sum(data_length + index_length) / 1024 / 1024, 1) \"DB Size in MB\" " +
"FROM   information_schema.tables " +
"WHERE table_schema = \"mk\"" +
"GROUP  BY table_schema; ";
            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
                dbSize = rs_1.getDouble(2);
            }

            con_1.close();
            return dbSize;   
    }
    
    
    
}
