/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mkws.Model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author oerden
 */
public class MKMission {
    public ArrayList<Waypoint> waypoints;
    public String name;
    public String comments;
    public Timestamp datetime;
    public int mkId; //0 for default
    
}
