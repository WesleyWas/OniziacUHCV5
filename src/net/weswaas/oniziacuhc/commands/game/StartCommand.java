package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.listeners.events.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StartCommand extends UHCCommand{
	
	private Game game;

	public StartCommand(Game game) {
		super("start", "/start");
		
		this.game = game;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if(sender instanceof Player){
			final Player p = (Player) sender;
			
			if(p.hasPermission("uhc.start")){
				if(game.getHost() != null){
					if(game.isGenerated()){
						if(!game.isStarted()){
							Bukkit.getPluginManager().callEvent(new GameStartEvent());
						}
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou need to pregenerate the chunks before starting the UHC. You can make it now with /chunks generate");
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou need to specify a host. You can do it with /host <player>");
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
