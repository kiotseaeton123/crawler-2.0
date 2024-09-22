package org.winnie.utils;

public class User {
    private String username;
    private String userlink;
    private boolean isanom;

    /**
     * 
     * @param username - wiki user name
     * @param userlink - path to user contribution page
     * @param isanom   - is anonymous user, user name is ip address
     */
    public User(String username, String userlink) {
        this(username, userlink, false);
    }

    public User(String username, String userlink, boolean isanom) {
        this.username = username;
        this.userlink = userlink;
        this.isanom = isanom;
    }

    /**
     * attribute getters
     * 
     * @return
     */
    public String getUserName() {
        return this.username;
    }

    public String getUserLink() {
        return this.userlink;
    }

    public boolean isAnom() {
        return this.isanom;
    }

}