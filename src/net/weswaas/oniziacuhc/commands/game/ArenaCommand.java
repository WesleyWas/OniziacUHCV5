package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.ArenaManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ArenaCommand extends UHCCommand{
	
	private ArenaManager arena;

	public ArenaCommand(ArenaManager arena) {
		super("arena", "/arena leave");
		this.arena = arena;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(GameState.isState(GameState.LOBBY)){
				if(args.length == 0){
					if(arena.isInArena(p)){
						arena.removeArena(p);
					}else{
						arena.setInArena(p);
					}
				}else if(args.length == 1){
					if(args[0].equalsIgnoreCase("leave")){
						arena.removeArena(p);
					}else if(args[0].equalsIgnoreCase("enable")){
						if(p.hasPermission("uhc.arena.enable")){
							if(!arena.isEnabled()){
								arena.setEnabled(true);
								p.sendMessage(OniziacUHC.PREFIX + "You enabled the arena.");
							}else{
								p.sendMessage(OniziacUHC.PREFIX + "§cArena is already enabled.");
							}
						}
					}else if(args[0].equalsIgnoreCase("disable")){
						if(p.hasPermission("uhc.arena.disable")){
							if(arena.isEnabled()){
								arena.setEnabled(false);
								arena.removeAllFromArena();
								p.sendMessage(OniziacUHC.PREFIX + "You disabled the arena.");
							}else{
								p.sendMessage(OniziacUHC.PREFIX + "§cArena is already disabled.");
							}
						}
					}
				}
			}else{
				p.sendMessage(OniziacUHC.PREFIX + "§cArena commands are disabled during the UHC.");
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
