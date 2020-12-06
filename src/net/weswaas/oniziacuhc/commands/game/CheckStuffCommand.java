package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CheckStuffCommand extends UHCCommand{

	public CheckStuffCommand() {
		super("checkstuff", "/checkstuff <1 | 2 | 3 | 4>");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.checkstuff")){
				if(args.length == 1){
					
					if(args[0].equalsIgnoreCase("1")){
						p.sendMessage(OniziacUHC.PREFIX + "==================================");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Players with 1 diamond part:");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						for(Player pls : Bukkit.getOnlinePlayers()){
							if(pls.getInventory().getHelmet() != null && pls.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Helmet)");
							}
							else if(pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getHelmet() != null && pls.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Chestplate)");
							}
							else if(pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getHelmet() != null && pls.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Leggings)");
							}
							else if(pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS
									&& pls.getInventory().getHelmet() != null && pls.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Boots)");
							}
						}
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==================================");
					}
					else if(args[0].equalsIgnoreCase("2")){
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==================================");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Players with 2 diamonds parts:");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						
						for(Player pls : Bukkit.getOnlinePlayers()){
							if(pls.getInventory().getHelmet() != null && pls.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Helmet + Chestplate)");
							}
							else if(pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Helmet + Leggings)");
							}
							else if(pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Helmet + Boots)");
							}
							else if(pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Chestplate + Leggings)");
							}
							else if(pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS
									&& pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Chestplate + Boots)");
							}
							else if(pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Leggings + Boots)");
							}
						}
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==================================");
					}
					else if(args[0].equalsIgnoreCase("3")){
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==================================");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Players with 3 diamonds parts:");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						
						for(Player pls : Bukkit.getOnlinePlayers()){
							if(pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Chestplate + Leggings + Boots)");
							}
							else if(pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Helmet + Leggings + Boots)");
							}
							else if(pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Helmet + Chestplate + Boots)");
							}
							else if(pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS
									&& pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName() + " §3(Helmet + Chestplate + Leggings)");
							}
						}
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==================================");
					}
					else if(args[0].equalsIgnoreCase("4")){
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==================================");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Full diamonds players:");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						
						for(Player pls : Bukkit.getOnlinePlayers()){
							if(pls.getInventory().getHelmet() !=  null && pls.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET
									&& pls.getInventory().getChestplate() != null && pls.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE
									&& pls.getInventory().getLeggings() != null && pls.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS
									&& pls.getInventory().getBoots() != null && pls.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS){
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §a" + pls.getPlayerListName());
							}
						}
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==================================");
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /checkstuff <1 | 2 | 3 | 4>");
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
