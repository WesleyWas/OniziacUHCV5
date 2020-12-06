package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.listeners.events.PVPEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PvPCommand extends UHCCommand{
	
	private PVPEvent pvp;

	public PvPCommand(PVPEvent pvp) {
		super("pvp", "/pvp <true | false>");
		this.pvp = pvp;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.setpvp")){
				if(GameState.isState(GameState.GAME) || GameState.isState(GameState.FINISH)){
					
					if(args.length == 0){
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "The PVP is actually: " + String.valueOf(Bukkit.getWorld("world").getPVP()));
					}
					else if(args.length == 1){
						
						Bukkit.getWorld("world").setPVP(Boolean.valueOf(args[0]));
						Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "The PVP has been toggled to: " + String.valueOf(args[0]));
						if(Bukkit.getWorld("world").getPVP()){
							Bukkit.getPluginManager().callEvent(pvp);
						}
						
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /pvp <true | false>");
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't toggle PvP before the start.");
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
