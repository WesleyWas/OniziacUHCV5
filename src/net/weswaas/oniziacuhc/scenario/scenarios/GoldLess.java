package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.utils.BlockUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class GoldLess extends Scenario implements Listener{
	
	private Cutclean cc;

	public GoldLess(Cutclean cc) {
		super("GoldLess", "Gold is disabled.");
		slot = 14;
		setMaterial(new ItemBuilder(Material.GOLD_ORE).name("§aGoldLess").build());
		
		this.cc = cc;
	}
	
	@EventHandler
	public void onGoldMining(BlockBreakEvent e){
		Player p = e.getPlayer();
		Block b = e.getBlock();
		
		if(b.getType() != Material.GOLD_ORE){
			return;
		}
		
		ItemStack replace = new ItemStack(cc.isEnabled() ? Material.IRON_INGOT : Material.IRON_ORE);
		
		BlockUtils.degradeDurabiliy(p);
		BlockUtils.dropItem(b.getLocation().add(0.5, 0.7, 0.5), replace);
		e.setCancelled(true);
		b.setType(Material.AIR);
	}

}
