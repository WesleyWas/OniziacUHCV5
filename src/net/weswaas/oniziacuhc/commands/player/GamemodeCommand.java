package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GamemodeCommand extends UHCCommand{

	public GamemodeCommand() {
		super("gamemode", "/gamemode <0 | 1 | 2> [<player>]");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.gamemode")){
				if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
					
					if(args.length == 2){
						
						Player target = Bukkit.getPlayer(args[1]);
						if(target != null && target.isOnline()){
							target.setGameMode(GameMode.getByValue(Integer.valueOf(args[0])));
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Gamemode " + Integer.valueOf(args[0]) + " has been set to " + target.getName() + ".");
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + args[1] + " is not online.");
						}
						
					}else if(args.length == 1){
						
						p.setGameMode(GameMode.getByValue(Integer.valueOf(args[0])));
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Your gamemode has been changed to gamemode " + Integer.valueOf(args[0]));
						
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /gamemode <0 | 1 | 2> [<player>]");
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlease do not change your gamemode while you're playing a game.");
				}
			}
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
