package timer;

import java.util.TimerTask;

import com.topru.telegrambot.bots.Bot;
import com.topru.telegrambot.bots.WakfuRpBot;

public abstract class DataUpdate extends TimerTask{
	
	protected final Bot bot;
	
	public DataUpdate(Bot bot) {
		this.bot = bot;
	}
}
