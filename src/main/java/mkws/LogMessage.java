/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mkws;

/**
 *
 * @author oerden
 */
public class LogMessage {
    private int deviceId;
    private String deviceType; //MK, MP or SERVER
    private int logLevel; //hata mesajı 1, bilgi mesajı 2, diğer 99
    private String logMessage;  //250 char max

    /**
     * @return the deviceId
     */
    public int getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return the deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the logLevel
     */
    public int getLogLevel() {
        return logLevel;
    }

    /**
     * @param logLevel the logLevel to set
     */
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * @return the logMessage
     */
    public String getLogMessage() {
        return logMessage;
    }

    /**
     * @param logMessage the logMessage to set
     */
    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
    
    
    
}
