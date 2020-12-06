package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.MiningNotifManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MiningNotifCommand extends UHCCommand{
	
	private MiningNotifManager mnm;

	public MiningNotifCommand(MiningNotifManager mnm) {
		super("notif", "/notif <add | remove> <player>");
		
		this.mnm = mnm;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.notif.manage")){
				if(args.length == 2){
					Player target = Bukkit.getPlayer(args[1]);
					if(target != null && target.isOnline()){
						if(args[0].equalsIgnoreCase("add")){
							if(!mnm.notified.contains(target)){
								mnm.notified.add(target);
								if(target == p){
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You are now on the notified mining list.");
								}else{
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You've added " + target.getName() + " to the notified mining list.");
									target.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You've been added to the notified mining list by " + p.getName());
								}
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThis player is already in the notified mining list.");
							}
						}else if(args[0].equalsIgnoreCase("remove")){
							if(mnm.notified.contains(target)){
								mnm.notified.remove(target);
								if(target == p){
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You are no more on the notified mining list.");
								}else{
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getName() + " is now no more on the notified mining list.");
									target.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You are now no more on the notified mining list.");
								}
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThis player is not in the notified mining list.");
							}
						}
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
					}
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /notif <add | remove> <player>");
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
