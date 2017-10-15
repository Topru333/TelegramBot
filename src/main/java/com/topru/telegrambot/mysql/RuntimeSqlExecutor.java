package com.topru.telegrambot.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

import org.apache.log4j.Logger;

public class RuntimeSqlExecutor implements Runnable{

	final static Logger logger = Logger.getLogger(RuntimeSqlExecutor.class);
	
	// JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/sys";

    //  Database credentials
    static final String USER = "root";
    private final String PASS;
    
    private Connection conn = null;
    private Statement stmt = null;
    protected ResultSet rs = null;
	
    
    public RuntimeSqlExecutor(String pass) {
    	PASS = pass;
    }
    
	private static Stack<String> quaries = new Stack<>();
    public static void putQuary(String value) {
    	quaries.push(value);
    }
    
	@Override
	public void run() {
		while(true) {
			if(!quaries.isEmpty()) {
				String quary = quaries.pop();
				if(quary.equals("stop")) {
					throw new RuntimeException("Stack of quaries was stoped!");
				}
				execute(quary);
			}
		}
		
	}
	
	public synchronized boolean execute(String query){
        try{
        	connect();
        	stmt.execute(query); 
        	logger.info("Executed");
            return true;
        }
        catch (Exception e) {
            return false;
        }
        finally {
        	try{
                stmt.close();
            }
            catch( Exception e ){
                logger.error("Error with closing Statement!");
            }
            
            try{
                conn.close();
                logger.info("Disconnected");
            }
            catch( Exception e ){
                logger.error("Error with closing Connection!");
            }
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
