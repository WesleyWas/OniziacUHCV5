package net.weswaas.oniziacuhc.managers.borders;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BorderTeleporter {
	
	public void teleport(int radius, World w, Player pls){
		
		int x = pls.getLocation().getBlockX();
		int z = pls.getLocation().getBlockZ();
		
		Location loc = pls.getLocation();
		
		if(x > radius || x < radius - radius * 2 || z > radius || z < radius - radius * 2){
			if(x > radius){
				
				loc.setX((radius - 3) + 0.5);
				loc.setY(w.getHighestBlockYAt(loc) + 1);
			}
			if(x < radius - radius * 2){
				
				loc.setX(((radius - radius * 2) + 3) - 0.5);
				loc.setY(w.getHighestBlockYAt(loc) + 1);
			}
			if(z > radius){
				
				loc.setZ((radius - 3) + 0.5);
				loc.setY(w.getHighestBlockYAt(loc) + 1);
			}
			if(z < radius - radius * 2){
				
				loc.setZ(((radius - radius * 2) + 3) - 0.5);
				loc.setY(w.getHighestBlockYAt(loc) + 1);
			}

			pls.teleport(loc);

		}

		if(radius < 101){
			if(pls.getLocation().getY() < 50){
				loc.setY(w.getHighestBlockYAt(loc));
				pls.teleport(loc);
				pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "As you were in cave, we teleported you to the surface to play the meetup.");
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void netherTeleport(){
		
		for(Player pls : Bukkit.getOnlinePlayers()){
			Location loc = pls.getLocation();
			if(loc.getWorld() == Bukkit.getWorld("world_nether")){
				
				Location newLoc = new Location(Bukkit.getWorld("world"), loc.getBlockX(), 100, loc.getBlockZ());
				newLoc.setY(Bukkit.getWorld("world").getHighestBlockYAt(newLoc) + 1);
				pls.teleport(newLoc);
				
			}
		}
		
	}

}
