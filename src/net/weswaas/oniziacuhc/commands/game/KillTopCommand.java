package net.weswaas.oniziacuhc.commands.game;

import com.weswaas.api.utils.ValueComparator;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.listeners.DeathListener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class KillTopCommand extends UHCCommand{


	public KillTopCommand() {
		super("killtop", "/killtop");
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(!GameState.isState(GameState.LOBBY)){
				
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "============ TOP 10 KILLS ============");

                ValueComparator bvc = new ValueComparator(DeathListener.pKills);
                TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
                sorted_map.putAll(DeathListener.pKills);
				
				for(int i = 1; i < 11; i++){

                    Entry<String, Integer> e = sorted_map.pollFirstEntry();

					String pName = e.getKey();
					int score = e.getValue();
					if(pName.contains("None") && score == 0){
						p.sendMessage(OniziacUHC.PREFIX + "§e[§a" + i + ".§e] " + "§aNone - 0");
					}else{
						p.sendMessage(OniziacUHC.PREFIX + "§e[§a" + i + ".§e] §a" + pName + " - " + score);
					}
				}
				
			}else{
				p.sendMessage(OniziacUHC.PREFIX + "§cThe game has not started yet !");
			}
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
