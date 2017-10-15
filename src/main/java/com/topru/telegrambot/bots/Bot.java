/*
 *  Class created by Topru
 *  My GitHub https://github.com/Topru333
 *  For any questions message me by telegram Topru Everywhere
 */
package com.topru.telegrambot.bots;

import java.util.Map;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.topru.telegrambot.mysql.data.User;

/**
 *
 * @author topru
 */
public abstract class Bot extends TelegramLongPollingBot  {
	private final String token;
	
	public Bot(String token) {
		this.token = token;
	}
	
    final static Logger logger = Logger.getLogger(Bot.class);
    
    
    protected void command_line(Message message){
        if(message== null || !message.hasText()||message.getText().charAt(0) != '/'){
            return;
        }
        
        if(message.getChat().isSuperGroupChat()){
            group_commands(message);
        }
        else if(message.getChat().isUserChat()){
            private_commands(message);
        }
    }
    
    
    protected abstract void reply_commands(CallbackQuery query);
    protected abstract void group_commands(Message message);
    protected abstract void private_commands(Message message);
    public abstract Map<Integer,User> getUsers();
    
    
    @Override
    public String getBotToken() {
        return token;
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(update.hasCallbackQuery()){
            reply_commands(update.getCallbackQuery());
        }
        logger.info(update.getMessage().getFrom().getFirstName() + " : " + update.getMessage().getText());
        command_line(message);
    }
    
    protected void sendMsg (Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        
        try{
            sendMessage(sendMessage);
        }
        catch(TelegramApiException e){
             
        }
    }
    protected void sendMsg (String chat_id, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chat_id);
        sendMessage.setText(text);
        
        try{
            sendMessage(sendMessage);
        }
        catch(TelegramApiException e){
             
        }
    }
    
    @Override
    public String getBotUsername() {
        return null;
    }
    
}
