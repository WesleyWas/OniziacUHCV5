package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.RespawnManager;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpecCommand extends UHCCommand{
	
	private SpectatorManager spec;
	private RespawnManager rm;

	public SpecCommand(SpectatorManager spec, RespawnManager rm) {
		super("spectator", "/spectator <add | remove> <player>");
		
		this.spec = spec;
		this.rm = rm;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.spec.manage")){
				if(args.length == 0){
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "======== Spectators commands ========");
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§a");
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/spec add [player] §8» §7Add the specified player into the specs.");
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/spec remove [player] §8» §7Remove the specified player from the specs.");
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "/spec list §8» §7List of all specs.");
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§a");
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "=====================================");
				}
				else if(args.length == 1 && args[0].equalsIgnoreCase("list")){
					
					if(spec.getSpecs().isEmpty()){
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "There are actually no spectators in this UHC.");
					}else{
						String s = null;
						StringBuilder list = new StringBuilder("");
						int i = 1;
						
						for(String pls : spec.getSpecs()){
							OfflinePlayer specs = Bukkit.getOfflinePlayer(pls);
							if(specs.isOnline()){
								if(list.length() > 1){
									if(i == spec.getSpecs().size()){
										list.append(" and ");
									}else{
										list.append(", ");
									}
								}

								list.append(pls);
								i++;
								s = list.toString().trim();
							}
						}
						
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Current spectators §a(" + spec.getSpecs().size() + ") §8» §7" + s);
					}
					
				}else if(args.length == 2){
					
					if(args[0].equalsIgnoreCase("add")){
						
						Player target = Bukkit.getPlayer(args[1]);
						if(target != null && target.isOnline()){
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getName() + " has been added to spectators.");
							net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.remove(target);
							spec.add(target);
						}else{
							OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + op.getName() + " has been added to spectators.");
							spec.getPreparedPlayers().add(op.getName());
						}
						
					}
					else if(args[0].equalsIgnoreCase("remove")){
						
						Player target = Bukkit.getPlayer(args[1]);
						if(GameState.isState(GameState.LOBBY)){
							if(target != null && target.isOnline()){
								if(!rm.invs.containsKey(p.getName())){
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + target.getName() + " has been removed from spectators.");
									spec.remove(target);
									if(!p.hasPermission("uhc.spec.stay")){
										target.kickPlayer(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou've been removed from the spectators.");
									}
								}else{
									p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlease do not try to force respawn !");
								}
							}else{
								OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + op.getName() + " has been removed from spectators.");
								spec.getPreparedPlayers().remove(op.getName());
							}
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cDo not revive you after game starting.");
						}
						
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /spec <add | remove | list> [<player>]");
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
