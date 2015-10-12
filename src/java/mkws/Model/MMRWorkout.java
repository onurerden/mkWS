/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.Model;

import java.util.ArrayList;

/**
 *
 * @author onurerden
 */
public class MMRWorkout {

    private String start_datetime;
    private String name;
    private String privacy;
    private String attachments;
    private TimeSeries time_series;
    private String start_locale_timezone = "Europe/Istanbul";
    private int reference_key; //rotaId
    private String notes = "Workout is submitted by FollowMe (mkWS)";
    private Aggregates aggregates;

    
    
    public class TimeSeries {

        ArrayList<ArrayList<Object>> distance = new ArrayList();
        ArrayList<ArrayList<Object>> position = new ArrayList();
        ArrayList<ArrayList<Object>> speed = new ArrayList();

        public void addToSeries(double newDistance, Location newPosition, double newSpeed) {
            int size = distance.size() - 1;
            
        }
    }

        public class Location {

            public double lat;
            public double lng;
            public double elevation;

        }
        
        public class Aggregates{
            
        }

    }
