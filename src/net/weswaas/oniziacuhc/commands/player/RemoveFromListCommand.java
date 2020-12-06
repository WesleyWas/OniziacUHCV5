package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 23/04/2020.
 */
public class RemoveFromListCommand extends UHCCommand{

    public RemoveFromListCommand() {
        super("removefromlist", "/removefromlist <name>");
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("uhc.superadmin")){
                if(args.length == 1){
                    Player player = net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayerByName(args[0]);
                    if(player != null){
                        net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().remove(player);
                        p.sendMessage("§aPlayer has been removed from the list. Thank you for taking care of the UHC.");
                    }else{
                        p.sendMessage("This player seems not to be in the UHC.");
                    }
                }else{
                    p.sendMessage("§cWrong synthax.");
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
