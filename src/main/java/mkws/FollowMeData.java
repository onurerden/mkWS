/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import java.sql.Timestamp;
import mkws.Model.MKMission;

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
    String name = "";
    int routeId = 0;
    int sessionId = -1;
    private double speed = 0.0; // in meters/sec
    private double altitude = 0.0; //in meters
      private MKMission mission = null;
      private boolean doNothing = false;

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return this.name;
    }

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

    public int getRouteId() {
        return this.routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(int id) {
        this.sessionId = id;
    }

    /**
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @return the altitude
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
     * @return the mission
     */
    public MKMission getMission() {
        return mission;
    }

    /**
     * @param mission the mission to set
     */
    public void setMission(MKMission mission) {
        this.mission = mission;
    }

    /**
     * @return the doNothing
     */
    public boolean isDoNothing() {
        return doNothing;
    }

    /**
     * @param doNothing the doNothing to set
     */
    public void setDoNothing(boolean doNothing) {
        this.doNothing = doNothing;
    }
}
