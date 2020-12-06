package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BloodDiamonds extends Scenario implements Listener{

	public BloodDiamonds() {
		super("BloodDiamonds", "For each diamond mined, you loose half a heart");
		slot = 21;
		setMaterial(new ItemBuilder(Material.DIAMOND).name("§aBloodDiamonds").build());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(BlockBreakEvent e){
		Player p = e.getPlayer();
		
		if(e.getBlock().getType() != Material.DIAMOND_ORE){
			return;
		}
		
		p.damage(1);
	}

}
