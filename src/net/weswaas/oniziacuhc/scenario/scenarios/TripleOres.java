package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.utils.BlockUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class TripleOres extends Scenario implements Listener{
	
	private Cutclean cc;

	public TripleOres(Cutclean cc) {
		super("TripleOres", "Each ore mined give you 3 of them.");
		slot = 28;
		setMaterial(new ItemBuilder(Material.GOLD_INGOT).amount(3).name("§aTripleOres").build());
		this.cc = cc;
	}
	
	@EventHandler
	public void onMine(BlockBreakEvent e){
		
		Block b = e.getBlock();
		Player p = e.getPlayer();
		
		if(p.getGameMode() == GameMode.CREATIVE){
			return;
		}
		
		if(b.getType() == Material.COAL_ORE){
			e.setCancelled(true);
			b.getDrops().clear();
			b.setType(Material.AIR);
			BlockUtils.dropItem(b.getLocation().add(0.5, 0.7, 0.5), new ItemStack(Material.COAL, 3));
			
			ExperienceOrb exp = (ExperienceOrb) b.getWorld().spawn(b.getLocation(), ExperienceOrb.class);
			exp.setExperience(1);
		}
		
		else if(b.getType() == Material.IRON_ORE){
			e.setCancelled(true);
			b.getDrops().clear();
			b.setType(Material.AIR);
			ItemStack ccl = new ItemStack(Material.IRON_INGOT, 3);
			ItemStack noccl = new ItemStack(Material.IRON_ORE, 3);
			BlockUtils.dropItem(b.getLocation().add(0.5, 0.7, 0.5), cc.isEnabled() ? ccl : noccl);
			if(cc.isEnabled()){
				ExperienceOrb exp = (ExperienceOrb) b.getWorld().spawn(b.getLocation(), ExperienceOrb.class);
				exp.setExperience(2);
			}
		}
		
		else if(b.getType() == Material.GOLD_ORE){
			e.setCancelled(true);
			b.getDrops().clear();
			b.setType(Material.AIR);
			ItemStack ccl = new ItemStack(Material.GOLD_INGOT, 3);
			ItemStack noccl = new ItemStack(Material.GOLD_ORE, 3);
			BlockUtils.dropItem(b.getLocation().add(0.5, 0.7, 0.5), cc.isEnabled() ? ccl : noccl);
			if(cc.isEnabled()){
				ExperienceOrb exp = (ExperienceOrb) b.getWorld().spawn(b.getLocation(), ExperienceOrb.class);
				exp.setExperience(3);
			}
		}
		
		else if(b.getType() == Material.DIAMOND_ORE){
			e.setCancelled(true);
			b.getDrops().clear();
			b.setType(Material.AIR);
			BlockUtils.dropItem(b.getLocation().add(0.5, 0.7, 0.5), new ItemStack(Material.DIAMOND, 3));
			ExperienceOrb exp = (ExperienceOrb) b.getWorld().spawn(b.getLocation(), ExperienceOrb.class);
			exp.setExperience(4);
		}
		
	}

}
