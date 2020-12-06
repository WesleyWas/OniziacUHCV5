package net.weswaas.oniziacuhc.commands.player;

import com.weswaas.api.functions.OniziacPlayer;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.List;

public class StaffCommand extends UHCCommand{

	public StaffCommand() {
		super("staff", "/staff <sentence>");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("uhc.staff")){
				if(args.length > 0){
					
					StringBuilder list = new StringBuilder("");
					for(String s : args){
						list.append(s + " ");
					}
					
					for(Player pls : Bukkit.getOnlinePlayers()){
						if(pls.hasPermission("uhc.staff")){
							PermissionUser user = PermissionsEx.getUser(p);
							
							pls.sendMessage("§3[StaffChat]" + user.getPrefix().replace("&", "§") + OniziacPlayer.getPlayer(pls).getColor() + p.getName() + " §8» §f" + list.toString().trim());
							if(pls != p){
								pls.playSound(pls.getLocation(), Sound.NOTE_BASS, 0.8f, 0.8f);
							}
						}
					}
					
				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /staff <sentence>.");
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
