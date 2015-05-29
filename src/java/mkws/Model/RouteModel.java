/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mkws.Model;

import java.sql.Timestamp;

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
}
