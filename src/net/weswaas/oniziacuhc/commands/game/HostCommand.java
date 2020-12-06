package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HostCommand extends UHCCommand{

	private Game game;
	private SpectatorManager sm;
	
	public HostCommand(Game game, SpectatorManager sm) {
		super("host", "/host <me | player | add> [<player>]");
		
		this.game = game;
		this.sm = sm;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.sethost")){
				if(args.length == 0){
					if(game.getHost() != null){
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Host is " + game.getHost().getName() + "§a.");
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "The host is actually not set.");
					}
				}
				else if(args.length == 1){
					
					if(args[0].equalsIgnoreCase("me")){
						setHost(p);
					}
				}else if(args.length == 2){
					
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null && target.isOnline()){
						setHost(target);
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlayer can't be found.");
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cSynthax error. Please try with /host <me | player | add> [<player>]");
				}
			}
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}
	
	private void setHost(Player p){
		
		game.setHost(p);
		sm.add(p);
		net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.remove(p);
		Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§3The host is now §3" + p.getName() + "§a.");
	}

}
