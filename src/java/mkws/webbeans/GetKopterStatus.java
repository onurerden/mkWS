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
        this.kopterId = ks.getKopterId();
        this.batteryCapacity = ks.getBatteryCapacity();
        this.batteryCurrent = ks.getBatteryCurrent();
        this.gpsSatCount = ks.getGpsSatCount();
        this.gsmSignalStrength = ks.getGsmSignalStrength();
        this.homeLatitude = ks.getHomeLatitude();
        this.homeLongitude = ks.getHomeLongitude();
        this.kopterAltitude = ks.getKopterAltitude();
        this.kopterErrorCode = ks.getKopterErrorCode();
        this.kopterHeading = ks.getKopterHeading();
        this.kopterId = ks.getKopterId();
        this.kopterLatitude = ks.getKopterLatitude();
        this.kopterLongitude = ks.getKopterLongitude();
        this.kopterRcSignal = ks.getKopterRcSignal();
        this.kopterSpeed = ks.getKopterSpeed();
        this.kopterVario = ks.getKopterVario();
        this.kopterVoltage = ks.getKopterVoltage();
        this.setUpdateTime(ks.getUpdateTime());

    }

}
