/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import java.sql.Timestamp;

/**
 *
 * @author oerden
 */
public class FollowMeData {

    Double lat = 0.0;
    Double lng = 0.0;
    int bearing = 0;
    //int kopterID = 0;
    Timestamp time;
    int event = 0;
    int followMeDeviceId;

    public void setLat(double latitude) {
        this.lat = latitude;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLon(double longitude) {
        this.lng = longitude;
    }

    public double getLon() {
        return this.lng;
    }

    public void setBearing(int brng) {
        this.bearing = brng;
    }

    public int getBearing() {
        return this.bearing;
    }

    public void setTime(Timestamp tmstmp) {
        this.time = tmstmp;
    }

    public Timestamp getTime() {
        return this.time;
    }

    public void setEvent(int e) {
        this.event = e;
    }

    public int getEvent() {
        return this.event;
    }

    public void setFollowMeDeviceId(int id) {
        this.followMeDeviceId = id;
    }

    public int getFollowMeDeviceId() {
        return this.followMeDeviceId;
    }

}
