/*
 *  Class created by Topru
 *  My GitHub https://github.com/Topru333
 *  For any questions message me by telegram Topru Everywhere
 */
package com.topru.telegrambot.mysql;

import com.topru.telegrambot.mysql.data.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 *
 * @author topru
 */
public class SqlManager {
    final static Logger logger = Logger.getLogger(SqlManager.class);
 	// JDBC driver name and database URL
    protected static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    protected static final String DB_URL = "jdbc:mysql://localhost:3306/sys";

    //  Database credentials
    protected static final String USER = "root";
    protected final String PASS;
    
    private Connection conn = null;
    private Statement stmt = null;
    protected ResultSet rs = null;
    
    protected SimpleDateFormat reg_date_format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    
    
    public SqlManager(String password){
    	PASS = password;
    }
    
    
    public synchronized Map<Integer, User> getUsers(){
        ResultSet rs = getResult("Select * from sys.Users");
        Map<Integer,User> users = new HashMap<>();
        
        if(rs == null){
            return users;
        }
        
        try{
            while (rs.next()) {
                users.put(rs.getInt(1),new User(rs.getInt(1),rs.getString(3),rs.getInt(4)));
            }
            rs.close();
        }
        catch(Exception e){
            logger.warn(e.getMessage());
        }
        return users;
    }
    
    protected enum UserCategory {
        User(0),
        Admin(1);

        private int value;
        
        public int getValue(){
            return value;
        }
        
        UserCategory(int value) {
            this.value = value;
        }
    }
    
    
    
    
    public ResultSet getResult(String Query){
        try {
            if(stmt.execute(Query)){
                return stmt.getResultSet();
            }
            else{
                return null;
            }
        } catch (SQLException ex) {
            logger.warn("Can't take data from sql!");
            return null;
        }
    }
    
    protected synchronized void connect(){

        try 
        {
        	Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", USER, PASS);
            stmt = conn.createStatement();
            logger.info("Connected");
        } 
        catch (SQLException ex) 
        {
            logger.error("SQLException: " + ex.getMessage());
            logger.error("SQLState: " + ex.getSQLState());
            logger.error("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException ex) {
            logger.error("Error with jdbc driver!");
        }
        
    }
    
    protected synchronized void disconnect(){
    	
    	try{
            rs.close();
        }
        catch( Exception e ){
            logger.error("Error with closing ResultSet!");
        }
    	
        try{
            stmt.close();
        }
        catch( Exception e ){
            logger.error("Error with closing Statement!");
        }
        
        try{
        }
        catch( Exception e ){
            logger.error("Error with closing Connection!");
        }
        logger.info("Disconnected");
    }

    
    
    
}
