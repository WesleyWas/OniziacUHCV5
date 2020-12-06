package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.listeners.events.FreezeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FreezeCommand extends UHCCommand{

	public FreezeCommand() {
		super("freeze", "/freeze <player>");
		
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(p.hasPermission("uhc.freeze")){
				
				if(args.length == 1){
					
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null && target.isOnline()){
						
						Bukkit.getPluginManager().callEvent(new FreezeEvent(p, target));
						
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Player can't be found.");
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cSynthax error. Please try with /freeze <player>");
				}
				
			}
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}
