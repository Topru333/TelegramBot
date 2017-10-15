package timer;

import java.util.Map;
import com.topru.telegrambot.bots.WakfuRpBot;
import com.topru.telegrambot.mysql.RpManager;
import com.topru.telegrambot.mysql.RuntimeSqlExecutor;
import com.topru.telegrambot.mysql.data.RpUser;
import com.topru.telegrambot.mysql.data.User;

public class RpUserUpdate extends DataUpdate {
	
	private RpManager sql;
	
	public RpUserUpdate(WakfuRpBot bot, String pass) {
		super(bot);
		sql = new RpManager(pass);
	}

	@Override
	public void run() {
		Map<Integer, User> users = bot.getUsers();
		for(User user:users.values()) {
			RuntimeSqlExecutor.putQuary("Update sys.roleplay_players Set lvl = " + ((RpUser)user).getLvl()  + ", exp = " + ((RpUser)user).getExp() + " where telegram_id = " + ((RpUser)user).getId() );
		}
	}



}
