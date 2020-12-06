package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import java.util.List;

public class HealCommand extends UHCCommand{

	public HealCommand() {
		super("heal", "/heal <player> [<life/20>]");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.heal")){
				if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
					if(args.length == 1){
						
						Player target = Bukkit.getPlayer(args[0]);
						if(target != null && target.isOnline()){
							Damageable d = target;
							d.setHealth(20);
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getName() + " has been healed to full life.");
							target.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have been healed to full life.");
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
						}
						
					}else if(args.length == 2){
						
						Player target = Bukkit.getPlayer(args[0]);
						int life = Integer.valueOf(args[1]);
						if(target != null && target.isOnline()){
							
							Damageable d = target;
							d.setHealth(life);
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getName() + " has been healed to " + life + "§4❤§a.");
							target.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have been healed to " + life + "§4❤§a.");
							
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
						}
						
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /heal <player> [<life/20>]");
					}
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cDo not try to heal someone while playing a UHC match.");
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
