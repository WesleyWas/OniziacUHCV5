package net.weswaas.oniziacuhc.gui.guis;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.*;
import net.weswaas.oniziacuhc.gui.GUI;
import net.weswaas.oniziacuhc.gui.GUIManager;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SettingsGUI extends GUI implements Listener{
	
	private Settings settings;
	private GUIManager gm;
	private Game game;
	private Timer timer;
	private SpectatorManager sm;
	
	public SettingsGUI(Settings settings, GUIManager gm, Game game, Timer timer, SpectatorManager sm) {
		super("Settings", "An inventory to manage the UHC");
		
		this.settings = settings;
		this.gm = gm;
		this.game = game;
		this.timer = timer;
		this.sm = sm;
	}
	
	private Inventory inv;

	@Override
	public void create() {
		
		inv = Bukkit.createInventory(null, 54, "§4Settings GUI");
		
	}
	
	public Inventory getInventory(){
		return inv;
	}
	
	public void fill(Player p){
		
		ItemStack maxplayers = new ItemBuilder(Material.SKULL_ITEM).amount(settings.getMaxPlayers()).name("§aMax players").lore("§8» §7Set the max players for this UHC.").lore("§aActually: " + settings.getMaxPlayers()).lore("§4It can't be changed once the chunks has been generated.").data((byte)3).build();
		ItemStack horseHealing = new ItemBuilder(Material.BREAD).amount(1).name("§aHorse Healing: " + (settings.getHorseHealing() ? "§aenabled" : "§cdisabled")).lore("§8» §7Toggle the horse healing").build();
		ItemStack strength = new ItemBuilder(Material.BLAZE_POWDER).amount(1).name("§aStrength potions: " + (settings.getStrength() ? "§aenabled" : "§cdisabled")).lore("§8» §7Toggle the strength potions").build();
		ItemStack invisibility = new ItemBuilder(Material.GOLDEN_CARROT).amount(1).name("§aInvisibility potions: " + (settings.getInvisibility() ? "§aenabled" : "§cdisabled")).lore("§8» §7Toggle the invisivility potions").build();
		ItemStack absorbtion = new ItemBuilder(Material.APPLE).amount(1).name("§aAbsorbtion : " + (settings.getAbsorbtion() ? "§aenabled" : "§cdisabled")).lore("§8» §7Toggle the absorbtion").build();
		ItemStack goldenHeads = new ItemBuilder(Material.GOLDEN_APPLE).amount(1).name("§aGolden heads: " + (settings.getGoldenHeads() ? "§aenabled" : "§cdisabled")).lore("§8» §7Toggle the golden heads").build();
		ItemStack godApples = new ItemBuilder(Material.GOLDEN_APPLE).amount(1).data((byte)1).name("§aGod apples: " + (settings.getGodApples() ? "§aenabled" : "§cdisabled")).lore("§8» §7Toggle god apples").build();
		ItemStack scenarios = new ItemBuilder(Material.CHEST).amount(1).name("§aScenarios").lore("§7Manage different scenarios").build();
		ItemStack chunks = new ItemBuilder(Material.SAPLING).amount(1).name("§aChunks generation").lore("§8» §aChunks status: " + (game.isGenerated() ? "§agenerated" : "§cNot generated.")).lore(game.isGenerated() ? "" : "§cClick here to generate them").build();
		ItemStack host = new ItemBuilder(Material.CACTUS).amount(1).name(game.hasHost() ? "§aHost is " + game.getHost().getName() : "§cHost is not set.").lore("§8» Click here to set you as host.").build();
		ItemStack border = new ItemBuilder(Material.BEDROCK).amount(timer.getFirstBorder()).name("§aFirst border schrink").lore("§8» §7First border is actually set to:").lore("§8» §6" + timer.getFirstBorder()).build();
		ItemStack radius = new ItemBuilder(Material.SLIME_BALL).amount(1).name("§aBorder radius").lore("§8» §7Radius is actually " + game.getRadius() + "x" + game.getRadius()).build();
		ItemStack start = new ItemBuilder(Material.ENCHANTMENT_TABLE).amount(1).name("§aStart the UHC").build();
		ItemStack visit = new ItemBuilder(Material.MAP).amount(1).name("§aVisit the 0 0 of the map").lore("§8» §7Try to keep a world when the 0;0 is a plain.").lore("§8» §7It's better to PVP at the meetup, or during the game.").lore(" ").lore("§8» §7You can click here to come back to lobby after visiting.").build();
		ItemStack pvp = new ItemBuilder(Material.DIAMOND_SWORD).amount(1).name("§aToggle the PVP").lore("§8» §7Toggle the PVP.").lore("§8» §7Actually: " + (Bukkit.getWorld("world").getPVP() ? "§aEnabled" : "§cDisabled")).build();
		ItemStack whitelist = new ItemBuilder(Material.WOOL).amount(1).name("§aWhitelist: " + (game.isOpen() ? "§aopen" : "§cclose")).data((byte) (game.isOpen() ? 5 : 14)).lore("§8» §7When the whitelist open is, can the players join the UHC.").build();
		ItemStack nether = new ItemBuilder(Material.OBSIDIAN).amount(1).name("§aNether: " + (settings.getNether() ? "enabled" : "§cdisabled")).lore("§8» §7Enable or disable the nether.").lore("§8» §7Actually: " + (settings.getNether() ? "§aenabled" : "§cdisabled")).build();
		
		inv.setItem(10, maxplayers);
		inv.setItem(11, horseHealing);
		inv.setItem(12, nether);
		inv.setItem(13, strength);
		inv.setItem(14, invisibility);
		inv.setItem(15, absorbtion);
		inv.setItem(16, goldenHeads);
		inv.setItem(22, godApples);
		inv.setItem(28, scenarios);
		inv.setItem(29, chunks);
		inv.setItem(30, host);
		inv.setItem(34, border);
		inv.setItem(32, radius);
		inv.setItem(40, start);
		inv.setItem(33, visit);
		inv.setItem(39, pvp);
		inv.setItem(41, whitelist);
	}

	@Override
	public void open(Player p){
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

        if(!game.isHost(p.getDisplayName()) && item.getType() != Material.CACTUS){
            e.setCancelled(true);
            p.sendMessage(OniziacUHC.PREFIX + "§cYou have to be host to perform this command.");
            return;
        }
		
		e.setCancelled(true);
		
		if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return;
		}
		
		if(GameState.isState(GameState.GAME)){
			if(!item.getItemMeta().getDisplayName().contains("PVP")){
				p.sendMessage(OniziacUHC.PREFIX + "§cImpossible to change settings once the game has begin.");
				return;
			}
		}
		
		if(item.getItemMeta().getDisplayName().contains("Max players")){
			if(!game.isGenerated()){
				gm.getGUI("MaxPlayers").open(p);
			}else{
				p.sendMessage(OniziacUHC.PREFIX + "§cYou can't change max players once the chunks has been generated.");
			}
			
		}else if(item.getItemMeta().getDisplayName().contains("Horse Healing")){
			settings.setHorseHealing(!settings.getHorseHealing());
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Horse Healing has been set to " + String.valueOf(settings.getHorseHealing()));
		}else if(item.getItemMeta().getDisplayName().contains("Horses")){
			settings.setHorses(!settings.getHorses());
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Horses has been set to " + String.valueOf(settings.getHorses()));
		}else if(item.getItemMeta().getDisplayName().contains("Strength")){
			settings.setStrength(!settings.getStrength());
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Strength potions has been set to " + String.valueOf(settings.getStrength()));
		}else if(item.getItemMeta().getDisplayName().contains("Invisibility")){
			settings.setInvisibility(!settings.getInvisibility());
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Invisibility potions has been set to " + String.valueOf(settings.getInvisibility()));
		}else if(item.getItemMeta().getDisplayName().contains("Absorbtion")){
			settings.setAbsorbtion(!settings.getAbsorbtion());
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Absorbtion has been set to " + String.valueOf(settings.getAbsorbtion()));
		}else if(item.getItemMeta().getDisplayName().contains("Golden heads")){
			settings.setGoldenHeads(!settings.getGoldenHeads());
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Golden heads has been set to " + String.valueOf(settings.getGoldenHeads()));
		}else if(item.getItemMeta().getDisplayName().contains("God apples")){
			settings.setGodApples(!settings.getGodApples());
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "God apples has been set to " + String.valueOf(settings.getGodApples()));
		}else if(item.getItemMeta().getDisplayName().contains("Scenarios")){
			gm.getGUI("Scenario").open(p);
		}else if(item.getItemMeta().getDisplayName().contains("Chunks generation")){
			p.performCommand("chunks generate");
		}else if(item.getItemMeta().getDisplayName().contains("Host")){
			if(game.getHost() != p){
				game.setHost(p);
				sm.add(p);
				net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.remove(p);
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§3The host is now " + p.getName() + ".");
                p.setFlying(true);
			}else{
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou are already the host of this game.");
			}
		}else if(item.getItemMeta().getDisplayName().contains("border schrink")){
			gm.getGUI("FirstBorder").open(p);
		}else if(item.getItemMeta().getDisplayName().contains("Border radius")){
			gm.getGUI("BorderRadius").open(p);
		}else if(item.getItemMeta().getDisplayName().contains("Start")){
			p.performCommand("start");
		}else if(item.getItemMeta().getDisplayName().contains("Visit")){
			p.performCommand("visit");
		}else if(item.getItemMeta().getDisplayName().contains("PVP")){
			p.performCommand((Bukkit.getWorld("world").getPVP() ? "pvp false" : "pvp true"));
		}else if(item.getItemMeta().getDisplayName().contains("Whitelist")){
			p.performCommand((game.isOpen() ? "close" : "open"));
		}else if(item.getItemMeta().getDisplayName().contains("Nether")){
			settings.setNether(!settings.getNether());
			Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Nether has been " + (settings.getNether() ? "enabled" : "§cdisabled"));
		}else if(item.getItemMeta().getDisplayName().contains("Team size")){
			gm.getGUI("TeamSize").open(p);
		}

		fill(p);
		p.updateInventory();

	}

}
