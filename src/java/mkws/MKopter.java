/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mkws;


import java.util.Date;

/**
 *
 * @author oerden
 */
public class MKopter {
    public String name = "";
    public int id = -1;
    public String uid = "";
    public boolean isActive = false;
    public Date latestTouch = new Date();
    public KopterStatus status = new KopterStatus();
    
    
    
}
