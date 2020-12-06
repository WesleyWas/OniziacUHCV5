package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import java.util.List;

public class HealthCommand extends UHCCommand{

	public HealthCommand() {
		super("health", "/health <player>");
	
		
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(args.length == 1){
				
				Player target = Bukkit.getPlayer(args[0]);
				if(target != null && target.isOnline()){
					
					Damageable d = target;
					double health = (int) d.getHealth();
					
					p.sendMessage(ChatColor.GREEN + target.getPlayerListName() + " §8» §3" + health / 2 + "§4❤");
				}
				
			}else{
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + "Synthax error. Please try with /health <player>");
			}
			
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
