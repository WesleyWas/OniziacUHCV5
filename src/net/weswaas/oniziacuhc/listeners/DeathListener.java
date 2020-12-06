package net.weswaas.oniziacuhc.listeners;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.managers.ArenaManager;
import net.weswaas.oniziacuhc.managers.NickManager;
import net.weswaas.oniziacuhc.managers.RespawnManager;
import net.weswaas.oniziacuhc.managers.WinManager;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class DeathListener implements Listener{
	
	private ScenarioManager manager;
	private WinManager wm;
	private RespawnManager rm;
	private PlayerDataManager data;
	private ArenaManager arena;
	private Game game;
	private NickManager nick;
	
	public DeathListener(ScenarioManager manager, WinManager wm, RespawnManager rm, PlayerDataManager data, ArenaManager arena, Game game, NickManager nick) {
		
		this.manager = manager;
		this.wm = wm;
		this.rm = rm;
		this.data = data;
		this.arena = arena;
		this.game = game;
		this.nick = nick;
	}
	
	public static HashMap<String, Integer> pKills = new HashMap<String, Integer>();
	
	@EventHandler
	public void onDeath(final PlayerDeathEvent e){
		String message = e.getDeathMessage();

		rm.saveInv(e.getEntity().getName(), e.getEntity().getLocation(), e.getEntity().getInventory());
        if(e.getDrops() != null){
            rm.saveDrops(e.getEntity().getName(), e.getDrops());
        }
		
		if(arena.isInArena(e.getEntity())){
			if(e.getEntity().getKiller() != null){
				e.getEntity().getKiller().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You killed " + e.getEntity().getName() + ".");
				e.getEntity().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You were killed by " + e.getEntity().getKiller().getName() + ".");
			}else{
				e.getEntity().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You died");
			}
			e.setDeathMessage(null);
			e.getDrops().clear();
			return;
		}
		
		data.getStatsToStoreByUUID(e.getEntity().getUniqueId().toString()).addDeath();

		if(game.getTeamSize() > 1){
			Team t = Team.getTeam(e.getEntity());
			if(t != null){
				t.removePlayer(e.getEntity());
			}
		}

        net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.remove(e.getEntity());

        e.setDeathMessage(null);
        Bukkit.broadcastMessage(deathMessage(e, e.getEntity(), e.getEntity().getKiller()));
		
		if(e.getEntity().getKiller() instanceof Player){
			Player killer = e.getEntity().getKiller();
			
			if(!pKills.containsKey(killer.getName())){
				pKills.put(killer.getName(), 0);
			}
			pKills.put(killer.getPlayerListName(), pKills.get(killer.getPlayerListName()) + 1);
			data.getStatsToStoreByUUID(killer.getUniqueId().toString()).addKill();
			
		}
		
		setFence(e.getEntity().getLocation(), e.getEntity().getDisplayName());
		new BukkitRunnable() {
			public void run() {
				wm.winCheck();
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 5);
	}
	
	@SuppressWarnings("deprecation")
	public static void setFence(Location loc, String nameP){
		
		if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getScenarioManager().getScenario("Timebomb").isEnabled()){
			return;
		}
		
		if(loc.getWorld() != Bukkit.getWorld("world")){
			return;
		}
		
		loc.getBlock().setType(Material.NETHER_FENCE);
		loc.add(0, 1, 0);
		loc.getBlock().setType(Material.SKULL);
		loc.getBlock().setData((byte)3);
		Skull skull = (Skull) loc.getBlock().getState();
		skull.setOwner(nameP);
		skull.update();
		
	}
	
	public String deathMessage(String message, Entity killer, Player victim){

		if(arena.isInArena(victim)){
			return null;
		}
		
		if(killer instanceof Player){
			Player killerP = (Player) killer;
			
			if(message.contains("was shot by")){
				message = ChatColor.RED + victim.getPlayerListName() + " §ewas shot by §c" + killerP.getPlayerListName();
			}else{
				message = ChatColor.RED + victim.getPlayerListName() + " §ewas slain by §c" + killerP.getPlayerListName();
			}
		}else{
			
			if(message.contains("drowned")){
				message = ChatColor.RED + victim.getPlayerListName() + " §edrowned";
			}else if(message.contains("hit the ground too hard")){
				message = ChatColor.RED + victim.getPlayerListName() + " §ehit the ground too hard";
			}else if(message.contains("fell from a high place")){
				message = ChatColor.RED + victim.getPlayerListName() + " §efell from a high place";
			}else if(message.contains("suffocated")){
				message = ChatColor.RED + victim.getPlayerListName() + " §esuffocated in a wall";
			}else if(message.contains("tried to swim in lava")){
				message = ChatColor.RED + victim.getPlayerListName() + " §etried to swim in lava";
			}
			else{
				message = ChatColor.RED + victim.getPlayerListName() + " §edied";
			}
		}
		
		return message;
		
	}

	public String deathMessage(Event e, LivingEntity entity, Player killer){

		String message;

		String name;
		if(entity instanceof Player){
			name = ((Player) entity).getPlayerListName();
		}else{
			name = entity.getCustomName();
		}

		if(entity.getLastDamageCause() != null){
			switch (entity.getLastDamageCause().getCause()){
				case BLOCK_EXPLOSION:
					message = ChatColor.RED + name + ChatColor.YELLOW + " just got blown the hell up.";
					break;
				case CONTACT:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " walked into a cactus whilst trying to escape " + ChatColor.RED + killer.getPlayerListName();
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " was pricked to death";
					}
					break;
				case CUSTOM:
					if(e instanceof PlayerDeathEvent){
						message = ChatColor.RED + name + ChatColor.YELLOW + ((PlayerDeathEvent )e).getDeathMessage();
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " died for some reason";
					}
					break;
				case DROWNING:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " drowned whilst trying to escape " + ChatColor.RED + killer.getPlayerListName();
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " drowned";
					}
					break;
				case ENTITY_ATTACK:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " was slain by " + ChatColor.RED + killer.getPlayerListName();
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " was slain";
					}
					break;
				case ENTITY_EXPLOSION:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " got blown the hell up by " + ChatColor.RED + killer.getPlayerListName();
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " got blown the hell up";
					}
					break;
				case FALL:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " was doomed to fall by " + ChatColor.RED + killer.getPlayerListName();
					}else{
						if(entity.getFallDistance() > 5){
							message = ChatColor.RED + name + ChatColor.YELLOW + " fell from a high place";
						}else{
							message = ChatColor.RED + name + ChatColor.YELLOW + " hit the ground too hard";
						}
					}
					break;
				case FALLING_BLOCK:
					message = ChatColor.RED + name + ChatColor.YELLOW + " got freaking squashed by a block";
					break;
				case FIRE:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " walked into a fire whilst fighting " + ChatColor.RED + killer.getPlayerListName();
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " went up in flames";
					}
					break;
				case FIRE_TICK:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " was burnt to a crisp whilst fighting " + ChatColor.RED + killer.getPlayerListName();
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " burned to death";
					}
					break;
				case LAVA:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " tried to swim in lava while trying to escape " + ChatColor.RED + killer.getPlayerListName();
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " tried to swim in lava";
					}
					break;
				case LIGHTNING:
					message = ChatColor.RED + name + ChatColor.YELLOW + " got lit the hell up by lightnin'";
					break;
				case MAGIC:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " was killed by " + ChatColor.RED + killer.getPlayerListName() + ChatColor.YELLOW + " using magic";
					}else{
						message = ChatColor.RED + name + ChatColor.YELLOW + " was killed by magic";
					}
					break;
				case POISON:
					message = ChatColor.RED + name + ChatColor.YELLOW + " was poisoned";
					break;
				case PROJECTILE:
					if(killer != null){
						message = ChatColor.RED + name + ChatColor.YELLOW + " was shot by " + ChatColor.RED + killer.getPlayerListName();
					}else{
                        message = ChatColor.RED + name + ChatColor.YELLOW + " was shot";
                    }
                    break;
                case STARVATION:
                    message = ChatColor.RED + name + ChatColor.YELLOW + " starved to death";
                    break;
                case SUFFOCATION:
                    message = ChatColor.RED + name + ChatColor.YELLOW + " suffocated in a wall";
                    break;
                case SUICIDE:
                    message = ChatColor.RED + name + ChatColor.YELLOW + " suicided";
                    break;
                case THORNS:
                    message = ChatColor.RED + name + ChatColor.YELLOW + " was slain by thorns";
                    break;
                case VOID:
                    if(killer != null){
                        message = ChatColor.RED + name + ChatColor.YELLOW + " was knocked into the void by " + ChatColor.RED + killer.getPlayerListName();
                    }else{
                        message = ChatColor.RED + name + ChatColor.YELLOW + " fell out the world";
                    }
                    break;
                case WITHER:
                    message = ChatColor.RED + name + ChatColor.YELLOW + " withered away";
                    break;
                default:
                    message = ChatColor.RED + name + ChatColor.YELLOW + " died";
			}
		}else{
            message = ChatColor.RED + name + ChatColor.YELLOW + " died";
        }

        return message;

	}



























}
