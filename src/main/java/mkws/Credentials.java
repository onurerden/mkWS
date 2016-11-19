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
public class Credentials {

    String server = "127.5.211.2:3306"; //mk
 //   String server = "127.2.57.130:3306";//mknew
  //  String server = "localhost:3306";
//    String server = "onur-mac-mini.local:3306";
    String database = "mk";
//    String dbUserName = "adminGStcLXX";
//    String dbPassword = "Wc_flbnyklZt";
    private String dbUserName = "adminHr8UXvV";
    private String dbPassword = "UrtgKUvL3deC";
    
    private String jjwtKey = "3zciEFR2rf+beofy3rh0Ak/2L8NOLS0GGnNODeSBNUI=";
    
    private String mmrClientId= "kd8sw2eft3bmy3hft7x9yezqgfhwezcz";
    private String mmrClientSecret = "smAfN8CYndqQSuUMR5p8Tt4pNPkuhGv5DT6pns3B3sW";

//    private String mmrClientIdForWeb= "48tedwecj3aum5uewqyprd6m9behtu3j";
//    private String mmrClientSecretForWeb = "yfmhmf9f8szPYtraVu8DtwZtyFnnVHUr5Teqksb7f7z";
    
     private String mmrClientIdForWeb= "ewekuaubbgj38f4qtabjhknphhhp5sxb";
    private String mmrClientSecretForWeb = "fthrDs2EpSUtMWZxzDpxRBGEmYnCDj6sNWW2AuDFP7v";
  
    public String getMysqlConnectionString() {
        String connectionString = new String();
        connectionString = "jdbc:mysql://" + server + "/" + database +"?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        return connectionString;
    }
    public String getDbUserName(){
        return this.dbUserName;
    }
    public String getDbPassword(){
        return this.dbPassword;
    }

    /**
     * @return the mmrClientId
     */
    public String getMmrClientId() {
        return mmrClientId;
    }

    /**
     * @param mmrClientId the mmrClientId to set
     */
    public void setMmrClientId(String mmrClientId) {
        this.mmrClientId = mmrClientId;
    }

    /**
     * @return the mmrClientSecret
     */
    public String getMmrClientSecret() {
        return mmrClientSecret;
    }

    /**
     * @param mmrClientSecret the mmrClientSecret to set
     */
    public void setMmrClientSecret(String mmrClientSecret) {
        this.mmrClientSecret = mmrClientSecret;
    }

    /**
     * @return the mmrClientIdForWeb
     */
    public String getMmrClientIdForWeb() {
        return mmrClientIdForWeb;
    }

    /**
     * @param mmrClientIdForWeb the mmrClientIdForWeb to set
     */
    public void setMmrClientIdForWeb(String mmrClientIdForWeb) {
        this.mmrClientIdForWeb = mmrClientIdForWeb;
    }

    /**
     * @return the mmrClientSecretForWeb
     */
    public String getMmrClientSecretForWeb() {
        return mmrClientSecretForWeb;
    }

    /**
     * @param mmrClientSecretForWeb the mmrClientSecretForWeb to set
     */
    public void setMmrClientSecretForWeb(String mmrClientSecretForWeb) {
        this.mmrClientSecretForWeb = mmrClientSecretForWeb;
    }

    /**
     * @return the jjwtKey
     */
    public String getJjwtKey() {
        return jjwtKey;
    }
}
