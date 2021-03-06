/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.Model;

import com.google.gson.Gson;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import mkws.FollowMeData;
import mkws.MKHelper;
import mkws.ServerEngine;
import mkws.webbeans.GetRouteDetails;

/**
 *
 * @author onurerden
 */
public class MMRWorkout {

    private String start_datetime;
    private String name;
    private String privacy = "/v7.1/privacy_option/0/";
    private String attachments;
    private TimeSeries time_series;
    private String start_locale_timezone = "Europe/Istanbul";
    private String reference_key; //version_rotaId
    private String notes = "Workout is submitted by FollowMe (mkWS)";
    private Aggregates aggregates;
    private transient int deviceId;
    private transient int userId;
    private String activity_type = "/v7.1/activity_type/11/"; //11 ride, 16 run, 772 driving

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

    public class Sharing {

        boolean facebook = false;
        boolean twitter = false;
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

        double distance_total = 0.0;
        double active_time_total = 0.0;
    }

    class AltitudeChartValues {

        public double y = 0;
        public double x = 0; //id
    }

    public int getDeviceId() {

        return this.deviceId;
    }

    @SuppressWarnings("empty-statement")
    public int populateWorkout(int routeId) {
        ServerEngine server = new ServerEngine();
        RouteModel route = server.getRouteDetails(routeId, true);

        // GetRouteDetails gatherer = new GetRouteDetails();
        //gatherer.setRouteId(routeId);
        TimeSeries series = new TimeSeries();
//        series.distance = new Object[gatherer.getLatitudeList().size()-1];
//        series.position = new Object[gatherer.getLatitudeList().size()-1];
//        series.speed = new Object[gatherer.getLatitudeList().size()-1];
        series.distance = new Object[route.getFollowMeData().size() ];
        series.position = new Object[route.getFollowMeData().size() ];
        series.speed = new Object[route.getFollowMeData().size() ];
        System.out.println("Array size is: " + route.getFollowMeData().size());
        Timestamp firstDate = null;
        // List<Double> mmrTimeList = new ArrayList<>();
        if (firstDate == null) {
            firstDate = route.getFollowMeData().get(0).getTime();
        }
        double distance = 0.0;
        int i = 0;
        for (FollowMeDataModel fm : route.getFollowMeData()) {
            Object[] distanceArray = new Object[2];
            Object[] speedArray = new Object[2];
            Object[] locationArray = new Object[2];

            Location loc = new Location();
            loc.elevation = fm.getAltitude();
            loc.lat = fm.getLat();
            loc.lng = fm.getLng();

            double difference = (double) (fm.getTime().getTime() - firstDate.getTime())/1000;

            locationArray[0] = difference;
            locationArray[1] = loc;

            

            speedArray[0] = difference;
            speedArray[1] = fm.getSpeed();

            series.speed[i] = speedArray;
            series.position[i] = locationArray;
            
            
            if (i != 0) {
                MKHelper helper = new MKHelper();
                Object[] previousLocationArray = (Object[]) series.position[i-1];
                distance = distance + helper.distanceBetweenPoints(loc.lat, loc.lng, ((Location) previousLocationArray[1]).lat, ((Location) previousLocationArray[1]).lng)*1000;
               // System.out.println(i +". distance is: " + distance);
            }
            distanceArray[0] = difference;
            distanceArray[1] = distance;
            series.distance[i] = distanceArray;
            
            i++;
        }
        
        System.out.println("Series Completed.");
        time_series = series;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.S");

        try {
            Date date = format.parse(route.getTime().toString());

            this.start_datetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        } catch (ParseException ex) {
            System.out.println("Start_datetime ayarlanamadı.");
        }
        this.aggregates = new Aggregates();

        this.aggregates.distance_total = route.getRouteLength() * 1000.0;
        this.aggregates.active_time_total = (double) (route.getFollowMeData().get(route.getFollowMeData().size() - 1).getTime().getTime()
                - route.getFollowMeData().get(0).getTime().getTime()) / 1000.0;
        
        System.out.println("Active total time completed.");
        
        this.setDeviceId(route.getUserId());
        if (route.getTitle() == null) {
            this.name = "FollowMeRoute: " + route.getRouteId();
        } else {
            this.name = route.getTitle();
        }
        this.reference_key = "v008_" + route.getRouteId();//increase if anything changes

        this.notes = this.notes + "\nRouteId: " + route.getRouteId();

        switch (route.getType()) {
            case 1: {
                activity_type = "/v7.1/activity_type/16/"; //11 ride, 16 run, 772 driving
                break;
            }
            case 2: {
                activity_type = "/v7.1/activity_type/11/"; //11 ride, 16 run, 772 driving
                break;
            }
            case 3: {
                activity_type = "/v7.1/activity_type/772/"; //11 ride, 16 run, 772 driving
                break;
            }
            default: {
                activity_type = "/v7.1/activity_type/11/"; //11 ride, 16 run, 772 driving
                break;
            }
        }

        //System.out.println(new Gson().toJson(this));
        return 0;

    }

    public void setDeviceId(int id) {
        this.deviceId = id;
    }
}
