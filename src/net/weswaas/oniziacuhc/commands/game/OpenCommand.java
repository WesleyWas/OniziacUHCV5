package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OpenCommand extends UHCCommand{

	private Game game;
	
	public OpenCommand(Game game) {
		super("open", "/open");
		
		this.game = game;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.setopen")){
				
				if(!game.isOpen()){
					
					game.setOpen(true);
					Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "The UHC is now open.");
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThe game is already open.");
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
