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
import mkws.ServerEngine;

/**
 *
 * @author oerden
 */
public class GetKopterSessionInfo {

    private String voltageSerie = "{}";
    private String currentSerie = "{}";
    private String capacitySerie = "{}";
    private int sessionId = 0;

    public void setSessionId(int id) {
        this.sessionId = id;
        ServerEngine server = new ServerEngine();
        ArrayList<KopterStatus> list = server.getKopterSessionData(id);
        ArrayList<XChartData> voltageList = new ArrayList<>();
        ArrayList<XChartData> currentList = new ArrayList<>();
        ArrayList<XChartData> capacityList = new ArrayList<>();
        int i=0;
        for (KopterStatus status : list) {
            i++;
            XChartData newVoltageData = new XChartData();
            XChartData newCurrentData = new XChartData();
            XChartData newCapacityData = new XChartData();
            newVoltageData.y=status.getKopterVoltage()/10.0;
            newCurrentData.y=status.getBatteryCurrent()/10.0;
            newCapacityData.y=(double)status.getBatteryCapacity();
            
            //newData.x=status.getUpdateTime().toString();
            newVoltageData.x=i;
            newCurrentData.x=i;
            newCapacityData.x=i;
            voltageList.add(newVoltageData);
            currentList.add(newCurrentData);
            capacityList.add(newCapacityData);
        }

        Gson gson = new Gson();
        this.voltageSerie = gson.toJson(voltageList);
        this.currentSerie = gson.toJson(currentList);
        this.capacitySerie = gson.toJson(capacityList);
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

    private class XChartData{
        double y;
        int x;
    }
}
