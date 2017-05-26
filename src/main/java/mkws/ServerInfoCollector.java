/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import java.sql.Connection;
import java.sql.DriverManager;
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
    public DashboardInfoModel getServerInfo(){
        DashboardInfoModel info = new DashboardInfoModel();
        try {
            Credentials cr = new Credentials();
            Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(), cr.getDbUserName(), cr.getDbPassword());
            st_1 = con_1.createStatement();
            String query = "SELECT * FROM `members`";
            //System.out.println(query);
            rs_1 = st_1.executeQuery(query);

            while (rs_1.next()) {
info.setUserCount(rs_1.getInt(0));
            }

            con_1.close();

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        return info;
    }
}
