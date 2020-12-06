package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TPCommand extends UHCCommand{

	public TPCommand() {
		super("tp", "/tp <player> [<player>]");
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.teleport")){
				if(args.length == 1){
					
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null && target.isOnline()){
						p.teleport(target.getLocation());
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Teleported " + p.getName() + " to " + target.getPlayerListName());
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + args[0] + " is not online.");
					}
					
				}else if(args.length == 2){
					
					Player target1 = Bukkit.getPlayer(args[0]);
					Player target2 = Bukkit.getPlayer(args[1]);
					
					if(target1 != null && target1.isOnline()){
						if(target2 != null && target2.isOnline()){
							
							target1.teleport(target2);
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + args[0] + " has been teleported to " + args[1] + ".");
							
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + args[1] + " is not online.");
						}
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + args[0] + " is not online.");
					}
					
				}else if(args.length == 4){
					
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null && target.isOnline()){
						
						int x = Integer.valueOf(args[1]);
						int y = Integer.valueOf(args[2]);
						int z = Integer.valueOf(args[3]);
						
						target.teleport(new Location(Bukkit.getWorld("world"), x, y, z));
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getDisplayName() + " has been teleport to: " + x + ", " + y + ", " + z + ".");
						target.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You've been teleported to " + x + ", " + y + ", " + z + ".");
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + args[0] + " is not online.");
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /tp <player> [<player> | <coords>]");
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
