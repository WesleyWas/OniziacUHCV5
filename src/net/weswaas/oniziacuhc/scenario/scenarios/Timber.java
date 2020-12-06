package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.utils.BlockUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Timber extends Scenario implements Listener{

	public Timber() {
		super("Timber", "When you break a tree, all the logs breaks.");
		slot = 31;
		setMaterial(new ItemBuilder(Material.SAPLING).name("Timber").build());
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		
		Block b = e.getBlock();
		
		if(b.getType() != Material.LOG && b.getType() != Material.LOG_2){
			return;
		}

		if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getSpec().isSpectator(e.getPlayer().getName())){
			e.setCancelled(true);
			return;
		}

		if(!GameState.isState(GameState.GAME)){
			e.setCancelled(true);
			return;
		}
		
		b = b.getRelative(BlockFace.UP);
        Block bd = e.getBlock().getRelative(BlockFace.DOWN);
        Block be = e.getBlock().getRelative(BlockFace.EAST);
        Block bw = e.getBlock().getRelative(BlockFace.WEST);
        Block bn = e.getBlock().getRelative(BlockFace.NORTH);
        Block bs = e.getBlock().getRelative(BlockFace.SOUTH);
		
		while(b.getType() == Material.LOG || b.getType() == Material.LOG_2){
			BlockUtils.blockBreak(null, b);
			b.breakNaturally();
			b = b.getRelative(BlockFace.UP);
		}

        while(bd.getType() == Material.LOG || bd.getType() == Material.LOG_2){
            BlockUtils.blockBreak(null, bd);
            bd.breakNaturally();
            bd = bd.getRelative(BlockFace.DOWN);
        }
		
	}

}
