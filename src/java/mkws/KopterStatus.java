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
public class KopterStatus {

    public int kopterHeading;  //in degree
    public int kopterAltitude; //in meters
    public double kopterLatitude;
    public double kopterLongitude;
    public double homeLatitude;
    public double homeLongitude;
    public int kopterErrorCode;
    public int gsmSignalStrength;
    public int kopterVoltage; //in mV
    public int gpsSatCount;
    public double batteryCurrent; //in mA
    public int batteryCapacity; //in mAh
    public double kopterSpeed; //in m/s
    public int kopterRcSignal;
    public double kopterVario; //in m/s
    public int kopterId;
    public Timestamp updateTime;

    // NC flags
    boolean flNC_FLAG_FREE = false; //0b00000001
    boolean flNC_FLAG_PH = false; //0b00000010
    boolean flNC_FLAG_CH = false; //0b00000100
    boolean flNC_FLAG_RANGE_LIMIT = false; //0b00001000
    boolean flNC_FLAG_NOSERIALLINK = false; //0b00010000
    boolean flNC_FLAG_TARGET_REACHED = false; //0b00100000
    boolean flNC_FLAG_MANUAL = false; //0b01000000
    boolean flNC_FLAG_GPS_OK = false; //0b10000000
    String flagsNC = "00000000";

    //FCStatusFlags-1
    boolean flFC_STATUS_MOTOR_RUN = false;//                    0x01
    boolean flFC_STATUS_FLY = false;//                          0x02
    boolean flFC_STATUS_CALIBRATE = false;//                   0x04
    boolean flFC_STATUS_START = false;//                      0x08
    boolean flFC_STATUS_EMERGENCY_LANDING = false;//           0x10
    boolean flFC_STATUS_LOWBAT = false;//                     0x20
    boolean flFC_STATUS_VARIO_TRIM_UP = false;//             0x40
    boolean flFC_STATUS_VARIO_TRIM_DOWN = false;//             0x80
    String fcStatusFlags1 = "00000000";

    //FCStatusFlags-2
    boolean flFC_STATUS2_CAREFREE_ACTIVE = false;//             0x01
    boolean flFC_STATUS2_ALTITUDE_CONTROL_ACTIVE = false;//    0x02
    boolean flFC_STATUS2_FAILSAFE_ACTIVE = false;//            0x04
    boolean flFC_STATUS2_OUT1 = false;//                      0x08
    boolean flFC_STATUS2_OUT2 = false;//                       0x10
    boolean flFC_STATUS2_RES1 = false;//                       0x20
    boolean flFC_STATUS2_RES2 = false;//                       0x40
    boolean flFC_STATUS2_RES3 = false;//                       0x80
    String fcStatusFlags2 = "00000000";

    //parser methods
    void parseFlagsNC(String flString) {
        if (flString.charAt(0) == '0') {
            flNC_FLAG_FREE = false;
        } else {
            flNC_FLAG_FREE = true;
        }

        if (flString.charAt(1) == '0') {
            flNC_FLAG_PH = false;
        } else {
            flNC_FLAG_PH = true;
        }

        if (flString.charAt(2) == '0') {
            flNC_FLAG_CH = false;
        } else {
            flNC_FLAG_CH = true;
        }

        if (flString.charAt(3) == '0') {
            flNC_FLAG_RANGE_LIMIT = false;
        } else {
            flNC_FLAG_RANGE_LIMIT = true;
        }

        if (flString.charAt(4) == '0') {
            flNC_FLAG_NOSERIALLINK = false;
        } else {
            flNC_FLAG_NOSERIALLINK = true;
        }

        if (flString.charAt(5) == '0') {
            flNC_FLAG_TARGET_REACHED = false;
        } else {
            flNC_FLAG_MANUAL = true;
        }

        if (flString.charAt(6) == '0') {
            flNC_FLAG_GPS_OK = false;
        } else {
            flNC_FLAG_GPS_OK = true;
        }

        if (flString.charAt(7) == '0') {
            flNC_FLAG_FREE = false;
        } else {
            flNC_FLAG_FREE = true;
        }

    }

    void parseFlagsStatusFC1(String flString) {
        if (flString.charAt(0) == '0') {
            flFC_STATUS_MOTOR_RUN = false;
        } else {
            flFC_STATUS_MOTOR_RUN = true;
        }
        if (flString.charAt(1) == '0') {
            flFC_STATUS_FLY = false;
        } else {
            flFC_STATUS_FLY = true;
        }
        if (flString.charAt(2) == '0') {
            flFC_STATUS_CALIBRATE = false;
        } else {
            flFC_STATUS_CALIBRATE = true;
        }
        if (flString.charAt(3) == '0') {
            flFC_STATUS_START = false;
        } else {
            flFC_STATUS_START = true;
        }
        if (flString.charAt(4) == '0') {
            flFC_STATUS_EMERGENCY_LANDING = false;
        } else {
            flFC_STATUS_EMERGENCY_LANDING = true;
        }
        if (flString.charAt(5) == '0') {
            flFC_STATUS_LOWBAT = false;
        } else {
            flFC_STATUS_LOWBAT = true;
        }
        if (flString.charAt(6) == '0') {
            flFC_STATUS_VARIO_TRIM_UP = false;
        } else {
            flFC_STATUS_VARIO_TRIM_UP = true;
        }
        if (flString.charAt(7) == '0') {
            flFC_STATUS_VARIO_TRIM_DOWN = false;
        } else {
            flFC_STATUS_VARIO_TRIM_DOWN = true;
        }
    }

    void parseFlagsStatusFC2(String flString) {
        if (flString.charAt(0) == '0') {
            flFC_STATUS2_CAREFREE_ACTIVE = false;
        } else {
            flFC_STATUS2_CAREFREE_ACTIVE = true;
        }
        if (flString.charAt(1) == '0') {
            flFC_STATUS2_ALTITUDE_CONTROL_ACTIVE = false;
        } else {
            flFC_STATUS2_ALTITUDE_CONTROL_ACTIVE = true;
        }
        if (flString.charAt(2) == '0') {
            flFC_STATUS2_FAILSAFE_ACTIVE = false;
        } else {
            flFC_STATUS2_FAILSAFE_ACTIVE = true;
        }
        if (flString.charAt(3) == '0') {
            flFC_STATUS2_OUT1 = false;
        } else {
            flFC_STATUS2_OUT1 = true;
        }
        if (flString.charAt(4) == '0') {
            flFC_STATUS2_OUT2 = false;
        } else {
            flFC_STATUS2_OUT2 = true;
        }
        if (flString.charAt(5) == '0') {
            flFC_STATUS2_RES1 = false;
        } else {
            flFC_STATUS2_RES1 = true;
        }
        if (flString.charAt(6) == '0') {
            flFC_STATUS2_RES2 = false;
        } else {
            flFC_STATUS2_RES2 = true;
        }
        if (flString.charAt(7) == '0') {
            flFC_STATUS2_RES3 = false;
        } else {
            flFC_STATUS2_RES3 = true;
        }
    }

    /**
     * @return the kopterHeading
     */
    public int getKopterHeading() {
        return kopterHeading;
    }

    /**
     * @return the kopterAltitude
     */
    public int getKopterAltitude() {
        return kopterAltitude;
    }

    /**
     * @return the kopterLatitude
     */
    public double getKopterLatitude() {
        return kopterLatitude;
    }

    /**
     * @return the kopterLongitude
     */
    public double getKopterLongitude() {
        return kopterLongitude;
    }

    /**
     * @return the homeLatitude
     */
    public double getHomeLatitude() {
        return homeLatitude;
    }

    /**
     * @return the homeLongitude
     */
    public double getHomeLongitude() {
        return homeLongitude;
    }

    /**
     * @return the kopterErrorCode
     */
    public int getKopterErrorCode() {
        return kopterErrorCode;
    }

    /**
     * @return the gsmSignalStrength
     */
    public int getGsmSignalStrength() {
        return gsmSignalStrength;
    }

    /**
     * @return the kopterVoltage
     */
    public int getKopterVoltage() {
        return kopterVoltage;
    }

    /**
     * @return the gpsSatCount
     */
    public int getGpsSatCount() {
        return gpsSatCount;
    }

    /**
     * @return the batteryCurrent
     */
    public double getBatteryCurrent() {
        return batteryCurrent;
    }

    /**
     * @return the batteryCapacity
     */
    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    /**
     * @return the kopterSpeed
     */
    public double getKopterSpeed() {
        return kopterSpeed;
    }

    /**
     * @return the kopterRcSignal
     */
    public int getKopterRcSignal() {
        return kopterRcSignal;
    }

    /**
     * @return the kopterVario
     */
    public double getKopterVario() {
        return kopterVario;
    }

    /**
     * @return the kopterId
     */
    public int getKopterId() {
        return kopterId;
    }

    /**
     * @return the updateTime
     */
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
