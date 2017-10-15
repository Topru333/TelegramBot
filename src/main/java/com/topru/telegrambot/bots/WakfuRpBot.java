/*
 *  Class created by Topru
 *  My GitHub https://github.com/Topru333
 *  For any questions message me by telegram Topru Everywhere
 */
package com.topru.telegrambot.bots;



import com.topru.telegrambot.mysql.RpManager;
import com.topru.telegrambot.mysql.data.RpUser;
import com.topru.telegrambot.mysql.data.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;


/**
 *
 * @author topru
 */
public class WakfuRpBot extends Bot{
	
	public WakfuRpBot(String token, String pass){
		super(token);
	    this.sql = new RpManager(pass);
	    Users = sql.getUsers();
	    
	}
	    
    
    private final RpManager sql;
    
    private Map<Integer,User> Users = new HashMap<>();
    
    @Override
    public Map<Integer,User> getUsers(){
    	Map<Integer,User> map = new HashMap<>();
    	if(Users.size()>0)
    		map.putAll(Users);
    	return map;
    }
    
   
    @Override
    public void onUpdateReceived(Update update) {
    	if(update.getMessage().getNewChatMembers()!=null) {
    		sendMsg(update.getMessage(), "Добро пожаловать!");
    		return;
    	}
    	if(update.getMessage().isSuperGroupMessage()) {
        	addExp(update.getMessage().getFrom().getId());
        }
        super.onUpdateReceived(update);
    }
    
    private void addExp(int id) {
    	if(Users.containsKey(id)) {
    		((RpUser)Users.get(id)).addExp();
    	}
    }
    
    @Override
    protected void reply_commands(CallbackQuery query) {
        if(query.getData().contains("/reply_register")){
            end_register_comand(query);
        }
    }
    
    @Override
    protected void group_commands(Message message) {
        String text = message.getText();
        if(text.contains("/help")){
            sendGroupHelp(message);
            
        }
        else{
            sendNonCommandReply(message);
            
        }
    }

    @Override
    protected void private_commands(Message message) {
        String text = message.getText();
        if(text.contains("/help")){
            sendPrivateHelp(message);
        }
        else if(text.contains("/register")){
            start_register_command(message);   
        }
        else if(text.contains("/deleteme")){
            deleteme_command(message);   
        }
        else if(text.contains("/myinfo")){
            if(Users.containsKey(message.getFrom().getId())) {
            	sendMsg(message,"Ваш уровень " + ((RpUser)Users.get(message.getFrom().getId())).getLvl() );
            }
            else {
            	sendMsg(message,"Таких не знаем!");
            }
        }
        else{
            sendNonCommandReply(message);
            
        }        
    }
    
    
    
    
    ////////////////REGISTER///////////////////////////////
    Map<Integer, String> quaure = new HashMap<Integer, String>();
    
    protected void start_register_command(Message message){
        String text = message.getText();
        String[] parts = text.split(" ");
        if(parts.length != 2){
            sendMsg(message, "Нужно ввести /register игровой_ник");
        }
        quaure.put(message.getFrom().getId(), parts[1]);
        
        SendMessage new_msg = new SendMessage().setChatId(message.getChatId()).setText("Выбирай свою расу нубик");
        
        new_msg.setReplyMarkup(getRaceButtons());
        
        try {
            sendMessage(new_msg);
        } catch (TelegramApiException ex) {
           
        }
    }
    
    protected void end_register_comand(CallbackQuery query){
        if(quaure.containsKey(query.getFrom().getId())){
            sql.registerUser(query.getFrom().getFirstName(), quaure.get(query.getFrom().getId()), query.getData().split(" ")[1],query.getFrom().getId());   
            logger.info("We have one more player! His name : " + quaure.get(query.getFrom().getId()) + " and race : " + query.getData().split(" ")[1]);
            quaure.remove(query.getFrom().getId());
            EditMessageText new_message = new EditMessageText()
                        .setChatId(query.getMessage().getChatId())
                        .setMessageId(query.getMessage().getMessageId())
                        .setText("Ваш персонаж создан " + query.getFrom().getFirstName() +"!");
            try{
                editMessageText(new_message);
            }
            catch(Exception e){
            }
        }else{
            sendMsg(query.getMessage().getChatId().toString(), "Попробуйте еще раз c самого начала пожалуйста. Не вижу вашего игрового ника.");
        }
    }
    ///////////////////////////////////////////////////////
    
    protected void deleteme_command(Message message){
        sql.deleteUser(message.getFrom().getId());
        sendMsg(message, "Персонаж погиб в муках");
    }
    
    
    
    
    protected void sendNonCommandReply(Message message){
        sendMsg(message, "Неверная комманда. Попробуйте ввести /help для полной информации по возможным функциям.");
    }
    
    
    
    
    protected void sendGroupHelp(Message message){
        sendMsg(message, "Ничем не могу помочь!");
    }
    
  
    
    
    protected void sendPrivateHelp(Message message){
        sendMsg(message, "Если вы хотите зарегестрироваться введите комманду /register и через пробел ваш никнейм.\n Если хотите получить информацию о своем прогрессе /myinfo.\n Если же по каким либо причинам вам нужно удалить информацию введите /deleteme.");
    }
    
    @Override
    public String getBotUsername() {
        return "@Eliocube_bot";
    }

    

    
    private InlineKeyboardMarkup getRaceButtons() {
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline0 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        
        rowInline0.add(new InlineKeyboardButton().setText("Панда") .setCallbackData("/reply_register " + RpRaces.Panda.getValue()));
        rowInline0.add(new InlineKeyboardButton().setText("Иоп")   .setCallbackData("/reply_register " + RpRaces.Iop.getValue()));
        rowInline0.add(new InlineKeyboardButton().setText("Садида").setCallbackData("/reply_register " + RpRaces.Sadida.getValue()));
        
        rowInline1.add(new InlineKeyboardButton().setText("Кра")     .setCallbackData("/reply_register " + RpRaces.Cra.getValue()));
        rowInline1.add(new InlineKeyboardButton().setText("Енутроф") .setCallbackData("/reply_register " + RpRaces.Enu.getValue()));
        rowInline1.add(new InlineKeyboardButton().setText("Срам")    .setCallbackData("/reply_register " + RpRaces.Sram.getValue()));
        
        rowInline2.add(new InlineKeyboardButton().setText("Сакриер")    .setCallbackData("/reply_register " + RpRaces.Sacrier.getValue()));
        rowInline2.add(new InlineKeyboardButton().setText("Фека")       .setCallbackData("/reply_register " + RpRaces.Feca.getValue()));
        rowInline2.add(new InlineKeyboardButton().setText("Маскарайдер").setCallbackData("/reply_register " + RpRaces.Masquerader.getValue()));
        
        rowInline3.add(new InlineKeyboardButton().setText("Фогернаут").setCallbackData("/reply_register " + RpRaces.Foggernaut.getValue()));
        rowInline3.add(new InlineKeyboardButton().setText("Экафлип")  .setCallbackData("/reply_register " + RpRaces.Ecaflip.getValue()));
        rowInline3.add(new InlineKeyboardButton().setText("Элиатроп") .setCallbackData("/reply_register " + RpRaces.Eliatrop.getValue()));
        
        rowsInline.add(rowInline0);
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }


    public enum RpRaces {
        Panda("panda"),
        Iop("iop"),
        Cra("cra"),
        Enu("enu"),
        Sacrier("sacrier"),
        Feca("feca"),
        Sadida("sadida"),
        Sram("sram"),
        Foggernaut("foggernaut"),
        Ecaflip("ecaflip"),
        Eliatrop("eliatrop"),
        Masquerader("masq");
        
        public static boolean contains(String value){
            for(RpRaces race : RpRaces.values()){
                if(race.value.equals(value)){
                    return true;
                }
            }
            return false;
        }
        
        private final String value;
        
        public String getValue(){
            return value;
        }
        
        RpRaces(String value) {
            this.value = value;
        }
    }
    
    
}
