/*
 *  Class created by Topru
 *  My GitHub https://github.com/Topru333
 *  For any questions message me by telegram Topru Everywhere
 */
package com.topru.telegrambot.mysql;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.topru.telegrambot.mysql.data.RpUser;
import com.topru.telegrambot.mysql.data.User;

/**
 *
 * @author topru
 */
public class RpManager extends  SqlManager{
	
	public RpManager(String password) {
		super(password);
	}

	@Override
	public synchronized Map<Integer,User> getUsers(){
		connect();
        rs = getResult("Select main_table.name , join_table.nick , join_table.race , join_table.lvl ,  main_table.telegram_id , main_table.is_admin ,  join_table.exp from sys.Users as main_table left join sys.roleplay_players as join_table on main_table.telegram_id = join_table.telegram_id");
        Map<Integer,User> users = new HashMap<>();
        
        if(rs == null){
            return users;
        }
        
        try{
            while (rs.next()) {
                users.put(rs.getInt(5),new RpUser(rs.getInt(5),rs.getString(1),rs.getInt(6),rs.getString(3), rs.getInt(4), rs.getInt(7)));
            }
            disconnect();
        }
        catch(Exception e){
            logger.warn(e.getMessage());
        }
        return users;
    }
    
    public synchronized void registerUser(String name,String nick,String race,int id){
    	RuntimeSqlExecutor.putQuary(" Insert into sys.Users (name, registration_time, is_admin, telegram_id)" + " Values ('" + name + "', current_timestamp() , " + UserCategory.User.getValue() + ", "+ id + ")");
    	RuntimeSqlExecutor.putQuary(" Insert into sys.roleplay_players (nick,race,lvl,telegram_id)" + " Values ('" + nick + "', '" + race + "'," + 0 + ", "+ id + ")");
    }
    public synchronized void deleteUser(int id){
    	RuntimeSqlExecutor.putQuary(" DELETE FROM sys.roleplay_players WHERE roleplay_players.telegram_id = "+id);
    	RuntimeSqlExecutor.putQuary(" DELETE FROM sys.Users WHERE Users.telegram_id = "+id);
    }
    
}
