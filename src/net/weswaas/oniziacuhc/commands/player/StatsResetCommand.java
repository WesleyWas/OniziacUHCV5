package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.stats.PlayerData;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.stats.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 01/05/2020.
 */
public class StatsResetCommand extends UHCCommand{

    private SQLManager sql;
    private PlayerDataManager data;

    public StatsResetCommand(SQLManager sql, PlayerDataManager data) {
        super("statsreset", "/statsreset <player>");
        this.sql = sql;
        this.data = data;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("uhc.admin")){
                if(args.length == 1){
                    OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
                    if(sql.hasAccount(op.getUniqueId().toString())){

                        String uuid = op.getUniqueId().toString();

                        if(data.getPlayerData(uuid) != null){
                            PlayerData original = data.getPlayerData(uuid);
                            data.datas.remove(original);
                        }

                        if(data.pData.containsKey(uuid)){
                            PlayerData original = data.pData.get(uuid);
                            data.pData.remove(original);
                        }

                        PlayerData pData = new PlayerData(op.getName(), uuid, 0, 0, "0.00", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        data.datas.add(pData);
                        data.pData.put(uuid, pData);
                        data.sendStatsToBase(uuid, pData);

                        p.sendMessage(OniziacUHC.PREFIX + op.getName() + "'s stats have been reset.");

                        if(op.isOnline()){
                            Player target = op.getPlayer();
                            target.sendMessage(OniziacUHC.PREFIX + "Your stats have been reset.");
                        }

                    }else{
                        p.sendMessage(OniziacUHC.PREFIX + "§cThis player has never played on OniziacNetwork");
                    }
                }else{
                    p.sendMessage(OniziacUHC.PREFIX + "§cInvalid synthax.");
                }
            }
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

}
