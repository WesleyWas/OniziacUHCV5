package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.listeners.events.StarterFoodEvent;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class InfiniteEnchanter extends Scenario implements Listener{

	public InfiniteEnchanter() {
		super("InfiniteEnchanter", "Everyone begins the game with 64 anvils, 64 enchantement tables, 64 bookshelves and a lot of levels");
		setMaterial(new ItemBuilder(Material.ENCHANTMENT_TABLE).name("§aInifniteEnchanter").build());
		slot = 12;
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
			pls.getInventory().remove(Material.ENCHANTMENT_TABLE);
			pls.getInventory().remove(Material.BOOKSHELF);
			pls.getInventory().remove(Material.ANVIL);
			pls.setLevel(0);
		}
	}
	
	@EventHandler
	public void on(StarterFoodEvent e){
		for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
			pls.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
			pls.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
			pls.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
			pls.setLevel(20000);
		}
	}

}
