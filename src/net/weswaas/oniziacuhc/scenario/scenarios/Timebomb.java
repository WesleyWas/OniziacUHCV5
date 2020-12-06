package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Timebomb extends Scenario implements Listener{
	
	private final net.weswaas.oniziacuhc.OniziacUHC plugin;
	private String TIMEBOMB_PREFIX = "§4[§fTimeBomb§4]§f";

	public Timebomb(net.weswaas.oniziacuhc.OniziacUHC plugin){
		super("Timebomb", "After killing a player all of their items will appear in a double chest rather than dropping on the ground. You then have 30 seconds to loot what you want and get the hell away from it."
				+ " This is because the chest explodes after the time is up.");
		
		this.plugin = plugin;
		setMaterial(new ItemBuilder(Material.CHEST).name("§aTimebomb").build());
		slot = 11;
	}
	@Override
	public void onEnable(){
		
	}
	@Override
	public void onDisable(){
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		final Player p = e.getEntity();
		e.setKeepInventory(true);
		setChest(p.getName(), p.getLocation(), p.getInventory());
	}
	
	public void setChest(final String pName, Location loc, PlayerInventory inv){
		
		if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(pName)){
			return;
		}
		
		if(loc.getWorld() != Bukkit.getWorld("world")){
			return;
		}
		
		final Location deathLocOriginal = loc.add(0, -1, 0);
		final Location deathLoc = loc.add(0, -1, 0);
		
		deathLoc.getBlock().setType(Material.TRAPPED_CHEST);
		Chest chest = (Chest) deathLoc.getBlock().getState();
		
		deathLoc.add(0, 0, -1);
		deathLoc.getBlock().setType(Material.TRAPPED_CHEST);
		
		deathLoc.add(0, 1, 0);
		deathLoc.getBlock().setType(Material.AIR);
		
		deathLoc.add(0, 0, 1);
		deathLoc.getBlock().setType(Material.AIR);
		
		for(ItemStack item : inv.getContents()){
			if(item == null || item.getType() == Material.AIR){
				continue;
			}
			
			chest.getInventory().addItem(item);
		}
		
		for(ItemStack item : inv.getArmorContents()){
			if(item == null || item.getType() == Material.AIR){
				continue;
			}
			
			chest.getInventory().addItem(item);
		}
		
		chest.getInventory().addItem(addGoldenHead());
		
		Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				deathLoc.getWorld().createExplosion(deathLoc.getX(),  deathLoc.getY(), deathLoc.getZ(), 3, false, true);
				deathLoc.getWorld().strikeLightning(deathLoc);
				Bukkit.broadcastMessage(TIMEBOMB_PREFIX + "§4" + pName + "§f's corpse has exploded !");
				deathLoc.getBlock().setType(Material.AIR);
				deathLoc.add(0, 0, -1);
				deathLoc.getBlock().setType(Material.AIR);
			}
		}, 600);
		
		Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				deathLoc.getWorld().strikeLightning(deathLoc);
			}
		}, 620);
		
	}
	
	private ItemStack addGoldenHead(){
		ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta goldenHeadM = goldenHead.getItemMeta();
		goldenHeadM.setDisplayName("Golden Head");
		goldenHead.setItemMeta(goldenHeadM);
		
		return goldenHead;
	}

}
