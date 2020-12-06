package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VisitCommand extends UHCCommand{

	public VisitCommand() {
		super("visit", "/visit");
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.visit")){
				if(args.length == 0){
					
					if(p.getWorld() == Bukkit.getWorld("lobby")){
						Location loc = new Location(Bukkit.getWorld("world"), 0, 100, 0);
						p.teleport(new Location(Bukkit.getWorld("world"), 0, Bukkit.getWorld("world").getHighestBlockYAt(loc) + 5, 0));
					}else if(p.getWorld() == Bukkit.getWorld("world")){
						Location loc = new Location(Bukkit.getWorld("lobby"), 0, 100, 0);
						p.teleport(new Location(Bukkit.getWorld("lobby"), 0, Bukkit.getWorld("lobby").getHighestBlockYAt(loc) + 1, 0));
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /visit");
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
