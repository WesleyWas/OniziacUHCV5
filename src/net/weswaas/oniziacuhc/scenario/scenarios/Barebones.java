package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.utils.BlockUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Barebones extends Scenario implements Listener{
	
	private Cutclean cc;
	private Timer timer;

	public Barebones(Cutclean cc, Timer timer) {
		super("Barebones", "The Nether is disabled, and iron is the highest tier you can obtain through gearing up. When a player dies, they will drop 1 diamond, 1 golden apple, 32 arrows, and 2 string."
				+ " You cannot craft an enchantment table, anvil, or golden apple. Mining any ore except coal or iron will drop an iron ingot..");
		slot = 19;
		setMaterial(new ItemBuilder(Material.ANVIL).name("�aBarebones").build());
		
		this.cc = cc;
		this.timer = timer;
	}
	
	@Override
	public void onEnable() {
		timer.setHeal(5);
		timer.setPvP(10);
		timer.setFirstBorder(25);
	}
	
	@Override
	public void onDisable() {
		timer.setHeal(10);
		timer.setPvP(20);
		timer.setFirstBorder(30);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(GameState.isState(GameState.LOBBY)){
			return;
		}
		
		final Player p = e.getPlayer();
		final Block b = e.getBlock();
		
		if(p.getGameMode() == GameMode.CREATIVE){
			return;
		}
		
		ItemStack replaced = new ItemStack(cc.isEnabled() ? Material.IRON_INGOT : Material.IRON_ORE);
		
		switch (b.getType()) {
		case EMERALD_ORE:
		case REDSTONE_BLOCK:
		case GLOWING_REDSTONE_ORE:
		case LAPIS_ORE:
		case GOLD_ORE:
			break;
		case DIAMOND_ORE:
			break;
		default:
			return;
		}
		
		BlockUtils.degradeDurabiliy(p);
		BlockUtils.dropItem(b.getLocation(), replaced);
		
		e.setCancelled(true);
		b.setType(Material.AIR);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		if(GameState.isState(GameState.LOBBY)){
			return;
		}
		
		List<ItemStack> drops = e.getDrops();
		
		drops.add(new ItemStack(Material.DIAMOND, 1));
		drops.add(new ItemStack(Material.STRING, 2));
		drops.add(new ItemStack(Material.ARROW, 32));
		drops.add(new ItemBuilder(Material.GOLDEN_APPLE).name("Golden Head").build());
	}
	
	@EventHandler
	public void onCraftItem(CraftItemEvent e){
		if(GameState.isState(GameState.LOBBY)){
			return;
		}
		
		final Player p = (Player) e.getWhoClicked();
		
		final ItemStack item = e.getCurrentItem();
		
		if (item != null && item.getType() != Material.ANVIL && item.getType() != Material.GOLDEN_APPLE && item.getType() != Material.ENCHANTMENT_TABLE) {
			return;
		}
		
		p.sendMessage(ChatColor.RED + "You can't craft " + item.getType().name().toLowerCase().replace("_", " ") + ".");
		e.setCancelled(true);
	}

}
