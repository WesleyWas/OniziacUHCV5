package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiveCommand extends UHCCommand{

	public GiveCommand() {
		super("give", "/give <player | all> <item> <amount>");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.give")){
				if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
					if(args.length == 3){
						if(args[0].equalsIgnoreCase("all")){
							for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
								pls.getInventory().addItem(new ItemStack(Material.getMaterial(Integer.valueOf(args[1])), Integer.valueOf(args[2])));
								pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You've received material with id " + Integer.valueOf(args[1]) + " from a host.");
							}
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Material with id " + Integer.valueOf(args[1]) + " has been given to all the UHC players.");
						}else{
							Player target = Bukkit.getPlayer(args[0]);
							if(target != null && target.isOnline()){
								target.getInventory().addItem(new ItemStack(Material.getMaterial(Integer.valueOf(args[1])), Integer.valueOf(args[2])));
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Material with id " + Integer.valueOf(args[1]) + " has been given to " + target.getName());
								target.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You've received material with id " + Integer.valueOf(args[1]) + " from a host.");
							}else{
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer was not found.");
							}
						}
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /give <player | all> <item> <amount>");
					}
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlease do not give items while you're playing a game.");
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
