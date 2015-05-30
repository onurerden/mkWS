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

    private int kopterHeading;  //in degree
    private int kopterAltitude; //in meters
    private double kopterLatitude;
    private double kopterLongitude;
    private double homeLatitude;
    private double homeLongitude;
    private int kopterErrorCode;
    private int gsmSignalStrength;
    private int kopterVoltage; //in mV
    private int gpsSatCount;
    private double batteryCurrent; //in mA
    private int batteryCapacity; //in mAh
    private double kopterSpeed; //in m/s
    private int kopterRcSignal;
    private double kopterVario; //in m/s
    public int kopterId;
    private int sessionId;
    private Timestamp updateTime;
    private String blTempList;

    private double TargetLongitude;
    private double TargetLatitude;

    // NC flags
    boolean flNC_FLAG_FREE = false; //0b00000001
    boolean flNC_FLAG_PH = false; //0b00000010
    boolean flNC_FLAG_CH = false; //0b00000100
    boolean flNC_FLAG_RANGE_LIMIT = false; //0b00001000
    boolean flNC_FLAG_NOSERIALLINK = false; //0b00010000
    boolean flNC_FLAG_TARGET_REACHED = false; //0b00100000
    boolean flNC_FLAG_MANUAL = false; //0b01000000
    boolean flNC_FLAG_GPS_OK = false; //0b10000000
    private String FlagsNC = "00000000";

    //FCStatusFlags-1
    boolean flFC_STATUS_MOTOR_RUN = false;//                    0x01
    boolean flFC_STATUS_FLY = false;//                          0x02
    boolean flFC_STATUS_CALIBRATE = false;//                   0x04
    boolean flFC_STATUS_START = false;//                      0x08
    boolean flFC_STATUS_EMERGENCY_LANDING = false;//           0x10
    boolean flFC_STATUS_LOWBAT = false;//                     0x20
    boolean flFC_STATUS_VARIO_TRIM_UP = false;//             0x40
    boolean flFC_STATUS_VARIO_TRIM_DOWN = false;//             0x80
//    private String FcStatusFlags1 = "00000000";
    private String FlagsStatusFC1 = "00000000";

    //FCStatusFlags-2
    boolean flFC_STATUS2_CAREFREE_ACTIVE = false;//             0x01
    boolean flFC_STATUS2_ALTITUDE_CONTROL_ACTIVE = false;//    0x02
    boolean flFC_STATUS2_FAILSAFE_ACTIVE = false;//            0x04
    boolean flFC_STATUS2_OUT1 = false;//                      0x08
    boolean flFC_STATUS2_OUT2 = false;//                       0x10
    boolean flFC_STATUS2_RES1 = false;//                       0x20
    boolean flFC_STATUS2_RES2 = false;//                       0x40
    boolean flFC_STATUS2_RES3 = false;//                       0x80
    //private String FcStatusFlags2 = "00000000";
    private String FlagsStatusFC2 = "00000000";

    public void parseAllFlags() {
        parseFlagsNC(this.getFlagsNC());
        parseFlagsStatusFC1(this.getFcStatusFlags1());
        parseFlagsStatusFC2(this.getFcStatusFlags2());
    }

    //parser methods
    void parseFlagsNC(String flString) {
        if (flString.charAt(7) == '0') {
            this.flNC_FLAG_FREE = false;
        } else {
            this.flNC_FLAG_FREE = true;
        }

        if (flString.charAt(6) == '0') {
            this.flNC_FLAG_PH = false;
        } else {
            this.flNC_FLAG_PH = true;
        }

        if (flString.charAt(5) == '0') {
            this.flNC_FLAG_CH = false;
        } else {
            this.flNC_FLAG_CH = true;
        }

        if (flString.charAt(4) == '0') {
            this.flNC_FLAG_RANGE_LIMIT = false;
        } else {
            this.flNC_FLAG_RANGE_LIMIT = true;
        }

        if (flString.charAt(3) == '0') {
            this.flNC_FLAG_NOSERIALLINK = false;
        } else {
            this.flNC_FLAG_NOSERIALLINK = true;
        }

        if (flString.charAt(2) == '0') {
            this.flNC_FLAG_TARGET_REACHED = false;
        } else {
            this.flNC_FLAG_MANUAL = true;
        }

        if (flString.charAt(1) == '0') {
            this.flNC_FLAG_GPS_OK = false;
        } else {
            this.flNC_FLAG_GPS_OK = true;
        }

        if (flString.charAt(0) == '0') {
            this.flNC_FLAG_FREE = false;
        } else {
            this.flNC_FLAG_FREE = true;
        }

    }

    void parseFlagsStatusFC1(String flString) {
        if (flString.charAt(7) == '0') {
            this.flFC_STATUS_MOTOR_RUN = false;
        } else {
            this.flFC_STATUS_MOTOR_RUN = true;
        }
        if (flString.charAt(6) == '0') {
            this.flFC_STATUS_FLY = false;
        } else {
            this.flFC_STATUS_FLY = true;
        }
        if (flString.charAt(5) == '0') {
            this.flFC_STATUS_CALIBRATE = false;
        } else {
            this.flFC_STATUS_CALIBRATE = true;
        }
        if (flString.charAt(4) == '0') {
            this.flFC_STATUS_START = false;
        } else {
            this.flFC_STATUS_START = true;
        }
        if (flString.charAt(3) == '0') {
            this.flFC_STATUS_EMERGENCY_LANDING = false;
        } else {
            this.flFC_STATUS_EMERGENCY_LANDING = true;
        }
        if (flString.charAt(2) == '0') {
            this.flFC_STATUS_LOWBAT = false;
        } else {
            this.flFC_STATUS_LOWBAT = true;
        }
        if (flString.charAt(1) == '0') {
            this.flFC_STATUS_VARIO_TRIM_UP = false;
        } else {
            this.flFC_STATUS_VARIO_TRIM_UP = true;
        }
        if (flString.charAt(0) == '0') {
            this.flFC_STATUS_VARIO_TRIM_DOWN = false;
        } else {
            this.flFC_STATUS_VARIO_TRIM_DOWN = true;
        }
    }

    void parseFlagsStatusFC2(String flString) {
        if (flString.charAt(7) == '0') {
            this.flFC_STATUS2_CAREFREE_ACTIVE = false;
        } else {
            this.flFC_STATUS2_CAREFREE_ACTIVE = true;
        }
        if (flString.charAt(6) == '0') {
            this.flFC_STATUS2_ALTITUDE_CONTROL_ACTIVE = false;
        } else {
            this.flFC_STATUS2_ALTITUDE_CONTROL_ACTIVE = true;
        }
        if (flString.charAt(5) == '0') {
            this.flFC_STATUS2_FAILSAFE_ACTIVE = false;
        } else {
            this.flFC_STATUS2_FAILSAFE_ACTIVE = true;
        }
        if (flString.charAt(4) == '0') {
            this.flFC_STATUS2_OUT1 = false;
        } else {
            this.flFC_STATUS2_OUT1 = true;
        }
        if (flString.charAt(3) == '0') {
            this.flFC_STATUS2_OUT2 = false;
        } else {
            this.flFC_STATUS2_OUT2 = true;
        }
        if (flString.charAt(2) == '0') {
            this.flFC_STATUS2_RES1 = false;
        } else {
            this.flFC_STATUS2_RES1 = true;
        }
        if (flString.charAt(1) == '0') {
            this.flFC_STATUS2_RES2 = false;
        } else {
            this.flFC_STATUS2_RES2 = true;
        }
        if (flString.charAt(0) == '0') {
            this.flFC_STATUS2_RES3 = false;
        } else {
            this.flFC_STATUS2_RES3 = true;
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

    public void setSessionId(int id) {
        this.sessionId = id;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    /**
     * @return the FlagsNC
     */
    public String getFlagsNC() {
        return FlagsNC;
    }

    /**
     * @param FlagsNC the FlagsNC to set
     */
    public void setFlagsNC(String FlagsNC) {
        this.FlagsNC = FlagsNC;
    }

    /**
     * @return the FcStatusFlags1
     */
    public String getFcStatusFlags1() {
        return FlagsStatusFC1;
    }

    /**
     * @param FcStatusFlags1 the FcStatusFlags1 to set
     */
    public void setFcStatusFlags1(String FcStatusFlags1) {
        this.FlagsStatusFC1 = FcStatusFlags1;
    }

    /**
     * @return the FcStatusFlags2
     */
    public String getFcStatusFlags2() {
        return FlagsStatusFC2;
    }

    /**
     * @param FcStatusFlags2 the FcStatusFlags2 to set
     */
    public void setFcStatusFlags2(String FcStatusFlags2) {
        this.FlagsStatusFC2 = FcStatusFlags2;
    }

    public String getBlTempList() {
        return blTempList;
    }

    public void setBlTempList(String blTempList) {
        this.blTempList = blTempList;
    }

    /**
     * @return the TargetLongitude
     */
    public double getTargetLongitude() {
        return TargetLongitude;
    }

    /**
     * @param TargetLongitude the TargetLongitude to set
     */
    public void setTargetLongitude(double TargetLongitude) {
        this.TargetLongitude = TargetLongitude;
    }

    /**
     * @return the TargetLatitude
     */
    public double getTargetLatitude() {
        return TargetLatitude;
    }

    /**
     * @param TargetLatitude the TargetLatitude to set
     */
    public void setTargetLatitude(double TargetLatitude) {
        this.TargetLatitude = TargetLatitude;
    }

    /**
     * @param kopterHeading the kopterHeading to set
     */
    public void setKopterHeading(int kopterHeading) {
        this.kopterHeading = kopterHeading;
    }

    /**
     * @param kopterAltitude the kopterAltitude to set
     */
    public void setKopterAltitude(int kopterAltitude) {
        this.kopterAltitude = kopterAltitude;
    }

    /**
     * @param kopterLatitude the kopterLatitude to set
     */
    public void setKopterLatitude(double kopterLatitude) {
        this.kopterLatitude = kopterLatitude;
    }

    /**
     * @param kopterLongitude the kopterLongitude to set
     */
    public void setKopterLongitude(double kopterLongitude) {
        this.kopterLongitude = kopterLongitude;
    }

    /**
     * @param homeLatitude the homeLatitude to set
     */
    public void setHomeLatitude(double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    /**
     * @param homeLongitude the homeLongitude to set
     */
    public void setHomeLongitude(double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    /**
     * @param kopterErrorCode the kopterErrorCode to set
     */
    public void setKopterErrorCode(int kopterErrorCode) {
        this.kopterErrorCode = kopterErrorCode;
    }

    /**
     * @param gsmSignalStrength the gsmSignalStrength to set
     */
    public void setGsmSignalStrength(int gsmSignalStrength) {
        this.gsmSignalStrength = gsmSignalStrength;
    }

    /**
     * @param kopterVoltage the kopterVoltage to set
     */
    public void setKopterVoltage(int kopterVoltage) {
        this.kopterVoltage = kopterVoltage;
    }

    /**
     * @param gpsSatCount the gpsSatCount to set
     */
    public void setGpsSatCount(int gpsSatCount) {
        this.gpsSatCount = gpsSatCount;
    }

    /**
     * @param batteryCurrent the batteryCurrent to set
     */
    public void setBatteryCurrent(double batteryCurrent) {
        this.batteryCurrent = batteryCurrent;
    }

    /**
     * @param batteryCapacity the batteryCapacity to set
     */
    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    /**
     * @param kopterSpeed the kopterSpeed to set
     */
    public void setKopterSpeed(double kopterSpeed) {
        this.kopterSpeed = kopterSpeed;
    }

    /**
     * @param kopterRcSignal the kopterRcSignal to set
     */
    public void setKopterRcSignal(int kopterRcSignal) {
        this.kopterRcSignal = kopterRcSignal;
    }

    /**
     * @param kopterVario the kopterVario to set
     */
    public void setKopterVario(double kopterVario) {
        this.kopterVario = kopterVario;
    }

    /**
     * @param kopterId the kopterId to set
     */
    public void setKopterId(int kopterId) {
        this.kopterId = kopterId;
    }

}
