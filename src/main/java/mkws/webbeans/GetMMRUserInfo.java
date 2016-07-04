/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.webbeans;

import com.google.gson.Gson;
import mkws.Model.MMRUser;
import mkws.ServerEngine;

/**
 *
 * @author onurerden
 */
public class GetMMRUserInfo extends MMRUser {

    private MMRUser mmrUser;
    private int userId=-1;

    
    
    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId=userId;
        
        ServerEngine server = new ServerEngine();
        Gson gson = new Gson();
        MMRUser user;
        try {
            user = gson.fromJson(server.getMMRUserInfo(userId),MMRUser.class);
            this.setEmail(user.getEmail());
            this.setDisplay_name(user.getDisplay_name());
            this.setGender(user.getGender());
            this.setFirst_name(user.getFirst_name());
            this.setLast_name(user.getLast_name());
            this.setCommunication(user.getCommunication());
            this.setUser_name(user.getUser_name());
            this.setHeight(user.getHeight());
            this.setHobbies(user.getHobbies());
            this.setId(user.getId());
            this.setTime_zone(user.getTime_zone());
            this.setDate_joined(user.getDate_joined());
            this.setDisplay_measurement_system(user.getDisplay_measurement_system());
            this.setWeight(user.getWeight());
            this.setPhotoLinks(user.getPhotoLinks());
            this.setLinks(user.getLinks());
            this.setGoal_statement(user.getGoal_statement());
            
            
            
            
        } catch (Exception ex) {
            user = null;
        }
        
    }
    
    public MMRUser getMmrUser(){
        return this.mmrUser;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

}
