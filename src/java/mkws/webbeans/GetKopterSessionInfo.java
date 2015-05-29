/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.webbeans;

import com.google.gson.Gson;
import java.sql.Timestamp;
import java.util.ArrayList;
import mkws.KopterStatus;
import mkws.MapTools;
import mkws.Model.RoutePointClass;
import mkws.ServerEngine;

/**
 *
 * @author oerden
 */
public class GetKopterSessionInfo {

    private String voltageSerie = "{}";
    private String currentSerie = "{}";
    private String capacitySerie = "{}";
    private String altitudeSerie = "{}";
    private String gsmSignalSerie = "{}";
    private String rcSignalSerie = "{}";
    private String satSerie = "{}";
    private String latLonSerie = "";
    private String mapBounds ="";
    private int sessionId = 0;

    public void setSessionId(int id) {
        this.sessionId = id;
        ServerEngine server = new ServerEngine();
        ArrayList<KopterStatus> list = server.getKopterSessionData(id);
        ArrayList<XChartData> voltageList = new ArrayList<>();
        ArrayList<XChartData> currentList = new ArrayList<>();
        ArrayList<XChartData> capacityList = new ArrayList<>();
        ArrayList<XChartData> altitudeList = new ArrayList<>();
        ArrayList<XChartData> gsmSignalList = new ArrayList<>();
        ArrayList<XChartData> rcSignalList = new ArrayList<>();
        ArrayList<XChartData> satList = new ArrayList<>();
        ArrayList<RoutePointClass> rpcList = new ArrayList<>();

        int i = 0;
        for (KopterStatus status : list) {
            i++;
            XChartData newVoltageData = new XChartData();
            XChartData newCurrentData = new XChartData();
            XChartData newCapacityData = new XChartData();
            XChartData newAltitudeData = new XChartData();
            XChartData newGsmSignalData = new XChartData();
            XChartData newRcSignalData = new XChartData();
            XChartData newSatData = new XChartData();
            RoutePointClass newRoutePoint = new RoutePointClass();
            
            
            newVoltageData.y = status.getKopterVoltage() / 10.0;
            newCurrentData.y = status.getBatteryCurrent() / 10.0;
            newCapacityData.y = (double) status.getBatteryCapacity();
            newAltitudeData.y = status.getKopterAltitude() / 10;
            newGsmSignalData.y = status.getGsmSignalStrength();
            newRcSignalData.y = status.getKopterRcSignal();
            newSatData.y = status.getGpsSatCount();
            newRoutePoint.setLatitude(status.getKopterLatitude());
            newRoutePoint.setLongitude(status.getKopterLongitude());
            //newData.x=status.getUpdateTime().toString();
                        
            newVoltageData.x = i;
            newCurrentData.x = i;
            newCapacityData.x = i;
            newAltitudeData.x = i;
            newGsmSignalData.x = i;
            newRcSignalData.x = i;
            newSatData.x = i;
            voltageList.add(newVoltageData);
            currentList.add(newCurrentData);
            capacityList.add(newCapacityData);
            altitudeList.add(newAltitudeData);
            gsmSignalList.add(newGsmSignalData);
            rcSignalList.add(newRcSignalData);
            satList.add(newSatData);
            rpcList.add(newRoutePoint);
        }

        Gson gson = new Gson();
        this.voltageSerie = gson.toJson(voltageList);
        this.currentSerie = gson.toJson(currentList);
        this.capacitySerie = gson.toJson(capacityList);
        this.altitudeSerie = gson.toJson(altitudeList);
        this.gsmSignalSerie = gson.toJson(gsmSignalList);
        this.rcSignalSerie = gson.toJson(rcSignalList);
        this.satSerie = gson.toJson(satList);
        MapTools.setPoints(rpcList);
        this.latLonSerie = MapTools.getYandexMapPositionData();
        this.mapBounds=MapTools.getYandexBoundsData();
        
                
    }

    /**
     * @return the voltageSerie
     */
    public String getVoltageSerie() {
        return voltageSerie;
    }

    public String getCurrentSerie() {
        return currentSerie;
    }

    public String getCapacitySerie() {
        return capacitySerie;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public String getAltitudeSerie() {
        return this.altitudeSerie;
    }

    public String getGsmSignalSerie() {
        return this.gsmSignalSerie;
    }

    public String getRcSignalSerie() {
        return this.rcSignalSerie;
    }

    public String getSatSerie() {
        return this.satSerie;
    }

    public String getLatLonSerie() {
        return this.latLonSerie;
    }
    public String getMapBounds(){
        return this.mapBounds;
    }

    private class XChartData {

        double y;
        int x;
    }
}
