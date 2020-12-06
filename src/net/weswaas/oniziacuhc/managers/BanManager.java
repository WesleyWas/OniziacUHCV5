package net.weswaas.oniziacuhc.managers;


import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.listeners.DeathListener;
import net.weswaas.oniziacuhc.scenario.scenarios.Timebomb;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BanManager implements Listener{
	
	private DeathListener dl;
	private Timebomb timebomb;
	
	public BanManager(DeathListener dl, Timebomb timebomb) {
		
		this.dl = dl;
		this.timebomb = timebomb;
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e){
		final Player p = e.getPlayer();

		if(!e.getReason().contains("Flying is not enabled")){
            if(GameState.isState(GameState.LOBBY)){
                return;
            }

            if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
                return;
            }

            net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.remove(p);

            if(timebomb.isEnabled()){
                timebomb.setChest(p.getName(), p.getLocation(), p.getInventory());
                return;
            }

            dropItems(p);
            setFence(p);
		}
	}
	
	private void dropItems(final Player p){
		
		final World w = Bukkit.getWorld("world");
		
		
		
		for(final ItemStack item : p.getInventory().getContents()){
			if(item != null){
				if(item.getType() != Material.AIR){
					new BukkitRunnable() {
						public void run() {
							w.dropItemNaturally(p.getLocation(), item);
						}
					}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 2);
				}
			}
		}
		for(final ItemStack armor : p.getInventory().getArmorContents()){
			if(armor != null){
				if(armor.getType() != Material.AIR){
					new BukkitRunnable() {
						public void run() {
							w.dropItemNaturally(p.getLocation(), armor);
						}
					}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 2);
				}
			}
		}
		
	}
	
	private void setFence(final Player p){
		new BukkitRunnable() {
			public void run() {
				dl.setFence(p.getLocation(), p.getDisplayName());
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 2);
	}

}
