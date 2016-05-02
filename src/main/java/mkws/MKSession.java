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
public class MKSession {
    private int deviceId;
    private int sessionId;
    private Timestamp time;
    private DeviceTypes deviceType;

    /**
     * @return the kopterId
     */
    public int getDeviceId() {
        return deviceId;
    }

    /**
     * @param kopterId the kopterId to set
     */
    public void setDeviceId(int kopterId) {
        this.deviceId = kopterId;
    }

    /**
     * @return the KopterSessionId
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * @param KopterSessionId the KopterSessionId to set
     */
    public void setSessionId(int KopterSessionId) {
        this.sessionId = KopterSessionId;
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
     * @return the deviceType
     */
    public DeviceTypes getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(DeviceTypes deviceType) {
        this.deviceType = deviceType;
    }
}
