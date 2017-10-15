/*
 *  Class created by Topru
 *  My GitHub https://github.com/Topru333
 *  For any questions message me by telegram Topru Everywhere
 */
package com.topru.telegrambot.mysql.data;

/**
 *
 * @author topru
 */
public class User {
    private final int id;
    private final String name;
    private final int isadmin;
    
    public User(int id,String name,int isadmin){
        this.id = id;
        this.name = name;
        this.isadmin = isadmin;
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }

    public int isAdmin(){
        return isadmin;
    }
    
}
