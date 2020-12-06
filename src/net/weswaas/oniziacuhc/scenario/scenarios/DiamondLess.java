package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.utils.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DiamondLess extends Scenario implements Listener{
	
	private Cutclean cc;

	public DiamondLess(Cutclean cc) {
		super("DiamondLess", "Diamonds are disabled.");
		slot = 15;
		setMaterial(new ItemBuilder(Material.DIAMOND_ORE).name("�aDiamondLess").build());
		
		this.cc = cc;
	}
	
	@EventHandler
	public void onDiamondMining(BlockBreakEvent e){
		Player p = e.getPlayer();
		Block block = e.getBlock();
		
		if(block.getType() != Material.DIAMOND_ORE){
			return;
		}
		
		ItemStack replace = new ItemStack(cc.isEnabled() ? Material.IRON_INGOT : Material.IRON_ORE);
		
		BlockUtils.degradeDurabiliy(p);
		BlockUtils.dropItem(block.getLocation().add(0.5, 0.7, 0.5), replace);
		e.setCancelled(true);
		block.setType(Material.AIR);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		
		Bukkit.getWorld("world").dropItem(e.getEntity().getLocation(), new ItemStack(Material.DIAMOND));
		
	}

}
