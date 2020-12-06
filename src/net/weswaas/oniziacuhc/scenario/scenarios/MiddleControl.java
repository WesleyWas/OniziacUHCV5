package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.listeners.events.PVPEvent;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class MiddleControl extends Scenario implements Listener{

	public MiddleControl() {
		super("MiddleControl", "After the PVP activation, every minute, a random number between 1 and 3 for diamonds and 1 and 7 for gold decids the number of diamonds and golds looted at the middle.");
		slot = 29;
		setMaterial(new ItemBuilder(Material.FIREBALL).name("§aMiddleControl").build());
	}
	
	@EventHandler
	public void onPvP(PVPEvent e){
	
		if(!Bukkit.getWorld("world").getPVP()){
			return;
		}
		
		final Location loc = new Location(Bukkit.getWorld("world"), 0, Bukkit.getWorld("world").getHighestBlockYAt(new Location(Bukkit.getWorld("world"), 0, 100, 0)) + 2, 0);
		
		new BukkitRunnable() {
			public void run() {
				
				Random r = new Random();
				int diaint = r.nextInt(3);
				int goldint = r.nextInt(7);
				
				if(diaint == 0){
					diaint = 1;
				}
				
				if(goldint == 0){
					goldint = 1;
				}
				
				final ItemStack dia = new ItemStack(Material.DIAMOND, diaint);
				final ItemStack gold = new ItemStack(Material.GOLD_INGOT, goldint);
				
				Bukkit.getWorld("world").dropItemNaturally(loc, dia);
				Bukkit.getWorld("world").dropItemNaturally(loc, gold);
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + diaint + " diamonds and " + goldint + " golds just looted at the middle of the map.");
				
			}
		}.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 0, 1200);
		
	}

}
