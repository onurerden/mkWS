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

    //String server = "127.8.64.130:3306";
      String server = "localhost:3306";
    String database = "mk";
//    String dbUserName = "adminGStcLXX";
//    String dbPassword = "Wc_flbnyklZt";
    private String dbUserName = "onur";
    private String dbPassword = "19861986";

    public String getMysqlConnectionString() {
        String connectionString = new String();
        connectionString = "jdbc:mysql://" + server + "/" + database;
        return connectionString;
    }
    public String getDbUserName(){
        return this.dbUserName;
    }
    public String getDbPassword(){
        return this.dbPassword;
    }
}
