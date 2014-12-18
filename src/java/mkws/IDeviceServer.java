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
public interface IDeviceServer {

    int touchServer(String uid, String deviceType);
    //uid cihaz tekil numarasi maks 45 hane, deviceType mk (mikrokopter) ya da mp (mobilephone)
    //-2 mikrokopter kayd? yok, -3 mobile phone kayd? yok, -1 DB error

    int registerDevice(String uid, String name, String deviceType);
    //uid cihaz tekil numarasi maks 45 hane, name cihaz ismi
    //deviceType mk (mikrokopter) ya da mp (mobilephone)
    //-1 ise db eriºim hatas?. -2 ya da -3 ise kayd? yok ancak yine de kay?t edilemedi.

    int sendStatus(String jsonStatus);

    String getTask(int deviceID);
    //yalnizca mk tarafindan kullanilir. koptere iletilecek yeni bir 
    //gorev varsa json formatinda cevap donulur.
    //-2 gelirse söz konusu kopter ID için bir konum bilgisi yoktur.

}
