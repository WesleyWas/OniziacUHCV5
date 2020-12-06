package net.weswaas.oniziacuhc.managers;

import com.weswaas.api.functions.OniziacPlayer;
import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.gui.GUIManager;
import net.weswaas.oniziacuhc.listeners.DeathListener;
import net.weswaas.oniziacuhc.stats.PlayerData;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

@SuppressWarnings("deprecation")
public class SpectatorManager implements Listener{
	
	private Game game;
	private GUIManager gui;
	private Timer timer;
	private PlayerDataManager data;
	
	public SpectatorManager(Game game, GUIManager gui, Timer timer, PlayerDataManager data) {
		
		this.game = game;
		this.gui = gui;
		this.timer = timer;
		this.data = data;
	}
	
	public Location spawn;
	public static ArrayList<String> specs = new ArrayList<String>();
	public static ArrayList<Player> tempSpecs = new ArrayList<Player>();
	public ArrayList<String> preparedPlayers = new ArrayList<String>();
	public static ArrayList<String> specChat = new ArrayList<String>();
	public Location spawnLoc = new Location(Bukkit.getWorld("lobby"), 0, 101, 0).setDirection(new Vector(-5, 0, 0));
	
	public void toggle(Player p){
		if(specs.contains(p)){
			remove(p);
		}else{
			add(p);
		}
	}
	
	public boolean isSpectator(String name){
		return specs.contains(name) || tempSpecs.contains(Bukkit.getPlayer(name));
	}
	
	public void remove(Player p){
		
		net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.add(p);
		p.setGameMode(GameMode.SURVIVAL);
		p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You are now a player. You are counted in the in-game players.");
		p.setFlying(false);
		if(GameState.isState(GameState.LOBBY)){
			p.teleport(spawnLoc);
		}else{
			p.teleport(new Location(Bukkit.getWorld("world"), 0, 101, 0).setDirection(new Vector(-5, 0, 0)));
		}
		p.setHealth(20);
		p.setFoodLevel(20);
		setVanish(false, p);
		removeItems(p);
		if(p == game.getHost()){
			game.setHost(null);
		}
		specChat.remove(p.getName());
		if(specs.contains(p.getName())){
			specs.remove(p.getName());
		}
	}
	
	public void add(Player p){
		
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.remove(p);
		p.setGameMode(GameMode.CREATIVE);
		p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You are now a spectator.");
		p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You are vanished, and can't interact with anything.");
		p.teleport((GameState.isState(GameState.LOBBY) ? new Location(Bukkit.getWorld("lobby"), 0.5, 101, 0.5).setDirection(new Vector(-5, 0, 0)) : spawn));
		setVanish(true, p);
		if(!p.hasPermission("uhc.admin.chat")){
			specChat.add(p.getName());
		}
		if(!specs.contains(p.getName())){
			specs.add(p.getName());
		}
		
		addItems(p);
		
	}
	
	public ArrayList<String> getSpecs(){
		return specs;
	}
	
	public ArrayList<String> getPreparedPlayers(){
		return preparedPlayers;
	}
	
	private void removeItems(Player p){
		p.getInventory().remove(Material.WATCH);
		if(p.hasPermission("uhc.spec.admin")){
			p.getInventory().remove(Material.REDSTONE_COMPARATOR);
			p.getInventory().remove(Material.DIAMOND_CHESTPLATE);
			p.getInventory().remove(Material.BED);
			p.getInventory().remove(Material.EYE_OF_ENDER);
		}
	}
	
	private void setVanish(boolean vanish, Player p){
		if(vanish){
			for(Player pls : Bukkit.getOnlinePlayers())
				pls.hidePlayer(p);
		}else{
			for(Player pls : Bukkit.getOnlinePlayers()){
				pls.showPlayer(p);
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		
		int x = p.getLocation().getBlockX();
		int y = p.getLocation().getBlockY();
		int z = p.getLocation().getBlockZ();
		int limit;
		
		if(!specs.contains(p.getName())){
			return;
		}
		
		if(p.getWorld() != Bukkit.getWorld("world")){
			return;
		}
		
		if(p.hasPermission("uhc.staff")){
			limit = game.getRadius();
		}
		else if(p.hasPermission("uhc.spec.500")){
			limit = 500;
		}
		else if(p.hasPermission("uhc.spec.100")) {
			limit = 100;
		}
		else if(p.hasPermission("uhc.spec")){
			limit = 50;
		}
		else{
			limit = 50;
		}
		
		if(y < 40){
			if(!p.hasPermission("uhc.staff")){
				p.teleport(p.getLocation().add(0, 2, 0));
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cWith your rank are you not able to spec underground.");
			}
		}
		
		if(x > limit || x < limit - limit * 2 || z > limit || z < limit - limit * 2){
			
			if(x > limit){
				p.teleport(p.getLocation().add(-2, 0, 0));
			}
			if(x < limit - limit * 2){
				p.teleport(p.getLocation().add(2, 0, 0));
			}
			if(z > limit){
				p.teleport(p.getLocation().add(0, 0, -2));
			}
			if(z < limit - limit * 2){
				p.teleport(p.getLocation().add(0, 0, 2));
			}
			
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cWith your rank are you not able to go out of " + limit + "x" + limit + " as a spectator.");
		}
		
	}
	
	public void addItems(Player p){
		
		if(p.hasPermission("uhc.spec.admin")){
			ItemStack settings = new ItemBuilder(Material.REDSTONE_COMPARATOR).name("§aSettings").lore("§8» §7Change your spectator settings").build();
			ItemStack random = new ItemBuilder(Material.EYE_OF_ENDER).name("§aRandom teleport").lore("§8» §7With this feature, you can").lore("§8» §7teleport you to a random player").build();
			ItemStack checkstuff = new ItemBuilder(Material.DIAMOND_CHESTPLATE).name("§aCheck Stuff").lore("§8» §7With this feature, you can").lore("§8» §7check the stuff of the players").amount(4).build();
			p.getInventory().setItem(8, settings);
			p.getInventory().setItem(3, random);
			p.getInventory().setItem(5, checkstuff);

            if(game.isHost(p.getDisplayName())){
                p.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).name("§3UHC Settings").build());
            }

		}

		ItemStack middle = new ItemBuilder(Material.BED).name("§aTeleport to the center").build();
        ItemStack players = new ItemBuilder(Material.WATCH).name(ChatColor.DARK_AQUA + "Players in " + ChatColor.AQUA + getPermission(p) + "x" + getPermission(p)).build();

        p.getInventory().setItem(0, players);
		p.getInventory().setItem(1, middle);
	}

	private int getPermission(Player p){
        int permission = 50;
        if(p.hasPermission("uhc.spec.100")){
            permission = 100;
        }
        if(p.hasPermission("uhc.spec.500")){
            permission = 500;
        }
        if(p.hasPermission("uhc.staff")){
            permission = game.getCurrentBorder();
        }
        return permission;
    }
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(!specs.contains(p.getName())){
			return;
		}
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR){
			return;
		}
		
		if(e.getClickedBlock() != null){
			if(e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST || e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.BREWING_STAND
					){
				e.setCancelled(true);
			}
			
		}
		
		if(e.getItem() == null){
			return;
		}
		
		if(e.getItem().getType() == Material.MONSTER_EGG || e.getItem().getType() == Material.MONSTER_EGGS || e.getItem().getType() == Material.DRAGON_EGG || e.getItem().getType() == Material.EGG || e.getItem().getType() == Material.SNOW_BALL || e.getItem().getType() == Material.FISHING_ROD){
			e.setCancelled(true);
		}
		
		if(!e.getItem().hasItemMeta()){
			return;
		}
		
		if(e.getItem().getType() == Material.REDSTONE_COMPARATOR){
			gui.getGUI("SpectatorSettingsGUI").open(p);
		}else if(e.getItem().getType() == Material.WATCH){
			if(e.getItem().getItemMeta().getDisplayName().contains("Players in")){
				openPlayerGUI(p);
				e.setCancelled(true);
			}
		}else if(e.getItem().getType() == Material.EYE_OF_ENDER){
			if(e.getItem().getItemMeta().getDisplayName().contains("Random teleport")){
				teleportToRandomPlayer(p);
				e.setCancelled(true);
			}
		}else if(e.getItem().getType() == Material.BED){
            if(e.getItem().getItemMeta().hasDisplayName()){
                if(e.getItem().getItemMeta().getDisplayName().contains("Teleport to the center")){
                    e.setCancelled(true);
                    p.teleport(new Location(Bukkit.getWorld("world"), 0, Bukkit.getWorld("world").getHighestBlockYAt(new Location(Bukkit.getWorld("world"), 0, 100, 0)) + 2, 0));
                }
            }
		}else if(e.getItem().getType() == Material.DIAMOND_CHESTPLATE){
			if(e.getItem().getItemMeta().getDisplayName().contains("Check Stuff")) {
				e.setCancelled(true);
				gui.getGUI("CheckStuff").open(p);
				removeChestplate(p);
			}

		}else{
			e.setCancelled(true);
		}
		
		
	}
	
	private void removeChestplate(final Player p){
		new BukkitRunnable() {
			public void run() {
				p.getInventory().setChestplate(null);
				p.updateInventory();
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 10);
	}
	
	private void teleportToRandomPlayer(Player p){

		if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().size() == 0){
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThere are actually no players playing the UHC.");
			return;
		}
		
		Player randomPlayer;
		Random r = new Random();
		int total = net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.size();
		int random = r.nextInt(total);
		randomPlayer = net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.get(random);
		p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Teleported you to a random player named " + randomPlayer.getPlayerListName() + " §8(" + (random + 1) + "/" + total + ")");
		p.teleport(randomPlayer.getLocation());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){

		if(e.isCancelled()){
			return;
		}

		Player p = e.getPlayer();
		OniziacPlayer oniziacPlayer = OniziacPlayer.getPlayer(p);
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(p.getUniqueId().toString());
		
		e.setCancelled(true);
		
		//if(BmAPI.isMuted(p)){
			//e.setCancelled(true);
			//return;
		//}

		
		if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
			for(Player pls : Bukkit.getOnlinePlayers()){
				pls.sendMessage(user.getPrefix().replace("&", "§") + oniziacPlayer.getColor() + p.getPlayerListName() + ChatColor.DARK_GRAY + " » " + ChatColor.RESET + e.getMessage());
			}
		}else{
			if(specChat.contains(p.getName())){
				for(Player pls : Bukkit.getOnlinePlayers()){
					if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(pls)){
						pls.sendMessage("§b[Spectator]§r" + user.getPrefix().replace("&", "§") + oniziacPlayer.getColor() + p.getPlayerListName() + " §8» §r" + e.getMessage());
					}
				}
			}else{
				for(Player pls : Bukkit.getOnlinePlayers()){
					pls.sendMessage(user.getPrefix().replace("&", "§") + oniziacPlayer.getColor() + p.getPlayerListName() + ChatColor.DARK_GRAY + " » " + ChatColor.RESET + e.getMessage());
				}
			}
		}
		
	}

	public void openPlayerGUI(final Player p){

        int permission = getPermission(p);

        final Inventory inv = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Players in " + permission + "x" + permission);

        int i = 0;
        for(Player pls : OniziacUHC.getInstance().getPlayers()){
            if(i > 53){
                break;
            }

            if(!p.hasPermission("uhc.staff") && pls.getLocation().getBlockY() < 40){
                continue;
            }

            if(isInside(permission, pls.getLocation())){
				int kills = (DeathListener.pKills.get(pls.getName()) == null ? 0 : DeathListener.pKills.get(pls.getName()));
				PlayerData pData = data.getPlayerData(pls.getUniqueId().toString());
				int wins = pData == null ? 0 : pData.getWins();
                inv.setItem(i, new ItemBuilder(Material.SKULL_ITEM).data((byte)3).name(pls.getName()).lore("§8» §3Kills: §b" + kills).lore("§8» §3Total Wins §8» §b" + wins).build());
                i++;
            }

        }

        new BukkitRunnable(){
            @Override
            public void run() {
                p.openInventory(inv);
            }
        }.runTaskLater(OniziacUHC.getInstance(), 3);

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        Player p = (Player)e.getWhoClicked();

        if(e.getClickedInventory() == null){
            return;
        }

        if(e.getClickedInventory().getName().isEmpty()){
            return;
        }

        if(!e.getClickedInventory().getName().contains("Players in")){
            return;
        }

        ItemStack item = e.getCurrentItem();

        if(item == null){
            return;
        }

        if(!item.hasItemMeta()){
            return;
        }

        if(!item.getItemMeta().hasDisplayName()){
            return;
        }

        e.setCancelled(true);

        OfflinePlayer target = Bukkit.getOfflinePlayer(item.getItemMeta().getDisplayName());

        if(target == null || !target.isOnline()){
            p.sendMessage(OniziacUHC.PREFIX + "§cThis players seems not to be online.");
        }

        Player tar = target.getPlayer();

        if(!OniziacUHC.getInstance().getPlayers().contains(tar)){
            p.sendMessage(OniziacUHC.PREFIX + "§cThis player seems not to be alive.");
            return;
        }

        if(!isSpectator(p.getName())){
            return;
        }

        p.teleport(tar);

    }

    private boolean isInside(int radius, Location loc){
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        boolean isX = false;
        boolean isZ = false;

        boolean is = false;

        if(!loc.getWorld().getName().equalsIgnoreCase("world")){
            return false;
        }

        if(x > 0){
            if(x <= radius){
                isX = true;
            }
        }else{
            if(x >= (-1 * radius)){
                isX = true;
            }
        }

        if(z > 0){
            if(z <= radius){
                isZ = true;
            }
        }else{
            if(z >= (-1 * radius)){
                isZ = true;
            }
        }

        if(isX && isZ){
            is = true;
        }

        return is;
    }
	
	public void switchChat(Player p){
		if(specChat.contains(p.getName())){
			specChat.remove(p.getName());
		}else{
			specChat.add(p.getName());
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(specs.contains(p.getName()) || tempSpecs.contains(p)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onProjectile(ProjectileLaunchEvent e){
		if(e.getEntity().getShooter() instanceof Player){
			Player p = (Player) e.getEntity().getShooter();
			if(specs.contains(p.getName())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(specs.contains(p.getName()) || tempSpecs.contains(p)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSpecDamage(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			if(specs.contains(p.getName()) || tempSpecs.contains(p)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDrops(PlayerDropItemEvent e){
		Player p = e.getPlayer();
		if(specs.contains(p.getName())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		Player p = e.getPlayer();
		if(specs.contains(p.getName()) || tempSpecs.contains(p)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void pickUpItem(PlayerPickupItemEvent e){
		if(specs.contains(e.getPlayer().getName()) || tempSpecs.contains(e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onCraftItem(CraftItemEvent e){
		Player p = (Player) e.getWhoClicked();
		if(specs.contains(p.getName())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent e){
		Player p = e.getPlayer();
		if(specs.contains(p.getName())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent e){
		Player p = e.getPlayer();
		if(specs.contains(p.getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onHVehicle(VehicleEnterEvent e){
		if(e.getEntered() instanceof Player){
			Player p = (Player) e.getEntered();
			if(specs.contains(p.getName()) || tempSpecs.contains(p.getName())){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();

		if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getSpec().isSpectator(p.getName())){
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onInt(PlayerInteractEvent e){
		if(!isSpectator(e.getPlayer().getName())){
			return;
		}

		if(e.getAction() != Action.LEFT_CLICK_BLOCK){
			return;
		}

		if(e.getPlayer().getTargetBlock((HashSet<Byte>) null, 5).getType() == Material.FIRE){
			e.setCancelled(true);
		}

	}

}
