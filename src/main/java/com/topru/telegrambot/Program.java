/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topru.telegrambot;

import com.topru.telegrambot.bots.WakfuRpBot;
import com.topru.telegrambot.mysql.RuntimeSqlExecutor;

import timer.RpUserUpdate;

import java.util.Timer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.generics.BotSession;

/**
 *
 * @author topru
 */
public class Program {
    final static Logger logger = Logger.getLogger(Program.class);
    
    public static void main(String args[])
    {
    	Options options = new Options();
    	Option optionPass = new Option("p", "pass", true, "password for sql root user"); 
    	optionPass.setArgs(1);
    	optionPass.setArgName("pass");
    	Option optionToken = new Option("t", "token", true, "bot's token"); 
    	optionToken.setArgs(1);
    	optionToken.setArgName("token");
    	options.addOption(optionPass).addOption(optionToken);
    	CommandLineParser cmdLinePosixParser = new PosixParser();// создаем Posix парсер
    	CommandLine commandLine;
    	
    	String pass = "";
    	String token = "";
    	
		try {
			commandLine = cmdLinePosixParser.parse(options, args);
			if(commandLine.hasOption("p")) {
	    		pass = commandLine.getOptionValue("p");
	    		token = commandLine.getOptionValue("t");
	    	}
		} catch (ParseException e1) {
		}
    	
    	
    	
    	
    	
        BotSession session = null;
        Timer timer = null;
        try{
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            WakfuRpBot bot = new WakfuRpBot(token,pass);
            session = telegramBotsApi.registerBot(bot);
            
            RpUserUpdate update = new RpUserUpdate(bot,pass);
            timer = new Timer();
            timer.schedule(update, 0, 60000);
            
            Thread executor = new Thread(new RuntimeSqlExecutor(pass), "executor");
            executor.start();
            
            System.in.read();
            timer.cancel();
            session.close();
            RuntimeSqlExecutor.putQuary("stop");
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }            
    }
    
    

}
