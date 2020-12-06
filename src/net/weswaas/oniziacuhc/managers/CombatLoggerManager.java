package net.weswaas.oniziacuhc.managers;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.listeners.DeathListener;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import net.weswaas.oniziacuhc.scenario.scenarios.Timebomb;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class CombatLoggerManager implements Listener{
	
	private DeathListener dl;
	private RespawnManager rm;
	private WinManager wm;
	private ScenarioManager sm;
	private Timebomb timebomb;
	private PlayerDataManager data;
	//private SpectatorManager spec;
    private Game game;
	
	public CombatLoggerManager(DeathListener dl, RespawnManager rm, WinManager wm, ScenarioManager sm, Timebomb timebomb, SpectatorManager spec, PlayerDataManager data, Game game) {
		
		this.dl = dl;
		this.rm = rm;
		this.wm = wm;
		this.sm = sm;
		this.timebomb = timebomb;
		this.data = data;
        this.game = game;
	}
	
	public static ArrayList<Villager> villagers = new ArrayList<Villager>();
	public static HashMap<String, Villager> pVillager = new HashMap<String, Villager>();
    public static HashMap<String, net.minecraft.server.v1_7_R4.Entity> pEntity = new HashMap<>();
	public static HashMap<Villager, String> vPlayer = new HashMap<Villager, String>();
	public static HashMap<String, PlayerInventory> invs = new HashMap<String, PlayerInventory>();
	public static HashMap<String, Location> pLoc = new HashMap<String, Location>();
	
	@EventHandler
	public void onPvP(EntityDamageEvent e){
		if(e.getEntity() instanceof Villager){
			Villager z = (Villager) e.getEntity();
			if(villagers.contains(z)){
				if(!Bukkit.getWorld("world").getPVP()){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e){
		
		if(e.getEntity() instanceof Villager){
			Villager villager = (Villager) e.getEntity();
			if(villagers.contains(villager)){
				String nameP = vPlayer.get(villager);
				if(nameP == null){
					return;
				}
				
				e.getDrops().clear();
				
				final OfflinePlayer p = Bukkit.getOfflinePlayer(nameP);
				
				if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
					return;
				}
				
				if(sm.getScenario("Timebomb").isEnabled()){
					timebomb.setChest(nameP, villager.getLocation(), invs.get(nameP));
				}else{
					for(ItemStack item : invs.get(nameP).getContents()){
						if(item != null){
							if(item.getType() != Material.AIR){
								Bukkit.getWorld("world").dropItemNaturally(villager.getLocation(), item);
							}
						}
					}
					for(ItemStack item : invs.get(nameP).getArmorContents()){
						if(item != null){
							if(item.getType() != Material.AIR){
								Bukkit.getWorld("world").dropItemNaturally(villager.getLocation(), item);
							}
						}
					}
					dl.setFence(villager.getLocation(), nameP);
				}
				if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
					net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.remove(p);
				}

                if(game.getTeamSize() > 1){
                    Team t = Team.getTeamByName(nameP);
                    if(t.getAlivePlayerNames().size() == 0){
                        t.removeTeam();
                    }
                }

				pVillager.remove(nameP);
				pEntity.remove(nameP);
				
				final Player killer = e.getEntity().getKiller();
				
				Bukkit.broadcastMessage(ChatColor.RED + villager.getCustomName() + " (CombatLogger) §ewas slain by §c" + killer.getPlayerListName());
				data.getStatsToStoreByUUID(p.getUniqueId().toString()).addDeath();
				
				if(!DeathListener.pKills.containsKey(killer.getPlayerListName())){
					DeathListener.pKills.put(killer.getPlayerListName(), 0);
				}
				
				DeathListener.pKills.put(killer.getPlayerListName(), DeathListener.pKills.get(killer.getPlayerListName()) + 1);
				new BukkitRunnable() {
					public void run() {
						wm.winCheck();
					}
				}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 2);

                if(p.getPlayer() != null){
                    rm.saveInv(p.getPlayer().getName(), p.getPlayer().getLocation(), p.getPlayer().getInventory());
                }
			}
		}
		
	}

}
