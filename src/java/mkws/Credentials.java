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

    String server = "127.8.64.130:3306";
    //String server = "localhost:3306";
   // String server = "onur-mac-mini.local:3306";
    String database = "mk";
//    String dbUserName = "adminGStcLXX";
//    String dbPassword = "Wc_flbnyklZt";
    private String dbUserName = "onur";
    private String dbPassword = "19861986";
    
    private String mmrClientId= "kd8sw2eft3bmy3hft7x9yezqgfhwezcz";
    private String mmrClientSecret = "smAfN8CYndqQSuUMR5p8Tt4pNPkuhGv5DT6pns3B3sW";

    public String getMysqlConnectionString() {
        String connectionString = new String();
        connectionString = "jdbc:mysql://" + server + "/" + database +"?useUnicode=true&characterEncoding=UTF-8";
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
}
