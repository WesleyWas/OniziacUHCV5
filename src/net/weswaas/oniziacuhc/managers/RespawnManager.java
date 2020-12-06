package net.weswaas.oniziacuhc.managers;

import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RespawnManager {
	
	private SpectatorManager spec;
	
	public RespawnManager(SpectatorManager spec) {
		this.spec = spec;
	}
	
	public HashMap<String, ItemStack[]> invs = new HashMap<String, ItemStack[]>();
	public HashMap<String, List<ItemStack>> drops = new HashMap<>();
	public HashMap<String, ItemStack[]> armors = new HashMap<String, ItemStack[]>();
	public HashMap<String, Location> locs = new HashMap<String, Location>();
	public ArrayList<String> readyToBeRespawned = new ArrayList<String>();
	
	public void saveInv(String name, Location loc, PlayerInventory inv){
		
		invs.put(name, inv.getContents());
		armors.put(name, inv.getArmorContents());
		locs.put(name, loc);
		
	}

	public void saveDrops(String name, List<ItemStack> drop){
		drops.put(name, drop);
	}
	
	@SuppressWarnings("deprecation")
	public void respawn(final Player p){
		
		if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
			net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.remove(p);
		}

		p.getInventory().setContents(this.invs.get(p.getName()));
		p.getInventory().setArmorContents(this.armors.get(p.getName()));
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setGameMode(GameMode.SURVIVAL);
		p.setFlying(false);
		if(spec.isSpectator(p.getName())){
			spec.remove(p);
		}

		if(!Team.hasTeam(p)){
			new Team(p);
		}

		//REMOVE DROPS IF RESPAWN TO AVOID DOUBLE STUFF
		if(drops.containsKey(p.getName())){
			Location loc = locs.get(p.getName());
			for(ItemStack is : drops.get(p.getName())){
				for(Entity ent : loc.getChunk().getEntities()){
					if(ent instanceof Item){
						Item item = (Item) ent;
						if(item.getItemStack() == is){
							item.remove();
						}
					}
				}
			}
		}

		new BukkitRunnable(){
            public void run(){
                p.teleport(locs.get(p.getName()));
            }
        }.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 10);
		
	}

}
