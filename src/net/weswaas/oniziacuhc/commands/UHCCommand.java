package net.weswaas.oniziacuhc.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class UHCCommand {
	
	private String name;
	private String usage;
	
	public UHCCommand(String name, String usage){
		this.name = name;
		this.usage = usage;
	}
	
	public String getUsage(){
		return this.usage;
	}
	
	public String getName(){
		return this.name;
	}
	
	public abstract boolean execute(CommandSender sender, String[] args);
	
	public abstract List<String> tabComplete(CommandSender sender, String[] args);

}
