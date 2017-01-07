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
public class Token {

    private int userId;
    private String subject;
    private String issuer;
    private ArrayList roles = new ArrayList();
    private boolean isAdmin ;
    private boolean isActivated;

    public void addRole(String role) {
        roles.add(role);
    }

    public ArrayList getRoles() {
        return this.roles;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @param issuer the issuer to set
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * @return the isAdmin
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin the isAdmin to set
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return the isActivated
     */
    public boolean isIsActivated() {
        return isActivated;
    }

    /**
     * @param isActivated the isActivated to set
     */
    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

}
