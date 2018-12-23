/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

/**
 *
 * @author onurerden
 */
public class MKHelper {
   public double distanceBetweenPoints(double latFirst, double lngFirst, double latSecond, double lngSecond) {

        double R = 6371.0; // metres
        double d;
        double fiFirst = Math.toRadians(latFirst);
        double fiSecond = Math.toRadians(latSecond);
        double deltaFi = Math.toRadians(latSecond - latFirst);
        double deltaLambda = Math.toRadians(lngSecond - lngFirst);

        double a = Math.sin(deltaFi / 2) * Math.sin(deltaFi / 2)
                + Math.cos(fiFirst) * Math.cos(fiSecond)
                * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        d = R * c;
        return d;
    }   
}
