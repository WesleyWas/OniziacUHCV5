package net.weswaas.oniziacuhc.listeners;

import net.weswaas.oniziacuhc.managers.ArenaManager;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class RespawnListener implements Listener{
	
	private SpectatorManager spec;
	private ArenaManager arena;
	
	public RespawnListener(SpectatorManager spec, ArenaManager arena) {
		
		this.spec = spec;
		this.arena = arena;
	}
	
	private ArrayList<String> kicked = new ArrayList<String>();
	
	@EventHandler
	public void on(PlayerRespawnEvent e){
		final Player p = e.getPlayer();
		
		if(arena.isInArena(p)){
			return;
		}
		
		if(!p.hasPermission("uhc.spec")){
			kicked.add(p.getName());
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have died. Better luck next time :)");
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You will be kicked in 30 seconds. To spectate the game, you can buy a donator rank on our website.");
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().clear();
			p.setFoodLevel(20);
			SpectatorManager.specChat.add(p.getName());
			p.setSaturation(999999);
			new BukkitRunnable() {
				public void run() {
					p.teleport(new Location(Bukkit.getWorld("lobby"), 0, Bukkit.getWorld("lobby").getHighestBlockYAt(new Location(Bukkit.getWorld("lobby"), 0, 100, 0)) + 1, 0).setDirection(new Vector(-5, 0, 0)));
					SpectatorManager.tempSpecs.add(p);
				}
			}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 4);
			new BukkitRunnable() {
				public void run() {
					SpectatorManager.tempSpecs.remove(p);
					p.kickPlayer("You have died. Better luck next time :)");
				}
			}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 600);
		}else{
			new BukkitRunnable() {
				public void run() {
					spec.add(p);
				}
			}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 4);
			
		}
	}

}
