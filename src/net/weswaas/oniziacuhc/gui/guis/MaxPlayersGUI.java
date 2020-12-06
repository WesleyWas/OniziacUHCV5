package net.weswaas.oniziacuhc.gui.guis;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.gui.GUI;
import net.weswaas.oniziacuhc.gui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MaxPlayersGUI extends GUI implements Listener{
	
	private Settings settings;
	private GUIManager gm;

	public MaxPlayersGUI(Settings settings, GUIManager gm) {
		super("MaxPlayers", "A GUI to change max players");
		
		this.settings = settings;
		this.gm = gm;
	}
	
	private Inventory inv;

	@Override
	public void create() {
		
		inv = Bukkit.createInventory(null, 18, "§4Change max players");
		
	}

	@Override
	public void open(final Player p) {
		fill(p);
		
		p.openInventory(inv);
	}

	@Override
	public void fill(Player p) {
		
		ItemStack remove = new ItemBuilder(Material.WOOL).amount(-1).data((byte)14).name("§4Remove a slot").build();
		ItemStack add = new ItemBuilder(Material.WOOL).amount(1).data((byte)5).name("§aAdd a slot").build();
		ItemStack count = new ItemBuilder(Material.SKULL_ITEM).amount(settings.getMaxPlayers()).name("§aMax players: " + settings.getMaxPlayers()).data((byte)3).build();
		ItemStack back = new ItemBuilder(Material.ARROW).amount(1).name("§aBack").build();
		
		inv.setItem(2, remove);
		inv.setItem(4, count);
		inv.setItem(6, add);
		inv.setItem(17, back);
		
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
		
		if(item.getItemMeta().getDisplayName().contains("Remove a slot")){
			settings.setMaxPlayers(settings.getMaxPlayers() - 1);
		}else if(item.getItemMeta().getDisplayName().contains("Add a slot")){
			settings.setMaxPlayers(settings.getMaxPlayers() + 1);
		}else if(item.getItemMeta().getDisplayName().contains("Back")){
			gm.getGUI("Settings").open(p);
		}
		
		fill(p);
		p.updateInventory();
	}

}
