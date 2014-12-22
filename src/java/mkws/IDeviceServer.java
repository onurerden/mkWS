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
    //-2 mikrokopter kaydı yok, -3 mobile phone kaydı yok, -1 DB error

    int registerDevice(String uid, String name, String deviceType);
    //uid cihaz tekil numarasi maks 45 hane, name cihaz ismi
    //deviceType mk (mikrokopter) ya da mp (mobilephone)
    //-1 ise db erişim hatası. -2 ya da -3 ise kaydı yok ancak yine de kayıt edilemedi.

    int sendStatus(String jsonStatus);
    //gelecek olan json KopterStatus sınıfındaki gibi olmalıdır. (parametre adı jsonstatus)
    //dönüş 1 ise başarılı işlem gerçekleştimiştir; dönüş 0 ise data kaydedilmemiştir.
    //dönüş -1 ise db bağlantısında sıkıntı bulunmaktadır.

    String getTask(int deviceID);
    //yalnizca mk tarafindan kullanilir. koptere iletilecek yeni bir 
    //gorev varsa json formatinda cevap donulur.
    //-2 gelirse söz konusu kopter ID için bir konum bilgisi yoktur.
    //-1 db connection Error

    int sendFollowMeData(String jsonfollowme);
    //Yalnızca followMeDevice tarafından kullanılır.
    //FollowMeData sınıfındaki objeyi json olarak (parametre adı jsonfollowme) gonderir.
    //Başarılı gönderim halinde 0, hatalı json -2, db erişim hatası -1
    //

}
