/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crons;

import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
public class HourlyJob implements Runnable {

@Override
public void run() {
    // Do your hourly job here.
//    System.out.println("Hourly Job triggered by scheduler");
    ServerEngine server = new ServerEngine();
    server.clearifyNotEndedRoutes();
  }
}