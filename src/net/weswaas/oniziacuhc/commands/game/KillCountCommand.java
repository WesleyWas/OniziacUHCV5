package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.listeners.DeathListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class KillCountCommand extends UHCCommand{

	public KillCountCommand() {
		super("killcount", "/killcount <player>");
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(args.length == 1){
				
				Player target = Bukkit.getPlayer(args[0]);
				if(target != null && target.isOnline()){
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getPlayerListName() + " has " + DeathListener.pKills.get(target.getPlayerListName()) + " kills.");
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
				}
				
			}else{
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /killcount <player>");
			}
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
