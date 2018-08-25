/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crons;

import java.util.ArrayList;
import mkws.LogMessage;
import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
public class MinJob implements Runnable {

    @Override
    public void run() {
        // Do your minutely job here.
//        System.out.println("Min Job triggered by scheduler.");
        processRoutes();
     
        
    }
    
    private void processRoutes(){
        ServerEngine server = new ServerEngine();
        ArrayList<Integer> routeList = server.getRoutesToBeProcessed();
        
        for(Integer i : routeList){
            String json = server.readFromMabeyn(i);
            if(server.sendFollowMeData(json)==0){
            server.markRouteAsProcessed(i);
            System.out.println("Route in mabeyn has been processed. Id is " + i);
            }else{
            server.markRouteAsFailed(i);
            LogMessage msg = new LogMessage();
            msg.setLogLevel(2);
            msg.setLogMessage("Route in mabeyn cannot be processed. Id is " +i);
            server.sendLog(msg);
            }
        }
    }
    
    
}
