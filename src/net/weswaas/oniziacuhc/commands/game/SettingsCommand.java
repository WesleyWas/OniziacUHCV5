package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.gui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SettingsCommand extends UHCCommand{
	
	private Settings settings;
	private Game game;
	private GUIManager gui;

	public SettingsCommand(Settings settings, Game game, GUIManager gui) {
		super("settings", "/settings set <value> <true | false | int | string>");
		
		this.settings = settings;
		this.game = game;
		this.gui = gui;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.settings.manage")){
				if(args.length > 2){
					if(args[0].equalsIgnoreCase("set")){
						
						if(args[1].equalsIgnoreCase("maxplayers")){
							if(!game.isOpen()){
								settings.setMaxPlayers(Integer.valueOf(args[2]));
								Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "The maximum amount of players in this game has been set to " + Integer.valueOf(args[2]) + ".");
								return true;
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cMax players can't be managed once the game is open.");
							}
						}
						
						
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /settings set <value> <true | false | int | string>");
					}
				}else if(args.length == 0){
					gui.getGUI("Settings").open(p);
				}
			}
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		List<String> toReturn = new ArrayList<String>();
		
		if(args.length == 1){
			toReturn.add("set");
		}else if(args.length == 2){
			toReturn.add("maxplayers");
		}
		
		return null;
	}

}
