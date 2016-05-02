/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mkws.Model;

/**
 *
 * @author oerden
 */
public class Waypoint {
    public double targetLat=0;
    public double targetLon=0;
    public double targetHeight=0; //in meters
    public double targetSpeed=1; //0,1 m/s,  0 for max speed 1-247, 248-255 for POTI's
    public double targetAngle=0; //0 for inactive, -1 for POI, -1/-2/-3... for P1, P2, P3
    public int waypointEvent=0; //event to be done at target
    public int radius=0; //target radius in meters
    public int waitingTime=0; //waiting time at target
    public int autoTrigger=0; //triger out1 for 10ms for every X meters.
    public double vario=0; //255 for automatic, 0-254 for 0,1 m/s climb/descend
    public boolean isPOI =false;
    public int missionId = 0;
    //public double targetMKId=0;

}
