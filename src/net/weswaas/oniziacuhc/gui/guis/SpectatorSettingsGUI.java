package net.weswaas.oniziacuhc.gui.guis;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.gui.GUI;
import net.weswaas.oniziacuhc.managers.MiningNotifManager;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SpectatorSettingsGUI extends GUI implements Listener{
	
	private SpectatorManager sm;
	private MiningNotifManager mnm;

	public SpectatorSettingsGUI(SpectatorManager sm, MiningNotifManager mnm) {
		super("SpectatorSettingsGUI", "A GUI to manage spectator settings");
		
		this.sm = sm;
		this.mnm = mnm;
	}
	
	private Inventory inv;

	@Override
	public void create() {
		
		inv = Bukkit.createInventory(null, 9, "§4SpectatorSettingsGUI");
		
	}

	@SuppressWarnings("static-access")
	@Override
	public void fill(Player p) {
		
		ItemStack chat = new ItemBuilder(Material.PAPER).name("§aChat type").lore("§8» §7With this feature are you able to change").lore("§8» §7The visibility of your chat messages,").lore("§8» §7And choose between global chat, or only-spectator chat.").lore("§8» §7Actually: " + (sm.specChat.contains(p.getName()) ? "§aSpecChat" : "§aGlobalChat")).build();
		ItemStack notifs = new ItemBuilder(Material.REDSTONE_TORCH_ON).name("§aMining notifications").lore("§8» §7With this feature are you able to toggle").lore("§8» §7The mining notifications").lore("§8» §7Actually: " + (mnm.notified.contains(p) ? "§aenabled" : "§cdisabled")).build();
		
		inv.setItem(1, chat);
		inv.setItem(2, notifs);
	}

	@Override
	public void open(Player p) {
		fill(p);
		
		p.openInventory(inv);
	}
	
	@SuppressWarnings("static-access")
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
		
		if(item.getItemMeta().getDisplayName().contains("Chat type")){
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have switched your chat type to " + (sm.specChat.contains(p.getName()) ? "global" : "spectator only"));
			sm.switchChat(p);
		}else if(item.getItemMeta().getDisplayName().contains("Mining notifications")){
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have switched your mining notifications to " + (mnm.notified.contains(p) ? "§cfalse" : "true"));
			mnm.switchNotifs(p);
		}
		
		fill(p);
		
		p.updateInventory();
	}

}
