package com.topru.telegrambot.mysql.data;

public class RpUser extends User {
	
	private final String race;
	private int lvl;
	private int exp;
	
	public RpUser(int id, String name, int isadmin, String race,int lvl,int exp) {
		super(id, name, isadmin);
		this.race = race;
		this.lvl = lvl;
		this.exp = exp;
	}
	
	public String getRace() {
		return race;
	}
	
	public int getLvl() {
		return lvl;
	}
	
	public int getExp() {
		return exp;
	}
	
	private int getExpWall() {
		return 50 + lvl * lvl;
	}
	
	public void addExp() {
		exp += exp;
		if(exp>getExpWall()) {
			exp -= getExpWall();
			lvl+=1;
		}
	}
	
	public void addLvl(int value) {
		lvl += value;
	}
}
