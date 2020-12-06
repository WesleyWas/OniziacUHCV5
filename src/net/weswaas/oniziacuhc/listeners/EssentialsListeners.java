package net.weswaas.oniziacuhc.listeners;

import com.weswaas.api.Main;
import com.weswaas.api.functions.OniziacPlayer;
import com.weswaas.api.utils.MessageUtils;
import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.gui.GUIManager;
import net.weswaas.oniziacuhc.listeners.events.PVPEvent;
import net.weswaas.oniziacuhc.managers.ArenaManager;
import net.weswaas.oniziacuhc.managers.NickManager;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import net.weswaas.oniziacuhc.managers.borders.BorderTeleporter;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Leaves;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.weswaas.oniziacuhc.managers.SpectatorManager.specChat;

public class EssentialsListeners implements Listener{
	
	private int maxChances = 100;
	private int minChances = 1;
	
	public ArrayList<String> cooldown = new ArrayList<String>();
	
	private Settings settings;
	private Game game;
	private PlayerDataManager data;
	private ArenaManager arena;
	private BorderTeleporter tp;
    private ScenarioManager manager;
	private GUIManager gui;
	private NickManager nick;
	private SpectatorManager spec;
	
	public EssentialsListeners(Settings settings, Game game, PlayerDataManager data, ArenaManager arena, BorderTeleporter tp, ScenarioManager manager, GUIManager gui, NickManager nick, SpectatorManager spec) {
		
		this.settings = settings;
		this.game = game;
		this.data = data;
		this.arena = arena;
		this.tp = tp;
        this.manager = manager;
		this.gui = gui;
		this.nick = nick;
		this.spec = spec;
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		
		if(e.getEntity() instanceof Player && arena.isInArena((Player) e.getEntity())){
			return;
		}
		
		if(e.getEntity().getWorld() == Bukkit.getWorld("lobby")){
			e.setCancelled(true);
		}
		
		if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e){
		if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onWeather(WeatherChangeEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING)){
			e.setCancelled(true);
		}else{
			if(arena.isInArena(e.getPlayer())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onMobsKilling(EntityDeathEvent e){
		
		if(e.getEntity().getKiller() instanceof Player){
			if(e.getEntity().getKiller().getGameMode() == GameMode.SURVIVAL){
				
				String uuid = e.getEntity().getKiller().getUniqueId().toString();
				
				if(e.getEntity().getType() == EntityType.COW){
					data.getStatsToStoreByUUID(uuid).addCow();
				}else if(e.getEntity().getType() == EntityType.CHICKEN){
					data.getStatsToStoreByUUID(uuid).addChicken();
				}else if(e.getEntity().getType() == EntityType.PIG){
					data.getStatsToStoreByUUID(uuid).addPig();
				}
			}
		}
	}

	@EventHandler
	public void onLeafDecay(LeavesDecayEvent e){
		Random r1 = new Random();
		Block b = e.getBlock();

		int randomNumber = r1.nextInt(maxChances-minChances) + minChances;
		if(b.getTypeId() == Material.LEAVES.getId() || b.getTypeId() == Material.LEAVES_2.getId()){
			Leaves leafData = new Leaves();
			leafData.setData(b.getData());
			e.setCancelled(true);
			b.setType(Material.AIR);
			if(randomNumber < 4){
				b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.APPLE));
			}
		}
	}

	@EventHandler
	public void onFlintRate(BlockBreakEvent e){
		Random r1 = new Random();
		Block b = e.getBlock();

		if(b.getType() == Material.GRAVEL){

			if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().isInGame(e.getPlayer())){
				e.setCancelled(true);
				return;
			}

			int randomNumber = r1.nextInt(maxChances-minChances) + minChances;
			e.setCancelled(true);
			b.setType(Material.AIR);
			if(randomNumber < 13){
				b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.FLINT));
			}else{
				b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.GRAVEL));
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		if(e.getClickedInventory() != null && e.getClickedInventory().getName() != null){
			if(e.getClickedInventory().getName().contains("Stats for")){
				e.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(p.getWorld() == Bukkit.getWorld("lobby")){
			if(p.getLocation().getBlockY() < 60){
				p.teleport(new Location(Bukkit.getWorld("lobby"), 0, Bukkit.getWorld("lobby").getHighestBlockYAt(new Location(Bukkit.getWorld("lobby"), 0, 100, 0)) + 1, 0).setDirection(new Vector(-5, 0, 0)));
			}
		}else if(p.getWorld() == Bukkit.getWorld("arena")){
			if(p.getLocation().getBlockY() < 50){
				p.damage(9999);
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(e.getPlayer().getWorld() == Bukkit.getWorld("lobby") || e.getPlayer().getWorld() == Bukkit.getWorld("arena")){
			e.setCancelled(true);
			return;
		}
		
		if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING)){
			e.setCancelled(true);
			return;
		}
		
		if(e.getBlock().getType() == Material.DIAMOND_ORE){
			data.getStatsToStoreByUUID(e.getPlayer().getUniqueId().toString()).addDiamond();
		}else if(e.getBlock().getType() == Material.GOLD_ORE){
			data.getStatsToStoreByUUID(e.getPlayer().getUniqueId().toString()).addGold();
		}else if(e.getBlock().getType() == Material.IRON_ORE){
			data.getStatsToStoreByUUID(e.getPlayer().getUniqueId().toString()).addIron();
		}
	}
	
	@EventHandler
	public void onPortal(PortalCreateEvent e){
		if(!settings.getNether()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		if(e.getCause() == TeleportCause.NETHER_PORTAL){
			if(!settings.getNether()){
				e.setCancelled(true);
			}else{
				if(game.getCurrentBorder() <= 500){
					e.setCancelled(true);
					e.getPlayer().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't go to nether after 500x500 scatter.");
					return;
				}
				Location to = e.getTo();
				if(to.getWorld() == Bukkit.getWorld("world")){
					tp.teleport(game.getCurrentBorder(), to.getWorld(), e.getPlayer());
				}else if(to.getWorld() == Bukkit.getWorld("world_nether")){
					tp.teleport(game.getCurrentBorder() / 8, to.getWorld(), e.getPlayer());
				}
			}
		}
	}
	
	@EventHandler
	public void onGoldenAppleAndHeadConsuming(PlayerItemConsumeEvent e){
		
		Player p = e.getPlayer();
		
		if(!arena.isInArena(p)){
			if(p.getGameMode() == GameMode.SURVIVAL){
				if(e.getItem().getType() == Material.GOLDEN_APPLE){
					if(e.getItem().hasItemMeta()){
						if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Golden Head")){
							data.getStatsToStoreByUUID(e.getPlayer().getUniqueId().toString()).addGHead();
						}
					}else{
						data.getStatsToStoreByUUID(e.getPlayer().getUniqueId().toString()).addGapple();
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onHorseTaming(VehicleEnterEvent e){
		if(e.getEntered() instanceof Player){
			Entity ent = e.getVehicle();
			Player p = (Player) e.getEntered();
			
			if(ent.equals(EntityType.HORSE)){
				if(settings.getHorses()){
					data.getStatsToStoreByUUID(p.getUniqueId().toString()).addHorseTamed();
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPvP(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getDamager() instanceof Player){
				
				Player p = (Player) e.getDamager();
				
				if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING) || !Bukkit.getWorld("world").getPVP()){
					if(!arena.isInArena(p)){
						e.setCancelled(true);
						p.sendMessage(ChatColor.RED + "PVP is actually disabled.");
					}
				}
			}else if(e.getDamager() instanceof Arrow){
				
				Arrow arrow = (Arrow) e.getDamager();
				
				if(arrow.getShooter() instanceof Player){
					Player p = (Player) arrow.getShooter();
					
					if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING) || !Bukkit.getWorld("world").getPVP()){
						if(!arena.isInArena(p)){
							e.setCancelled(true);
							p.sendMessage(ChatColor.RED + "PVP is actually disabled.");
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){

		if(GameState.isState(GameState.LOBBY)){
			e.setCancelled(true);
			return;
		}

		if(arena.isInArena(e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onAnimalsFill(PlayerInteractEntityEvent e){
		if(!settings.getHorseHealing()){
			if(e.getRightClicked() instanceof Horse){
				if(e.getPlayer().getItemInHand().getType() == Material.BREAD){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPotionDrink(final PlayerItemConsumeEvent e){
		ItemStack it = e.getItem();
		if(it.getType() == Material.POTION){
			Potion potion = Potion.fromItemStack(it);
			PotionEffectType pet = potion.getType().getEffectType();
			if(pet == PotionEffectType.INCREASE_DAMAGE){
				if(!settings.getStrength()){
					e.setCancelled(true);
					e.getPlayer().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cStrength potions are actually disabled !");
				}
			}else if(pet == PotionEffectType.INVISIBILITY){
				if(!settings.getInvisibility()){
					e.setCancelled(true);
					e.getPlayer().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvisibility potions are actually disabled !");
				}
			}
		}
		else if(it.getType() == Material.GOLDEN_APPLE){
			
			if(!settings.getAbsorbtion()){
				new BukkitRunnable() {
					public void run() {
						e.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
					}
				}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 2);
			}
			if(it.getItemMeta().getDisplayName() == "Golden Head"){
				if(!settings.getGoldenHeads()){
					e.setCancelled(true);
					e.getPlayer().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cGolden Heads are actually disabled !");
				}
			}
			else if(it.getData().getData() == (byte)1){
				if(!settings.getGodApples()){
					e.setCancelled(true);
					e.getPlayer().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cGod Apples are actually disabled !");
				}
			}
		}
	}
	
	@EventHandler
	public void on(PlayerItemConsumeEvent event) {
		final Player player = event.getPlayer();
		final float before = player.getSaturation();
		
		if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING)){
			return;
		}

		new BukkitRunnable() {
			public void run() {
				final float change = player.getSaturation() - before;
				
				player.setSaturation((float) (before + change * 2.5D));
			}
        }.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 1);
	}

	@EventHandler
	public void on(AsyncPlayerChatEvent e){
		
		final Player p = e.getPlayer();

        if(e.isCancelled()){
            return;
        }

        e.setCancelled(true);

		final String name = e.getPlayer().getName();

		if(cooldown.contains(name) && !e.getPlayer().hasPermission("api.chat.bypass")){
			e.setCancelled(true);
			e.getPlayer().sendMessage("§cPlease wait before writing another message.");
			return;
		}

        if(game.isMuted() && !p.hasPermission("uhc.supertalk")){
            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThe chat is currently disabled.");
            return;
        }

        if(nick.isNicked(p) && game.isMuted()){
            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThe chat is currently disabled.");
            return;
        }

        PermissionUser user = PermissionsEx.getUser(p.getName());
        OniziacPlayer oniziacPlayer = OniziacPlayer.getPlayer(p.getName());

        String color = oniziacPlayer.getColor();
        boolean nicked = nick.isNicked(p);

        String prefix = nicked ? "§9" : (user.getPrefix().replace("&", "§") + color);

        if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){

            Bukkit.broadcastMessage(prefix + p.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.RESET + e.getMessage());

        }else{
            if(specChat.contains(p.getName())){
                for(Player pls : Bukkit.getOnlinePlayers()){
                    if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(pls)){
                        pls.sendMessage("§b[Spectator-Chat]" + prefix + p.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.RESET + e.getMessage());
                    }
                }
            }else{
                for(Player pls : Bukkit.getOnlinePlayers()){
                    pls.sendMessage(prefix + p.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.RESET + e.getMessage());
                }
            }
        }

		cooldown.add(name);
		new BukkitRunnable(){
			public void run(){
				cooldown.remove(name);
			}

		}.runTaskLater(Main.getInstance(), 30);

		
	}
	
	@EventHandler
	public void on(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();
		final Random rand = new Random();
		
		if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING)){
			event.setCancelled(true);
			return;
		}
		
		if (event.getFoodLevel() < player.getFoodLevel()) {
			event.setCancelled(rand.nextInt(100) < 80);
	    }
	}
	
	@EventHandler
	public void onGHeadCrafting(CraftItemEvent e){
		
		final Player p = (Player) e.getWhoClicked();
		
		ItemStack result = e.getCurrentItem();
		
		if(!settings.getGoldenHeads()){
			if(result != null && result.getType() == Material.GOLDEN_APPLE && result.getItemMeta().getDisplayName().equalsIgnoreCase("Golden Head")){
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + " §cGolden Heads are actually disabled");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onHeadEat(PlayerItemConsumeEvent e){
		Player p = e.getPlayer();
		if(settings.getGoldenHeads()){
			if(e.getItem().getType().equals(Material.GOLDEN_APPLE) & e.getItem().hasItemMeta()){
				if(e.getItem().getItemMeta().getDisplayName().equals("Golden Head")){
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onShoot(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Arrow){
			Arrow a = (Arrow) e.getDamager();
			if(a.getShooter() instanceof Player){
				if(e.getEntity() instanceof Player){
					Player shooter = (Player) a.getShooter();
					Player victim = (Player) e.getEntity();

					if(!GameState.isState(GameState.GAME) && !arena.isInArena(shooter) && !arena.isInArena(victim)){
						return;
					}

					if(victim != shooter && victim.getHealth() - e.getFinalDamage() > 0){
						shooter.sendMessage(ChatColor.AQUA + victim.getName() + " §3is now at §b" + MessageUtils.getHearts(victim.getHealth(), e.getFinalDamage()) + " " + MessageUtils.HEART_WITH_COLOR);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBucket(PlayerBucketEmptyEvent e){
		Player p = e.getPlayer();
		if(e.getBucket() == Material.LAVA_BUCKET){
			if(!p.getWorld().getPVP()){
				e.setCancelled(true);
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlease do not use lava buckets before PVP activation.");
			}
		}
	}
	
	@EventHandler
	public void onInteractFnS(PlayerInteractEvent e){
		Player p = e.getPlayer();

		if(e.getItem() == null){
			return;
		}

		if(GameState.isState(GameState.LOBBY) && e.getItem().hasItemMeta()){
            if(e.getItem().getItemMeta().hasDisplayName()){
                if(e.getItem().getItemMeta().getDisplayName().contains("UHC Settings") && p.hasPermission("uhc.host")){
                    if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
                        gui.getGUI("Settings").open(p);
                    }
                }
            }
		}
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK){
			return;
		}
		
		if(e.getItem().getType() != Material.FLINT_AND_STEEL){
			return;
		}
		
		if(p.getWorld().getPVP()){
			return;
		}
		
		e.setCancelled(true);
		p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPlease do not use any flint and steel before PVP activation.");
	}

	@EventHandler
	public void onPvP(PVPEvent e){

	}

    @EventHandler
    public void on(ServerListPingEvent e){

        int borderSize = game.getRadius();
        List<String> motd = new ArrayList<>();

        motd.add(game.getHostName());
        motd.add(StringUtils.capitalize(GameState.getState().toString()));
        motd.add(manager.getStringOfEnabledScenarios());
        motd.add((game.getTeamSize() == 1 ? "FFA" : "TO" + game.getTeamSize()));
        motd.add(borderSize + "x" + borderSize);
        motd.add("" + Bukkit.getOnlinePlayers().size());
        motd.add("" + settings.getMaxPlayers());


        StringBuilder list = new StringBuilder("");
        for(String s : motd){
            list.append(s + "}");
        }

        e.setMotd(list.toString().trim());

    }


}
