package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.RespawnManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RespawnCommand extends UHCCommand{
	
	private RespawnManager rm;

	public RespawnCommand(RespawnManager rm) {
		super("respawn", "/respawn <player>");
		
		this.rm = rm;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.respawn")){
				if(args.length == 1){
					
					Player target = Bukkit.getPlayer(args[0]);
					String name = target.getName();

					if(target != null && target.isOnline()){
						if(rm.invs.containsKey(target.getName())){
							rm.respawn(target);
							sendMessage(name);
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + name + " can't be respawned because he didn't played the game.");
						}
					}else{
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
						if(rm.invs.containsKey(name)){
							rm.readyToBeRespawned.add(name);
							sendMessage(name);
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + name + " can't be respawned because he didn't played the game.");
						}
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /respawn <player>");
				}
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private void sendMessage(String s){
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(pls.hasPermission("uhc.respawn.alert")){
				pls.sendMessage("§3[StaffOnly] " + s + " has been respawned !");
			}
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
