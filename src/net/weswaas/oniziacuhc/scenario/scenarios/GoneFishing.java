package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.listeners.events.StarterFoodEvent;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GoneFishing extends Scenario implements Listener{

	public GoneFishing() {
		super("GoneFishing", "Everyone begins the game with 64 anvils, a lot of levels and a op fishing rod");
		slot = 13;
		setMaterial(new ItemBuilder(Material.FISHING_ROD).name("§aGoneFishing").build());
	}
	
	@Override
	public void onDisable() {
		for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
			pls.getInventory().remove(Material.ANVIL);
			pls.getInventory().remove(Material.FISHING_ROD);
			pls.setLevel(0);
		}
	}
	
	@EventHandler
	public void on(StarterFoodEvent e){
		
		ItemStack rod = new ItemStack(Material.FISHING_ROD);
		ItemMeta rodM = rod.getItemMeta();
		rodM.addEnchant(Enchantment.LUCK, 250, true);
		rodM.addEnchant(Enchantment.DURABILITY, 150, true);
		rodM.addEnchant(Enchantment.LURE, 250, true);
		rod.setItemMeta(rodM);
		
		for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
			pls.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
			pls.getInventory().addItem(rod);
			pls.setLevel(20000);
		}
		
	}
	
	@EventHandler
	public void on(PrepareItemCraftEvent e){
		ItemStack item = e.getRecipe().getResult();
		
		if(item.getType() != Material.ENCHANTMENT_TABLE){
			return;
		}
		
		e.getInventory().setResult(new ItemStack(Material.AIR));
	}

}
