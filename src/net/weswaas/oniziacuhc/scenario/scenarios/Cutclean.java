package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import net.weswaas.oniziacuhc.utils.BlockUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class Cutclean extends Scenario implements Listener{
	
	private ScenarioManager manager;
	
	public Cutclean(ScenarioManager manager) {
		super("Cutclean", "No furnaces required. Items requiring cooking drops their cooked variety.");
		
		setMaterial(new ItemBuilder(Material.IRON_INGOT).name("§aCutclean").build());
		slot = 10;
		this.manager = manager;
	}

	@EventHandler
	public void onCCFood(EntityDeathEvent e){
		
		final List<ItemStack> drops = e.getDrops();
		final Entity entity = e.getEntity();
		
		if (entity instanceof Ageable && !((Ageable) entity).isAdult()) {
			drops.clear();
			return;
		}
		
		if (entity instanceof Cow) {
			drops.clear();
			drops.add(new ItemStack(Material.COOKED_BEEF, 2));
			drops.add(new ItemStack(Material.LEATHER, 1));
			return;
		} 
			
		if (entity instanceof Chicken) {
			drops.clear();
			drops.add(new ItemStack(Material.COOKED_CHICKEN, 2));
			drops.add(new ItemStack(Material.FEATHER, 1));
			return;
		}
		
		if (entity instanceof Pig) {
			drops.clear();
			drops.add(new ItemStack(Material.GRILLED_PORK, 2));
			return;
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Block block = e.getBlock();
		
		if(e.getPlayer().getGameMode() == GameMode.CREATIVE){
			return;
		}
	
		if(block.getType() == Material.IRON_ORE){
			if(manager.getScenario("TripleOres").isEnabled()){
				return;
			}

			if(e.getPlayer().getItemInHand().getType() == Material.WOOD_PICKAXE){
				return;
			}

			e.getBlock().getDrops().clear();
			block.setType(Material.AIR);
			BlockUtils.dropItem(block.getLocation().add(0.5, 0.7, 0.5), new ItemStack(Material.IRON_INGOT));
			
			ExperienceOrb exp = (ExperienceOrb) block.getWorld().spawn(block.getLocation(), ExperienceOrb.class);
			exp.setExperience(1);
		}
		else if(block.getType() == Material.GOLD_ORE){
			if(manager.getScenario("TripleOres").isEnabled()){
				return;
			}

			if(e.getPlayer().getItemInHand().getType() == Material.WOOD_PICKAXE || e.getPlayer().getItemInHand().getType() == Material.STONE_PICKAXE){
				return;
			}

			e.setCancelled(true);
			block.setType(Material.AIR);
			BlockUtils.dropItem(block.getLocation().add(0.5, 0.7, 0.5), new ItemStack(Material.GOLD_INGOT));
			
			ExperienceOrb exp = (ExperienceOrb) block.getWorld().spawn(block.getLocation(), ExperienceOrb.class);
			exp.setExperience(2);
		}
		else if(block.getType() == Material.GRAVEL){
			e.setCancelled(true);
			e.getBlock().getDrops().clear();
			block.setType(Material.AIR);
			BlockUtils.dropItem(block.getLocation().add(0.5, 0.7, 0.5), new ItemStack(Material.FLINT));
		}else if(block.getType() == Material.POTATO){
			block.getDrops().clear();
			block.setType(Material.AIR);
			BlockUtils.dropItem(block.getLocation().add(0.5, 0.7, 0.5), new ItemStack(Material.BAKED_POTATO, 1 + new Random().nextInt(2)));
		}
		else if(block.getType() == Material.SAND){
			e.setCancelled(true);
			block.setType(Material.AIR);
			BlockUtils.dropItem(block.getLocation().add(0.5, 0.7, 0.5), new ItemStack(Material.SAND));
		}
	}

}
