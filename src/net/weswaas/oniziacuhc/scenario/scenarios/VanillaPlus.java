package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Leaves;

import java.util.Random;

public class VanillaPlus extends Scenario implements Listener{
	
	private int maxChances = 100;
	private int minChances = 1;

	public VanillaPlus() {
		super("VanillaPlus", "Apples and flints drops are increased");
		slot = 20;
		setMaterial(new ItemBuilder(Material.APPLE).name("§aVanillaPlus").build());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLeafDecay(LeavesDecayEvent e){
		Random r1 = new Random();
		Block b = e.getBlock();
		
		int randomNumber = r1.nextInt(maxChances-minChances) + minChances;
		if(b.getTypeId() == Material.LEAVES.getId()){
			Leaves leafData = new Leaves();
			leafData.setData(b.getData());
			if(leafData.getSpecies() == TreeSpecies.GENERIC){
				e.setCancelled(true);
				b.setType(Material.AIR);
				if(randomNumber < 4){
					b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.APPLE));
				}
			}
		}
	}
	
	@EventHandler
	public void onFlintRate(BlockBreakEvent e){
		Random r1 = new Random();
		Block b = e.getBlock();
		
		if(b.getType() == Material.GRAVEL){
			int randomNumber = r1.nextInt(maxChances-minChances) + minChances;
			e.setCancelled(true);
			b.setType(Material.AIR);
			if(randomNumber < 13){
				b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.FLINT));
			}else{
				b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.GRAVEL));
			}
		}
	}

}
