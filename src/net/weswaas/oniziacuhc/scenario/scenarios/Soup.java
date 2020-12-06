package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Soup extends Scenario implements Listener{

	public Soup() {
		super("Soup", "Each eaten soup give you 3 hearts back.");
		slot = 25;
		setMaterial(new ItemBuilder(Material.MUSHROOM_SOUP).name("§aSoup").build());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		
		if(item == null){
			return;
		}
		
		if(item.getType() != Material.MUSHROOM_SOUP){
			return;
		}
		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			
			Damageable d = p;
			
			if(d.getHealth() > 19.9){
				return;
			}
			
			item.setType(Material.BOWL);
			
			int health = (int) (d.getHealth() + 4);
			if(health > 20){
				health = 20;
			}
			d.setHealth(health);
			p.setHealth(d.getHealth());
		}
		
	}

}
