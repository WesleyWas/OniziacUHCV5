package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FlyCommand extends UHCCommand{

	public FlyCommand() {
		super("fly", "/fly [<player>]");
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.fly")){
				if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
						
						if(p.isFlying()){
							p.setFlying(false);
						}else{
							p.setFlying(true);
						}
						
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Your fly mode has been " + (p.isFlying() ? "enabled" : "�cdisabled") + "�a.");
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "�cPlease do not try to fly while playing a UHC match.");
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
