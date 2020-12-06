package net.weswaas.oniziacuhc.gui.guis;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.Game;
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

public class BorderRadiusGUI extends GUI implements Listener{
	
	private GUIManager gm;
	private Game game;

	public BorderRadiusGUI(GUIManager gm, Game game) {
		super("BorderRadius", "A GUI to set the border radius");
		
		this.gm = gm;
		this.game = game;
	}
	
	private Inventory inv;

	@Override
	public void create() {
		
		inv = Bukkit.createInventory(null, 18, "§4BorderRadiusGUI");
		
	}

	@Override
	public void fill(Player p) {
		
		ItemStack troismille = new ItemBuilder(Material.WOOL).data((byte)8).name("§aSet the border to 3000x3000").build();
		ItemStack deuxvirgulecinq = new ItemBuilder(Material.WOOL).data((byte)8).name("§aSet the border to 2500x2500").build();
		ItemStack deuxmille = new ItemBuilder(Material.WOOL).data((byte)8).name("§aSet the border to 2000x2000").build();
		ItemStack unvirgulecinq = new ItemBuilder(Material.WOOL).data((byte)8).name("§aSet the border to 1500x1500").build();
		ItemStack mille = new ItemBuilder(Material.WOOL).data((byte)8).name("§aSet the border to 1000x1000").build();
		ItemStack count = new ItemBuilder(Material.BEDROCK).amount(1).name("§aBorder is actually set to: §6" + game.getRadius()).build();
		ItemStack back = new ItemBuilder(Material.ARROW).amount(1).name("§aBack").build();
		
		inv.setItem(2, troismille);
		inv.setItem(3, deuxvirgulecinq);
		inv.setItem(4, deuxmille);
		inv.setItem(5, unvirgulecinq);
		inv.setItem(6, mille);
		inv.setItem(13, count);
		inv.setItem(17, back);
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
		
		if(game.isGenerated()){
			if(!item.getItemMeta().getDisplayName().contains("Back")){
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't change radius once the chunks are generated.");
				return;
			}
		}
		
		if(item.getItemMeta().getDisplayName().contains("3000x3000")){
			game.setRadius(3000);
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Border radius has been set to 3000x3000.");
		}else if(item.getItemMeta().getDisplayName().contains("2500x2500")){
			game.setRadius(2500);
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Border radius has been set to 2500x2500.");
		}else if(item.getItemMeta().getDisplayName().contains("2000x2000")){
			game.setRadius(2000);
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Border radius has been set to 2000x2000.");
		}else if(item.getItemMeta().getDisplayName().contains("1500x1500")){
			game.setRadius(1500);
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Border radius has been set to 1500x1500.");
		}else if(item.getItemMeta().getDisplayName().contains("1000x1000")){
			game.setRadius(1000);
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Border radius has been set to 1000x1000.");
		}else if(item.getItemMeta().getDisplayName().contains("Back")){
			gm.getGUI("Settings").open(p);
		}
		
		fill(p);
		
		p.updateInventory();
	}

}
