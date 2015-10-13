/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.Model;

import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import mkws.webbeans.GetRouteDetails;

/**
 *
 * @author onurerden
 */
public class MMRWorkout {

    private String start_datetime;
    private String name;
    private String attachments;
    private TimeSeries time_series;
    private String start_locale_timezone = "Europe/Istanbul";
    //private int reference_key; //rotaId
    private String notes = "Workout is submitted by FollowMe (mkWS)";
    private Aggregates aggregates;
    private int deviceId;
    private String activity_type="/v7.1/activity_type/11/";
    private String privacy="/v7.1/privacy_option/0/";
    
            
    public int getDeviceId(){
        return this.deviceId;
    }

    /**
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public class TimeSeries {

        Object[] distance;
        Object[] position;
        Object[] speed;

    }

    public class Location {

        public double lat;
        public double lng;
        public double elevation;

    }

    public class Aggregates {
public double distance_total;
    }

    public int populateWorkout(int routeId) {
        GetRouteDetails gatherer = new GetRouteDetails();
        gatherer.setRouteId(routeId);
        int i = 0;
        TimeSeries series = new TimeSeries();
        series.distance = new Object[gatherer.getLatitudeList().size()];
        series.position = new Object[gatherer.getLatitudeList().size()];
        series.speed = new Object[gatherer.getLatitudeList().size()];

        for (Double d : gatherer.getLatitudeList()) {
            Object[] distanceArray = new Object[2];
            Object[] speedArray = new Object[2];
            Object[] locationArray = new Object[2];

            
            Location loc = new Location();
            loc.elevation=gatherer.getAltitudeList().get(i);
            loc.lat=gatherer.getLatitudeList().get(i);
            loc.lng=gatherer.getLongitudeList().get(i);
            locationArray[0]=i;
            locationArray[1]=loc;
            
            distanceArray[0]=i;
            distanceArray[1]=gatherer.getDistanceList().get(i)*1000;
            
            speedArray[0] = i;
            speedArray[1] = gatherer.getSpeedList().get(i);
            
            series.speed[i]=speedArray;
            series.position[i]=locationArray;
            series.distance[i]=distanceArray;
            i++;

        }
        time_series=series;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.S");
        
        
        try{
        Date date = format.parse(gatherer.getRouteCreationDate().toString());
        
        this.start_datetime= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date); 
        } catch (ParseException ex){
            System.out.println("Start_datetime ayarlanamadı.");
        }
        this.aggregates = new Aggregates();
                
        this.aggregates.distance_total=gatherer.getRouteLength()*1000;
        this.setDeviceId(gatherer.getDeviceId());
        this.name = "FollowMeRoute: " + gatherer.getRouteId();
        //this.reference_key = gatherer.getRouteId();
        this.notes = this.notes + "\nRouteId: " + gatherer.getRouteId();
        
        System.out.println(new Gson().toJson(this));
        
        return 0;
    }
}