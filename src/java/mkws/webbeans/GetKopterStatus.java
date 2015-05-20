/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.webbeans;

import com.google.gson.Gson;
import mkws.KopterStatus;
import mkws.ServerEngine;

/**
 *
 * @author oerden
 */
public class GetKopterStatus extends KopterStatus {

    public void setKopterId(int id) {
        ServerEngine server = new ServerEngine();

        KopterStatus ks = new KopterStatus();
        Gson jsonObject = new Gson();
        ks = jsonObject.fromJson(server.getKopterStatus(id), KopterStatus.class);
        this.setKopterId(ks.getKopterId());
        this.setBatteryCapacity(ks.getBatteryCapacity());
        this.setBatteryCurrent(ks.getBatteryCurrent());
        this.setGpsSatCount(ks.getGpsSatCount());
        this.setGsmSignalStrength(ks.getGsmSignalStrength());
        this.setHomeLatitude(ks.getHomeLatitude());
        this.setHomeLongitude(ks.getHomeLongitude());
        this.setKopterAltitude(ks.getKopterAltitude());
        this.setKopterErrorCode(ks.getKopterErrorCode());
        this.setKopterHeading(ks.getKopterHeading());
        this.setKopterId(ks.getKopterId());
        this.setKopterLatitude(ks.getKopterLatitude());
        this.setKopterLongitude(ks.getKopterLongitude());
        this.setKopterRcSignal(ks.getKopterRcSignal());
        this.setKopterSpeed(ks.getKopterSpeed());
        this.setKopterVario(ks.getKopterVario());
        this.setKopterVoltage(ks.getKopterVoltage());
        this.setUpdateTime(ks.getUpdateTime());
        this.setSessionId(ks.getSessionId());
        

    }

}
