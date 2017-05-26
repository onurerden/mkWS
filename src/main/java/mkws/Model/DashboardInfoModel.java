/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.Model;

/**
 *
 * @author onurerden
 */
public class DashboardInfoModel {
    private int userCount = 15;
    private int routeCount = 10;
    private double dbSize = 15.6;

    /**
     * @return the userCount
     */
    public int getUserCount() {
        return userCount;
    }

    /**
     * @param userCount the userCount to set
     */
    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    /**
     * @return the routeCount
     */
    public int getRouteCount() {
        return routeCount;
    }

    /**
     * @param routeCount the routeCount to set
     */
    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }

    /**
     * @return the dbSize
     */
    public double getDbSize() {
        return dbSize;
    }

    /**
     * @param dbSize the dbSize to set
     */
    public void setDbSize(double dbSize) {
        this.dbSize = dbSize;
    }
    }
