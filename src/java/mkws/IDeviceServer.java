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
    //-1 ise db erişim hatası. -2 ya da -3 ise kayd? yok ancak yine de kay?t edilemedi.

    int sendStatus(String jsonStatus); 
    //gelecek olan json KopterStatus s?n?f?ndaki gibi olmal?d?r.
    //dönüş 1 ise başarılı işlem gerçekleştimiştir; dönüş 0 ise data kaydedilmemiştir.
    //dönüş -1 ise db ba?lantısında sıkıntı bulunmaktad?r.

    String getTask(int deviceID);
    //yalnizca mk tarafindan kullanilir. koptere iletilecek yeni bir 
    //gorev varsa json formatinda cevap donulur.
    //-2 gelirse söz konusu kopter ID için bir konum bilgisi yoktur.
    //-1 db connection Error

}
