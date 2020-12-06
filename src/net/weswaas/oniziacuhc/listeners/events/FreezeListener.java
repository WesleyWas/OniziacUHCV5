package net.weswaas.oniziacuhc.listeners.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class FreezeListener implements Listener{
	
	public static ArrayList<Player> frozenPlayers = new ArrayList<Player>();
	
	@EventHandler
	public void on(FreezeEvent e){
		Player freezer = e.getFreezer();
		Player frozen = e.getFrozen();
		
		if(frozenPlayers.contains(frozen)){
			freezer.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + frozen.getName() + " is already frozen.");
			return;
		}
		
		frozenPlayers.add(frozen);
		frozen.setWalkSpeed(0);
		frozen.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999999, 127));
		frozen.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have been frozen. Please listen to the hosts instructions. Logout will result in a permanent ban.");
		freezer.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have froze " + frozen.getName());
	}
	
	@EventHandler
	public void onUnfreeze(UnfreezeEvent e){
		Player frozen = e.getFrozen();
		Player freezer = e.getFreezer();
		
		if(!frozenPlayers.contains(frozen)){
			freezer.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + frozen.getName() + " is not frozen.");
			return;
		}
		
		for(PotionEffect pe : frozen.getActivePotionEffects()){
			frozen.removePotionEffect(pe.getType());
		}
		frozenPlayers.remove(frozen);
		frozen.setWalkSpeed(0.2f);
		frozen.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have been unfreezed.");
		freezer.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have unfreeze " + frozen.getName());
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(frozenPlayers.contains(p)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getDamager() instanceof Player){
				if(frozenPlayers.contains(e.getEntity())){
					e.setCancelled(true);
				}
				if(frozenPlayers.contains(e.getDamager())){
					e.setCancelled(true);
				}
			}
		}
	}

}
