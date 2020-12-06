package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.NickManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerListCommand extends UHCCommand{

	private NickManager nick;

	public PlayerListCommand(NickManager nick) {
		super("plist", "/plist");
		this.nick = nick;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.seelist")){
				if(args.length == 0){
					
					String s = null;
					StringBuilder list = new StringBuilder("");
					int i = 1;
					
					for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
						if(list.length() > 1){
							if(i == net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.size()){
								list.append(" and ");
							}else{
								list.append(", ");
							}
						}

						String name = pls.getName();

						if(nick.isNicked(p)){
                            name = nick.getNickedName(pls.getName());
						}
						
						list.append(pls.getPlayerListName());
						i++;
						s = list.toString().trim();
					}

					ArrayList<Player> size = net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers();
					
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Current players §a(" + (size == null ? "0" : size.size()) + ") §8» §7" + s);
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /plist");
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
