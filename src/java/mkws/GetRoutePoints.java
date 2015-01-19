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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author onurerden
 */
public class GetRoutePoints {
    private int routeId = -1;
    private String routePoints ="";
    
    public void setRouteId(int i){
        this.routeId = i;
    }
    public int getRouteId(){
        return this.routeId;
    }
    
    public String getRoutePoints(){
       // this.routePoints = "[38.425218, 27.151823],[38.445388, 27.173281],[38.455874, 27.203665],";
       List<String> points = new ArrayList<String>(); 
       Credentials cr = new Credentials();
       this.routePoints="";
Connection con_1 = null;
            Statement st_1 = null;
            ResultSet rs_1 = null;
        try {
            /* TODO output your page here. You may use following sample code. */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con_1 = DriverManager.getConnection(cr.getMysqlConnectionString(),cr.dbUserName , cr.dbPassword);
            st_1 = con_1.createStatement();
            String query = "SELECT latitude, longitude FROM followMe WHERE routeId = 45";
            rs_1 = st_1.executeQuery(query);
            
            while(rs_1.next()){
                points.add("[" + rs_1.getString("latitude") + "," + rs_1.getString("longitude")+"],");
                this.routePoints = this.routePoints + "[" + rs_1.getString("latitude") + "," + rs_1.getString("longitude")+"],";
            }
            
        }catch (ClassNotFoundException e){
            
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (SQLException e) {
        } finally {
           try {
               con_1.close();
           } catch (SQLException ex) {
               
           }
        }
       
       
        
        
        return this.routePoints;
        
    }
    
    
}
