package net.weswaas.oniziacuhc.managers;

import net.weswaas.oniziacuhc.OniziacUHC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class ArenaManager implements Listener{
	
	public ArrayList<String> arenaPlayers = new ArrayList<String>();
	public ArrayList<Location> locs = new ArrayList<Location>();
	public ArrayList<Location> availableLocs = new ArrayList<Location>();
	private boolean enabled = true;
	
	public void setInArena(Player p){
		if(isEnabled()){
			if(!isInArena(p)){
				arenaPlayers.add(p.getName());
				getKit(p);
				p.teleport(findSpawnLoc());

				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You joined the arena.");
			}else{
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are already in the arena.");
			}
		}else{
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cArena is currently disabled.");
		}
	}
	
	public void removeArena(Player p){
		if(isInArena(p)){
			arenaPlayers.remove(p.getName());
			removeKit(p);
			p.teleport(new Location(Bukkit.getWorld("lobby"), 0.5, 101, 0.5).setDirection(new Vector(-5, 0, 0)));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You left the arena.");
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.setLevel(0);
            p.setGameMode(GameMode.SURVIVAL);
            p.setExp(0);
		}else{
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are not in the arena.");
		}
	}
	
	public boolean isInArena(Player p){
		if(p != null){
			return arenaPlayers.contains(p.getName());
		}
		return false;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public Location findSpawnLoc() {

        boolean ok = false;
        World arena = Bukkit.getWorld("arena");

        while (!ok) {
            Random randX = new Random();
            Random randZ = new Random();

            int x = randX.nextInt((256 - 124) + 1) + 124;
            int z = randZ.nextInt((240 - 171) + 1) + 171;

            Location loc = new Location(arena, x, arena.getHighestBlockYAt(x, z), z);
			Location loc1 = loc.clone().add(0, -1, 0);
			Location loc2 = loc.clone().add(0, -2, 0);

            if(isValid(loc) && isValid(loc1) && isValid(loc2)){
                return loc.add(0, 2, 0);
            }

        }

        return null;
    }


    public boolean isValid(Location loc){
		Block block = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        Material mat = block.getType();

        if(mat == Material.GLASS || mat == Material.STAINED_GLASS || mat == Material.STAINED_GLASS_PANE || mat == Material.STATIONARY_LAVA || mat == Material.STATIONARY_WATER || mat == Material.LAVA || mat == Material.WATER || mat == Material.LEAVES || mat == Material.LEAVES_2
                 || mat == Material.LOG || mat == Material.LOG_2 || mat == Material.WOOL){
            return false;
        }else{
            return true;
        }

	}


	public void getKit(final Player p){
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta swordM = sword.getItemMeta();
		swordM.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
		sword.setItemMeta(swordM);
		
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 1);
		
		ItemStack rod = new ItemStack(Material.FISHING_ROD);
		
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowM = bow.getItemMeta();
		bowM.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		bow.setItemMeta(bowM);
		
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemMeta helmetM = helmet.getItemMeta();
		helmetM.addEnchant(Enchantment.PROTECTION_PROJECTILE, 2, true);
		helmet.setItemMeta(helmetM);
		
		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemMeta chestM = chest.getItemMeta();
		chestM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		chest.setItemMeta(chestM);
		
		ItemStack leg = new ItemStack(Material.IRON_LEGGINGS);
		ItemMeta legM = leg.getItemMeta();
		legM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
		leg.setItemMeta(legM);
		
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		ItemMeta bootsM = boots.getItemMeta();
		bootsM.addEnchant(Enchantment.PROTECTION_PROJECTILE, 2, true);
		boots.setItemMeta(bootsM);
		
		p.getInventory().clear();
		p.getInventory().setItem(0, sword);
		p.getInventory().setItem(1, gapple);
		p.getInventory().setItem(2, rod);
		p.getInventory().setItem(3, bow);
		p.getInventory().setItem(24, new ItemStack(Material.ARROW, 16));
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chest);
		p.getInventory().setLeggings(leg);
		p.getInventory().setBoots(boots);
		
		p.setFlying(false);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setGameMode(GameMode.SURVIVAL);
		
		p.updateInventory();
        p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cTo leave the arena, do /arena leave");
		
	}
	
	@SuppressWarnings("deprecation")
	public void removeAllFromArena(){
		
		for(final Player pls : Bukkit.getOnlinePlayers()){
			if(isInArena(pls)){
				pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cArena has been disabled.");
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        removeArena(pls);
                    }
                }.runTaskLater(OniziacUHC.getInstance(), 2);
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void removeKit(final Player p){
		
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		
		p.setFlying(false);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setGameMode(GameMode.SURVIVAL);

        new BukkitRunnable(){
            @Override
            public void run() {
                p.updateInventory();
            }
        }.runTaskLater(OniziacUHC.getInstance(), 5);

	}
	
	public ArrayList<String> getArenaPlayerNames(){
		return this.arenaPlayers;
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		
		final Player p = e.getPlayer();
		
		if(!isInArena(p)){
			return;
		}
		
		getKit(p);
		new BukkitRunnable() {
			public void run() {
				p.teleport(findSpawnLoc());
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 4);
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		
		if(!isInArena(e.getEntity()) && !isInArena(e.getEntity().getKiller())){
			return;
		}

		if(e.getEntity().getKiller() != null){
			Player killer = e.getEntity().getKiller();
			killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
			killer.getInventory().addItem(new ItemStack(Material.ARROW, 8));
		}
		e.setDeathMessage(null);
		
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityDamageEvent e){

		if(e.getEntity() instanceof Player){
			Player p = ((Player) e.getEntity()).getPlayer();
			if(isInArena(p)){
				if(e.getCause() == EntityDamageEvent.DamageCause.FALL){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		
		Player p = e.getPlayer();
		
		if(isInArena(p)){
			arenaPlayers.remove(p.getName());
			e.setQuitMessage(null);
		}
		
	}

	@EventHandler
    public void onPickup(PlayerPickupItemEvent e){
        if(isInArena(e.getPlayer())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(!isInArena(e.getPlayer())){
            return;
        }

        if(e.getPlayer().getLocation().getBlockY() < 136){
            e.getPlayer().damage(99999);
        }
    }

    @EventHandler
	public void onDrop(PlayerDropItemEvent e){
		if(!isInArena(e.getPlayer())){
			return;
		}
		e.setCancelled(true);
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}

}
