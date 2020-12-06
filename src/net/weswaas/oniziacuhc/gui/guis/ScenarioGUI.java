package net.weswaas.oniziacuhc.gui.guis;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.gui.GUI;
import net.weswaas.oniziacuhc.gui.GUIManager;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ScenarioGUI extends GUI implements Listener{
	
	private ScenarioManager manager;
	private GUIManager gm;

	public ScenarioGUI(ScenarioManager manager, GUIManager gm) {
		super("Scenario", "A GUI to manage scenarios");
		
		this.manager = manager;
		this.gm = gm;
	}
	
	private Inventory inv;

	@Override
	public void create() {
		
		inv = Bukkit.createInventory(null, 54, "§4Scenarios");
		
	}

	@Override
	public void fill(Player p) {
		
		for(Scenario scen : manager.getScenarios()){
			int slot = scen.getSlot();
			
			if(slot >= inv.getSize() || slot < 0){
				continue;
			}
			
			inv.setItem(slot, scen.getItem());
		}
		
		inv.setItem(53, new ItemBuilder(Material.ARROW).name("§aBack").build());
		
	}

	@Override
	public void open(Player p) {
		fill(p);
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void on(InventoryClickEvent e){
		
		ItemStack item = e.getCurrentItem();
		Inventory inv = e.getClickedInventory();
		Player p = (Player) e.getWhoClicked();
		
		if(item == null){
			return;
		}
		
		if(!this.inv.getTitle().equals(inv.getTitle())){
			return;
		}
		
		e.setCancelled(true);
		
		if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return;
		}
		
		if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§aBack")){
			
			gm.getGUI("Settings").open(p);
			
		}else{

			Scenario scen = manager.getScenarioByMaterial(item.getType());
			scen.toggle();
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§3" + manager.getScenarioByMaterial(item.getType()).getName() + " has been " + (manager.getScenarioByMaterial(item.getType()).isEnabled() == true ? "§aenabled" : "§cdisabled"));
			
		}
		
		fill(p);
		p.updateInventory();
		
	}

}
