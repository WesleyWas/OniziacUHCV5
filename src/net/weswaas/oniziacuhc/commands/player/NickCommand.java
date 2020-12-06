package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class NickCommand extends UHCCommand{
	
	private NickManager nm;

	public NickCommand(NickManager nm) {
		super("nick", "/nick [<list | remove>]");
		this.nm = nm;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.nick")){
				
				if(args.length == 0){
					if(GameState.isState(GameState.LOBBY)){
						nm.nick(p);
					}else{
						p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't nick you once the game has begin.");
					}
					
				}else if(args.length == 1){
					
					if(args[0].equalsIgnoreCase("remove")){
						if(GameState.isState(GameState.LOBBY)){
							nm.unNick(p);
						}else{
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't nick you once the game has begin.");
						}
					}
					else if(args[0].equalsIgnoreCase("list")){
						if(p.hasPermission("uhc.nick.list")){
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "======= Nicked Players =======");
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "");
							for(String s : nm.getNickedPlayers()){
								OfflinePlayer op = Bukkit.getOfflinePlayer(s);
								String game = nm.getNickName().get(op.getName());
                                if(game == op.getName()){
                                    game = nm.getNameNick().get(op.getName());
                                }
								p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + op .getName() + " is nicked as " + game);
							}
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "");
							p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==============================");
						}
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /nick [<remove>]");
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
