package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.NickManager;
import net.weswaas.oniziacuhc.stats.PlayerData;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.stats.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.UUID;

public class StatsCommand extends UHCCommand{
	
	private PlayerDataManager data;
	private SQLManager sql;
	private NickManager nick;

	public StatsCommand(PlayerDataManager data, SQLManager sql, NickManager nick) {
		super("stats", "/stats <player>");
		this.data = data;
		this.sql = sql;
		this.nick = nick;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(args.length == 0){
				
				PlayerData pData = data.getPlayerData(p.getUniqueId().toString());
				
				Inventory inv = data.getStatsInventory(p.getUniqueId().toString(), pData);
				p.openInventory(inv);
				
			}else if(args.length == 1){

				UUID uuid;

				OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
				String name = op.getName();
				if(nick.nameNick.containsKey(name)){
					String trueName = nick.getOfficialName(name);
					uuid = Bukkit.getOfflinePlayer(trueName).getUniqueId();
				}else{
					uuid = op.getUniqueId();
				}

				if(sql.hasAccount(uuid.toString())){

					if(!data.isCreated(uuid.toString())){
						sql.createPlayerData(uuid.toString(), name);
					}

					PlayerData pData = data.getPlayerData(uuid.toString());

					Inventory inv = data.getStatsInventory(uuid.toString(), pData);

					p.openInventory(inv);

				}else{
					p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThis player has never played on OniziacNetwork.");
				}
				
			}else{
				p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /stats <player>");
			}
			
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
