package net.weswaas.oniziacuhc.gui;

import org.bukkit.entity.Player;

public abstract class GUI {
	
	private String name;
	private String description;
	
	public GUI(String name, String description) {
		
		this.name = name;
		this.description = description;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public abstract void create();
	
	public abstract void fill(Player p);
	
	public abstract void open(Player p);

}
