/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.Model;

import com.google.gson.Gson;
import java.util.ArrayList;
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

    public class Aggregates {

    }
class AltitudeChartValues {

        public double y = 0;
        public double x = 0; //id
    }

    
    @SuppressWarnings("empty-statement")
    public int populateWorkout(int routeId) {
        GetRouteDetails gatherer = new GetRouteDetails();
        gatherer.setRouteId(routeId);
        int i=0;
        ArrayList<Object[]> list = new ArrayList<>();
        for(Double d :gatherer.getLatitudeList()){
        Object[] o = new Object[2];
        o[0] = i;
        o[1] = Double.valueOf(d);
                i++;
                list.add(o);
        }
        System.out.println(new Gson().toJson(list));
        return 0;
    }
}
