package net.weswaas.oniziacuhc.commands.player;

import com.google.common.base.Joiner;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpopCommand extends UHCCommand{
	
	private String PREFIX = "§4HelpOp §8» §7";
	private final List<Player> cooldownPlayers = new ArrayList<Player>();

	public HelpopCommand() {
		super("helpop", "/helpop <message>");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			final Player p = (Player) sender;
			
			if(args.length == 0){
				return false;
			}
			
			if(cooldownPlayers.contains(p)){
				p.sendMessage(PREFIX + " §cPlease don't spam helpop.");
				return true;
			}
			
			String msg = Joiner.on(' ').join(Arrays.copyOfRange(args, 0, args.length));
			
			for(Player pls : Bukkit.getOnlinePlayers()){
				if(!pls.hasPermission("uhc.staff")){
					continue;
				}

				if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().contains(pls)){
					continue;
				}
				
				if(pls == p){
					continue;
				}
				
				pls.sendMessage(PREFIX + sender.getName() + ": §6" + msg);
				pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 0.8f, 0.8f);
			}
			
			cooldownPlayers.add(p);
			p.sendMessage(PREFIX + " §7Your message has been send. Please do not spam it.");
			
			new BukkitRunnable() {
				public void run() {
					cooldownPlayers.remove(p);
				}
			}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 300);
			
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
