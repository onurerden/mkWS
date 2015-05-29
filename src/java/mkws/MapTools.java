/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mkws;

import java.util.ArrayList;
import mkws.Model.RoutePointClass;

/**
 *
 * @author oerden
 */
public class MapTools {
    
    private static ArrayList<RoutePointClass> points= new ArrayList<>();
        
    public static String getYandexMapPositionData(){
        String mapData = "";
        for (RoutePointClass point : points){
            
            mapData = mapData + "[" + point.getLatitude()+","+point.getLongitude() + "],";
            
        }
        return mapData;
    }
    public static String getYandexBoundsData(){
        String boundsData="";
        double minLat=99;
        double maxLat=-99;
        double minLong=99;
        double maxLong=-99;
        
        for (RoutePointClass point : points){
            if (point.getLatitude()<minLat){
                minLat=point.getLatitude();
            }
           if (point.getLongitude()<minLong){
                minLong=point.getLongitude();
            }     
           if (point.getLatitude()>maxLat){
               maxLat=point.getLatitude();
           }
           if (point.getLongitude()>maxLong){
               maxLong=point.getLongitude();
           }                        
        }
        
        boundsData= "[["+minLat+","+minLong+"],"
                        + "["+maxLat+","+maxLong+"]]";
        
        return boundsData;
    }
    

    /**
     * @return the points
     */
    public ArrayList<RoutePointClass> getPoints() {
        return points;
    }

    /**
     * @param newPoints the points to set
     */
   public static void setPoints(ArrayList<RoutePointClass> newPoints) {
        points = newPoints; 
   }
    
    
    
}
