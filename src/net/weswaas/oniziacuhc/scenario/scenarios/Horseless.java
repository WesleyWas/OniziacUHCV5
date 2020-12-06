package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

public class Horseless extends Scenario implements Listener{
	
	private Settings settings;

	public Horseless(Settings settings) {
		super("Horseless", "Horses are disabled");
		this.settings = settings;
		slot = 24;
		setMaterial(new ItemBuilder(Material.SADDLE).name("§aHorseless").build());
	}
	
	@EventHandler
	public void onHorse(EntityMountEvent e){
		if(!isEnabled()){
			return;
		}
		
		if(!(e.getEntity() instanceof Player)){
			return;
		}
		
		Player p = (Player) e.getEntity();
		
		if(e.getMount().getType() != EntityType.HORSE){
			return;
		}
		
		e.setCancelled(true);
		p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "�cHorses are disabled.");
	}
	
	@Override
	public void onEnable() {
		settings.setHorses(false);
	}
	
	@Override
	public void onDisable() {
		settings.setHorses(true);
	}

}
