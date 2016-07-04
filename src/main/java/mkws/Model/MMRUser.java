/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkws.Model;

import java.util.ArrayList;

/**
 *
 * @author onurerden
 */
public class MMRUser {

    private String last_name;
    private double weight;
    private double height;
    private int id;
    private String first_name;
    private String display_name;
    private String gender;
    private Communication communication;
    private String hobbies;
    private String date_joined;
    private String introduction;
    private String display_measurement_system;
    private String last_login;
    private String email;
    private String user_name;
    private String time_zone;
    private String goal_statement;
    private Links _links;
    private MMRUserProfilePhoto photoLinks;
    

    /**
     * @return the communication
     */
    public Communication getCommunication() {
        return communication;
    }

    /**
     * @return the _links
     */
    public Links getLinks() {
        return _links;
    }

    public String getMMRProfilePhoto(){
        return photoLinks._links.large.get(0).href;
    }
    
    /**
     * @param _links the _links to set
     */
    public void setLinks(Links _links) {
        this._links = _links;
    }

    /**
     * @return the photoLinks
     */
    public MMRUserProfilePhoto getPhotoLinks() {
        return photoLinks;
    }

    /**
     * @param photoLinks the photoLinks to set
     */
    public void setPhotoLinks(MMRUserProfilePhoto photoLinks) {
        this.photoLinks = photoLinks;
    }

    public class MMRUserProfilePhoto{
        private MMRProfilePhotoLinks _links;

        /**
         * @return the _links
         */
        public MMRProfilePhotoLinks getLinks() {
            return _links;
        }

        /**
         * @param _links the _links to set
         */
        public void setLinks(MMRProfilePhotoLinks _links) {
            this._links = _links;
        }
    }
    public class MMRProfilePhotoLinks{
        private ArrayList<HrefIdName> small;
        private ArrayList<HrefIdName> medium;
        private ArrayList<HrefIdName> large;
        private ArrayList<HrefIdName> self;
        private ArrayList<HrefIdName> documentation;

        /**
         * @return the small
         */
        public ArrayList<HrefIdName> getSmall() {
            return small;
        }

        /**
         * @param small the small to set
         */
        public void setSmall(ArrayList<HrefIdName> small) {
            this.small = small;
        }

        /**
         * @return the medium
         */
        public ArrayList<HrefIdName> getMedium() {
            return medium;
        }

        /**
         * @param medium the medium to set
         */
        public void setMedium(ArrayList<HrefIdName> medium) {
            this.medium = medium;
        }

        /**
         * @return the large
         */
        public ArrayList<HrefIdName> getLarge() {
            return large;
        }

        /**
         * @param large the large to set
         */
        public void setLarge(ArrayList<HrefIdName> large) {
            this.large = large;
        }

        /**
         * @return the self
         */
        public ArrayList<HrefIdName> getSelf() {
            return self;
        }

        /**
         * @param self the self to set
         */
        public void setSelf(ArrayList<HrefIdName> self) {
            this.self = self;
        }

        /**
         * @return the documentation
         */
        public ArrayList<HrefIdName> getDocumentation() {
            return documentation;
        }

        /**
         * @param documentation the documentation to set
         */
        public void setDocumentation(ArrayList<HrefIdName> documentation) {
            this.documentation = documentation;
        }
    }
    
    public class Links {

        public ArrayList<HrefIdName> stats;
        public ArrayList<HrefIdName> privacy;
        public ArrayList<HrefIdName> image;
        public ArrayList<HrefIdName> documentation;
        public ArrayList<HrefIdName> deactivation;
        public ArrayList<HrefIdName> user_achievements;
        public ArrayList<HrefIdName> friendships;
        public ArrayList<HrefIdName> workouts;
        public ArrayList<HrefIdName> self;
    }

    public class HrefIdName {

        public String href;
        public String id;
        public String name;
    }

    /**
     * @param communication the communication to set
     */
    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    /**
     * @return the hobbies
     */
    public String getHobbies() {
        return hobbies;
    }

    /**
     * @param hobbies the hobbies to set
     */
    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    /**
     * @return the date_joined
     */
    public String getDate_joined() {
        return date_joined;
    }

    /**
     * @param date_joined the date_joined to set
     */
    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    /**
     * @return the introduction
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @param introduction the introduction to set
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    /**
     * @return the display_measurement_system
     */
    public String getDisplay_measurement_system() {
        return display_measurement_system;
    }

    /**
     * @param display_measurement_system the display_measurement_system to set
     */
    public void setDisplay_measurement_system(String display_measurement_system) {
        this.display_measurement_system = display_measurement_system;
    }

    /**
     * @return the last_login
     */
    public String getLast_login() {
        return last_login;
    }

    /**
     * @param last_login the last_login to set
     */
    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the user_name
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * @param user_name the user_name to set
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * @return the time_zone
     */
    public String getTime_zone() {
        return time_zone;
    }

    /**
     * @param time_zone the time_zone to set
     */
    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    /**
     * @return the goal_statement
     */
    public String getGoal_statement() {
        return goal_statement;
    }

    /**
     * @param goal_statement the goal_statement to set
     */
    public void setGoal_statement(String goal_statement) {
        this.goal_statement = goal_statement;
    }

    public class Location {

        public String country;
        public String region;
        public String locality;
        public String address;
    }

    public class Communication {

        boolean promotions;
        boolean newsletter;
        boolean system_messages;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the display_name
     */
    public String getDisplay_name() {
        return display_name;
    }

    /**
     * @param display_name the display_name to set
     */
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

}
