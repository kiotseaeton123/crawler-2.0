package org.winnie.utils;

/**
 * class represents site user
 * @author winnie
 */
public class User {
    private String username;
    private String userlink;
    private boolean isanom;


    /**
     * constructor for general user
     * @param username - user username
     * @param userlink - user userlink
     */
    public User(String username, String userlink) {
        this(username, userlink, false);
    }

    /**
     * constructor with boolean flag for anonymous users
     * 
     * @param username - wiki user name
     * @param userlink - path to user contribution page
     * @param isanom   - is anonymous user, user name is ip address
     */
    public User(String username, String userlink, boolean isanom) {
        this.username = username;
        this.userlink = userlink;
        this.isanom = isanom;
    }

    /**
     * username getter
     * 
     * @return String
     */
    public String getUserName() {
        return this.username;
    }

    /**
     * user link getter
     * @return String
     */
    public String getUserLink() {
        return this.userlink;
    }

    /**
     * is anonymous user, getter
     * @return boolean
     */
    public boolean isAnom() {
        return this.isanom;
    }

}