package net.weswaas.oniziacuhc.managers.borders;

import net.weswaas.oniziacuhc.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderPlacer {
	
	private Game game;
	
	public BorderPlacer(Game game) {
		
		this.game = game;
	}
	
	public void place(final int radius, final World w, final int borderHight, boolean isReal){

		if(isReal){
			game.setCurrentBorder(radius);
		}
		
		for(int extremite = (radius - radius * 2) + 1; extremite <= radius - 1; extremite++){
			
			Location loc = new Location(w, extremite, 100, radius - radius * 2);
			int hight = w.getHighestBlockYAt(loc);
			
			for(int i = hight; i <= hight+borderHight; i++){
				w.getBlockAt(extremite, i, radius - radius * 2).setType(Material.BEDROCK);
			}
		}
		
		new BukkitRunnable() {
			public void run() {
				for(int extremite = radius - radius * 2; extremite <= radius - 1; extremite++){
					
					Location loc = new Location(w, extremite, 100, radius);
					int hight = w.getHighestBlockYAt(loc);
					
					for(int i = hight; i <= hight+borderHight; i++){
						w.getBlockAt(extremite, i, radius).setType(Material.BEDROCK);
					}
				}
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 10);
		
		new BukkitRunnable() {
			public void run() {
				for(int extremite = radius - radius * 2; extremite <= radius - 1; extremite++){
					
					Location loc = new Location(w, radius - radius * 2, 100, extremite);
					int hight = w.getHighestBlockYAt(loc);
					
					for(int i = hight; i <= hight+borderHight; i++){
						w.getBlockAt(radius - radius * 2, i, extremite).setType(Material.BEDROCK);
					}
				}
				
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 20);
		
		new BukkitRunnable() {
			public void run() {
				for(int extremite = radius - radius * 2; extremite <= radius - 1; extremite++){
					
					Location loc = new Location(w, radius, 100, extremite);
					int hight = w.getHighestBlockYAt(loc);
					
					for(int i = hight; i <= hight+borderHight; i++){
						w.getBlockAt(radius, i, extremite).setType(Material.BEDROCK);
					}
				}
				
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 30);
		
	}

}
