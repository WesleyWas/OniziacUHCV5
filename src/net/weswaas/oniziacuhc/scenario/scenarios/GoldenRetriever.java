package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class GoldenRetriever extends Scenario implements Listener{

	public GoldenRetriever() {
		super("GoldenRetriever", "At each kill, a golden apple is added to the drops.");
		slot = 30;
		setMaterial(new ItemBuilder(Material.GOLDEN_APPLE).name("§aGoldenRetriever").build());
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent e){
		
		if(!isEnabled()){
			return;
		}
		
		e.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
		
	}

}
