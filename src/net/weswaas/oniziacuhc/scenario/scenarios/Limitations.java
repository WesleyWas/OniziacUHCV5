package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;

public class Limitations extends Scenario implements Listener{
	
	public HashMap<String, Integer> pDiamond = new HashMap<String, Integer>();
	public HashMap<String, Integer> pGold = new HashMap<String, Integer>();
	public HashMap<String, Integer> pIron = new HashMap<String, Integer>();

	public Limitations() {
		super("Limitations", "A player can mine a maximum of 16 diamonds, 32 golds and 64 irons.");
		slot = 23;
		setMaterial(new ItemBuilder(Material.BEACON).name("§aLimitations").build());
	}
	
	@EventHandler
	public void onMine(BlockBreakEvent e){
		
		Player p = e.getPlayer();
		Material mat = e.getBlock().getType();
		if(mat != Material.DIAMOND_ORE && mat != Material.GOLD_ORE && mat != Material.IRON_ORE){
			return;
		}
		
		if(!pDiamond.containsKey(p.getName())){
			pDiamond.put(p.getName(), 0);
		}
		if(!pGold.containsKey(p.getName())){
			pGold.put(p.getName(), 0);
		}
		if(!pIron.containsKey(p.getName())){
			pIron.put(p.getName(), 0);
		}
		
		int diamond = pDiamond.get(p.getName());
		int gold = pGold.get(p.getName());
		int iron = pIron.get(p.getName());
		
		if(mat == Material.DIAMOND_ORE){
			if(diamond >= 16){
				e.setCancelled(true);
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't mine diamond anymore.");
			}else{
				pDiamond.put(p.getName(), pDiamond.get(p.getName()) + 1);
			}
		}
		else if(mat == Material.GOLD_ORE){
			if(gold >= 32){
				e.setCancelled(true);
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't mine gold anymore.");
			}else{
				pGold.put(p.getName(), pGold.get(p.getName()) + 1);
			}
		}
		else if(mat == Material.IRON_ORE){
			if(iron >= 64){
				e.setCancelled(true);
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't mine iron anymore.");
			}else{
				pIron.put(p.getName(), pIron.get(p.getName()) + 1);
			}
		}
		
	}

}
