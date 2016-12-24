/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.Model;

import java.sql.Timestamp;

/**
 *
 * @author onurerden
 */
public class FollowMeDataModel {
/*    
    @property NSInteger routeId;
@property NSInteger sessionId;
@property double lat;
@property double lng;
@property int bearing;
@property int event;
@property NSInteger followMeDeviceId;
@property double speed;
@property double altitude;
@property NSDate *time;
*/
    
    private int routeId;
    private int sessionId;
    private double lat;
    private double lng;
    private int bearing;
    private int event;
    private int followMeDeviceId;
    private double speed;
    private double altitude;
    private Timestamp time;

    /**
     * @return the routeId
     */
    public int getRouteId() {
        return routeId;
    }

    /**
     * @param routeId the routeId to set
     */
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    /**
     * @return the sessionId
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * @return the bearing
     */
    public int getBearing() {
        return bearing;
    }

    /**
     * @param bearing the bearing to set
     */
    public void setBearing(int bearing) {
        this.bearing = bearing;
    }

    /**
     * @return the event
     */
    public int getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(int event) {
        this.event = event;
    }

    /**
     * @return the followMeDeviceId
     */
    public int getFollowMeDeviceId() {
        return followMeDeviceId;
    }

    /**
     * @param followMeDeviceId the followMeDeviceId to set
     */
    public void setFollowMeDeviceId(int followMeDeviceId) {
        this.followMeDeviceId = followMeDeviceId;
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
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }
    
    
}
