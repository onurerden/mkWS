/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws;

import mkws.Model.MKMission;

/**
 *
 * @author oerden
 */
public interface IDeviceServer {

    String touchServer(String uid, String deviceType);
    //uid cihaz tekil numarasi maks 45 hane, deviceType mk (mikrokopter) ya da mp (mobilephone)
    //KopterSession (json) sınıfı cinsinden dönüş yapılır.
    //KopterSession.deviceId: -2 mikrokopter kaydı yok, -3 mobile phone kaydı yok, -1 DB error
    //KopterSession.sessionId=-1 session oluşturulamadı.

    int registerDevice(String uid, String name, String deviceType);
    //uid cihaz tekil numarasi maks 45 hane, name cihaz ismi
    //deviceType mk (mikrokopter) ya da mp (mobilephone)
    //-1 ise db erişim hatası. -2 ya da -3 ise kaydı yok ancak yine de kayıt edilemedi.
    //401 status alınırsa token hatası

    int sendStatus(String jsonStatus);
    //gelecek olan json KopterStatus sınıfındaki gibi olmalıdır. (parametre adı jsonstatus)
    //dönüş 1 ise başarılı işlem gerçekleştimiştir; dönüş 0 ise data kaydedilmemiştir.
    //dönüş -1 ise db bağlantısında sıkıntı bulunmaktadır.

    String getKopterStatus(int deviceId);
    //Kopter'in son bilinen durumunu bildirir. hata çıkmazsa json formatında KopterStatus döner
    //-1 db hatası hatası. -2 Koptere ait durum bilgisi yok.

    String getTask(int deviceID);
    //yalnizca mk tarafindan kullanilir. koptere iletilecek yeni bir 
    //gorev varsa FollowMeData objesi json formatinda cevap donulur.
    //kopter için görev yoksa FollowMeData default değerler ile döner. (Lat0, long0 etc.) bu durumda
    //Kopter hedefe gönderilmemelidir.
    //-2 db connection Error
    //
    //GetTask içinde gelen veri :
    //      1 ise followme
    //      2 ise lookatme
    //      3 ise comehome

    //followme event'inde kopter'e  followme cihazının konumları Waypoint olarak gönderilir. aynı zamanda 
    //      bearing de hesaplanır.
    //lookatme event'inde kopterStatus'tan alınan pozisyon verileri Waypoint olarak geri gönderilir
    //          (böylece kopter pozisyonu korunur), bununla birlikte bearing hesaplanır ve gönderilir.
    //comehome event'inde koptere, kopterStatus'tan alınan home pozisyonu (enlem,boylam) Waypoint olarak
    //          gönderilir. Home'a göre bearing hesaplanır.
    int setTask(int kopterId, int followMeDeviceId);
    //Değerler ile ilgili açıklamalar getTask() metodu içerisinde yapılmıştır.

    int sendFollowMeData(String jsonfollowme);
    //Yalnızca followMeDevice tarafından kullanılır.
    //FollowMeData sınıfındaki objeyi json olarak (parametre adı jsonfollowme) gonderir.
    //Başarılı gönderim halinde 0, hatalı json -2, db erişim hatası -1

    String getFollowMeData(int deviceId);
    //deviceId'si verilen FollowMe cihazının bilgileri döndürülür.
    //deviceId 0 girilirse tüm followme cihazlarına ait bilgiler array olarak döndürülür.
    //SQL hatası -1

    int getRouteId(int deviceId, int type);
    //followMeDevice tarafından çağrılır. İlgili followMeDevice'ının session rotasını çıkartmak 
    //amacıyla kullanılır. Bu fonksiyon sonrasında dönen integer, FollowMeData'sındaki route değişkenine
    //girilmelidir. -1 ise db hatası, -2 ise deviceId parametresi Integer değil.
    //type 1 koşu, 2 cycle, 3 drive, 4 other.

    int sendLog(String logJson);
    //DeviceType DeviceTypes.java dosyasındaki gibi olacaktır. (MK ya da MP)
    //LogMessage.java cinsi obje json olarak gönderilir.
    //logLevel hata mesajları için 1, bilgi mesajları için 2, diğer mesajlar için 99 olacaktır.
    //logMessage için bir kısıtlama yoktur, istenen şey yazılabilir.
    //başarılı gönderim halinde 0, hatalı json -2, db erişim hatası -1 dönüş yapar.


    int endRoute(int routeId);
    //Rotanın sonuçlandırılması için followme aksiyonunun bitirilmesi ile çağırılan metoddur.
    //sonlandırılmak istenen rota gönderilir.
    //1: başarılı, -1 db erişim hatası ya da geçersiz rota id
    //-2 yetkisiz endRoute işlemi
    
        int endRoute(int routeId, double length);
    //Rotanın sonuçlandırılması için followme aksiyonunun bitirilmesi ile çağırılan metoddur.
    //sonlandırılmak istenen rota gönderilir.
    //1: başarılı, -1 db erişim hatası ya da geçersiz rota id
    //-2 yetkisiz endRoute işlemi
    
    int endRoute(int routeId, int duration);
    //Rotanın sonuçlandırılması için followme aksiyonunun bitirilmesi ile çağırılan metoddur.
    //sonlandırılmak istenen rota gönderilir.
    //1: başarılı, -1 db erişim hatası ya da geçersiz rota id
    //-2 yetkisiz endRoute işlemi
    int endRoute(int routeId, int duration, double length);
    //Rotanın sonuçlandırılması için followme aksiyonunun bitirilmesi ile çağırılan metoddur.
    //sonlandırılmak istenen rota gönderilir.
    //1: başarılı, -1 db erişim hatası ya da geçersiz rota id
    //-2 yetkisiz endRoute işlemi
    
    int endRoute (int routeId, boolean sendToMMR);
    //Rotanın sonuçlandırılması için followme aksiyonunun bitirilmesi ile çağırılan metoddur.
    //sonlandırılmak istenen rota gönderilir.
    //1: başarılı, -1 db erişim hatası ya da geçersiz rota id
    //-2 MMR hatası
    
    String ping();
    //Sistemin ayakta olup olmadığının tespiti için kullanılan metoddur.
    // ".../Ping" URL'i ile sorgulanır (P büyük), yanıt olarak "Pong" gelir.
    //Başka bir çıktısı yoktur.
    
    int saveGPXContent(String gpxString, String uid);
    //daha önceden kaydedilmiş bir gpx dosyası içeriğini sistemdeki rotaların arasına kaydeder.
    //URL .../SaveGpxFile'dır.
    //başarılı ise 1, session alınamaz ise -1, device kayıtlı değilse-2, geçersiz gpx dosyası için -3 döner 
    
    int saveMMRauthorizationCodeForDevice(int deviceId, String deviceType, String code);
    //MapMyRide için authorization code'unu sunucuya kaydetmek için kullanılır.
    //deviceId cihazın mkWS üzerindeki id'si,
    //deviceType mk (mikrokopter) ya da mp (mobilephone),
    //code mapmyride oauth 2 sunucusuna login olduktan sonra dönüşü yapılan code'dur.
    //
    //mapmyride oauth sunucusuna aşağıdaki linkten giriş yapılacak:
    //https://www.mapmyfitness.com/v7.1/oauth2/uacf/authorize/?client_id=XXXXXXXXXXXX&response_type=code
    //XXXXXXXXX client key, Onur Erden bunu soranlara verecek
    //giriş başarılı olursa "followme://response?state=&code=XXXXXXXXXXX"
    //uri'si ile dönüş yapılacak. program bu uri'yi yakalayarak code'u parse edecek ve bu methodla sunucuya iletecek.
    //parse işlemi android için aşağıdaki linkte örnekler var
    //http://stackoverflow.com/questions/2448213/how-to-implement-my-very-own-uri-scheme-on-android
    //başarılı ise 1, hata alınırsa -1 döner
    
    int saveMission(MKMission mission, String name, String comments);
    //MKMission dosyası JSON'a serialize edilerek gönderilir
    //başarılı ise 1, -1 ise db hatası, -2 ise diğer hatalar, -3 ise mission parse error
    //-4 ise waypoint yok
    
    String getMission(int kopterId);
    //kopterId için kayıt edilen en son görevi JSON serialize ederek gönderir.
    //-3 ise yeni görev yok demektir.
    
    int sendFollowMeDataToMabeyn(String mabeyn, String ip, int userid);
    //Send followmedata'nın datayı işlemeden mabeyn veritabanına atan metodudur.
    //Yalnızca followMeDevice tarafından kullanılır.
    //FollowMeData sınıfındaki objeyi json olarak (parametre adı jsonfollowme) gonderir.
    //Başarılı gönderim halinde 0, hatalı json -2, db erişim hatası -1
    
    
}
