/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mkws.Model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author oerden
 */

public class RouteModel {
    private int routeId;
    private Timestamp time;
    private int sessionId;
    private double routeLength;
    private double routeMeanSpeed;
    private int userId = 1;
    private boolean isDeleted;
    private boolean isEnded;
    private int followMeDeviceId;
    private ArrayList followMeData;

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
     * @return the routeLength
     */
    public double getRouteLength() {
        return routeLength;
    }

    /**
     * @param routeLength the routeLength to set
     */
    public void setRouteLength(double routeLength) {
        this.routeLength = routeLength;
    }

    /**
     * @return the routeMeanSpeed
     */
    public double getRouteMeanSpeed() {
        return routeMeanSpeed;
    }

    /**
     * @param routeMeanSpeed the routeMeanSpeed to set
     */
    public void setRouteMeanSpeed(double routeMeanSpeed) {
        this.routeMeanSpeed = routeMeanSpeed;
    }

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

    /**
     * @return the isDeleted
     */
    public boolean isIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @return the isEnded
     */
    public boolean isIsEnded() {
        return isEnded;
    }

    /**
     * @param isEnded the isEnded to set
     */
    public void setIsEnded(boolean isEnded) {
        this.isEnded = isEnded;
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
     * @return the followMeData
     */
    public ArrayList getFollowMeData() {
        return followMeData;
    }

    /**
     * @param followMeData the followMeData to set
     */
    public void setFollowMeData(ArrayList followMeData) {
        this.followMeData = followMeData;
    }
}
