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

public class TeamSizeGUI extends GUI implements Listener{
	
	private GUIManager gm;
	private Game game;

	public TeamSizeGUI(GUIManager gm, Game game) {
		super("TeamSize", "A GUI to change the UHC teams size");
		this.gm = gm;
		this.game = game;
	}
	
	private Inventory inv;

	@Override
	public void create() {
		
		inv = Bukkit.createInventory(null, 36, "§4Change team size");
		
	}

	@Override
	public void fill(Player p) {
		
		ItemStack current = new ItemBuilder(Material.ANVIL).name("§aCurrent team size: " + (game.getTeamSize() == 1 ? "FFA" : "To" + game.getTeamSize())).build();
		
		ItemStack one = new ItemBuilder(Material.PAPER).amount(1).name("§aChange to FFA").build();
		ItemStack two = new ItemBuilder(Material.PAPER).amount(2).name("§aChange to To2").build();
		ItemStack three = new ItemBuilder(Material.PAPER).amount(3).name("§aChange to To3").build();
		ItemStack four = new ItemBuilder(Material.PAPER).amount(4).name("§aChange to To4").build();
		ItemStack five = new ItemBuilder(Material.PAPER).amount(5).name("§aChange to To5").build();
		ItemStack six = new ItemBuilder(Material.PAPER).amount(6).name("§aChange to To6").build();
		ItemStack seven = new ItemBuilder(Material.PAPER).amount(7).name("§aChange to To7").build();
		ItemStack eight = new ItemBuilder(Material.PAPER).amount(8).name("§aChange to To8").build();
		ItemStack nine = new ItemBuilder(Material.PAPER).amount(9).name("§aChange to To9").build();
		ItemStack ten = new ItemBuilder(Material.PAPER).amount(10).name("§aChange to To10").build();
		ItemStack back = new ItemBuilder(Material.PAPER).amount(1).name("§aBack").build();
		
		inv.setItem(4, current);
		inv.setItem(10, one);
		inv.setItem(11, two);
		inv.setItem(12, three);
		inv.setItem(13, four);
		inv.setItem(14, five);
		inv.setItem(15, six);
		inv.setItem(16, seven);
		inv.setItem(19, eight);
		inv.setItem(20, nine);
		inv.setItem(21, ten);
		inv.setItem(35, back);
		
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
		
		if(item.getItemMeta().getDisplayName().contains("FFA")){
			game.setTeamSize(1);
		}else if(item.getItemMeta().getDisplayName().contains("Back")){
			gm.getGUI("Settings").open(p);
		}else{
			game.setTeamSize(item.getAmount());
		}
		
		fill(p);
		p.updateInventory();
	}

}
