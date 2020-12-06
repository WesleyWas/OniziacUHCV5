package net.weswaas.oniziacuhc.managers.borders;

import net.weswaas.oniziacuhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BorderListener implements Listener{
	
	private Game game;
	
	public BorderListener(Game game) {
		
		this.game = game;
	}
	
	@EventHandler
	public void on(PlayerMoveEvent e){
		Player p = e.getPlayer();
		
		int x = p.getLocation().getBlockX();
		int z = p.getLocation().getBlockZ();
		
		if(p.getWorld() == Bukkit.getWorld("lobby")){
			return;
		}
		
		if(p.getWorld() == Bukkit.getWorld("world")){
			if(x > (game.getCurrentBorder() - 1) ||
					x < (game.getCurrentBorder() - game.getCurrentBorder() * 2) + 1 ||
					z > (game.getCurrentBorder() - 1) ||
					z < (game.getCurrentBorder() - game.getCurrentBorder() * 2) + 1){
				
				if(x > (game.getCurrentBorder() - 1)){
					
					p.teleport(p.getLocation().add(-0.5, 0, 0));
					
				}
				if(x < (game.getCurrentBorder() - game.getCurrentBorder() * 2) + 1){
					
					p.teleport(p.getLocation().add(0.5, 0, 0));
					
				}
				if(z > (game.getCurrentBorder() - 1)){
					
					p.teleport(p.getLocation().add(0, 0, -0.5));
					
				}
				if(z < (game.getCurrentBorder() - game.getCurrentBorder() * 2) + 1){
					
					p.teleport(p.getLocation().add(0, 0, 0.5));
					
				}
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou have reached the edge of this world.");
				
			}
		}
		else if(p.getWorld() == Bukkit.getWorld("world_nether")){
			
			int pos = game.getNetherRadius();
			int neg = game.getNetherRadius() - game.getNetherRadius() * 2;
			
			if(x > pos || x < neg || z > pos || z < neg){
				
				if(x > pos){
					p.teleport(p.getLocation().add(-2, 0, 0));
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou have reached the edge of this world");
				}
				if(x < neg){
					p.teleport(p.getLocation().add(2, 0, 0));
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou have reached the edge of this world");
				}
				if(z > pos){
					p.teleport(p.getLocation().add(0, 0, -2));
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou have reached the edge of this world");
				}
				if(z < neg){
					p.teleport(p.getLocation().add(0, 0, 2));
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou have reached the edge of this world");
				}
				
			}
			
		}
	}

}
