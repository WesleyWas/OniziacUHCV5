package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Bowless extends Scenario implements Listener{

	public Bowless() {
		super("Bowless", "Bows are deactivated");
		slot = 16;
		setMaterial(new ItemBuilder(Material.BOW).name("§aBowless").build());
	}
	
	@EventHandler
	public void on(PlayerInteractEvent e){
		if(e.getItem() != null){
			if(e.getItem().getType() == Material.BOW){
				if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
					e.setCancelled(true);
					e.getPlayer().sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cBows are currently deactivated !");
				}
			}	
		}
	}
	
	@EventHandler
	public void onItemCraft(CraftItemEvent e){
		
		Player p = (Player) e.getWhoClicked();
		
		ItemStack item = e.getCurrentItem();
		
		if(item != null && item.getType() != Material.BOW){
			return;
		}
		
		p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cBows are currently deactivated !");
		e.setCancelled(true);
		
	}

}
