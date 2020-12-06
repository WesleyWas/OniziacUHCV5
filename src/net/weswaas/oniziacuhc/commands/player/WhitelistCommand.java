package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Weswas on 25/04/2020.
 */
public class WhitelistCommand extends UHCCommand{

    public HashMap<UUID, Integer> whitelistsPerPlayer;

    public WhitelistCommand(){
        super("whitelist", "/whitelist add|remove <player>");
        whitelistsPerPlayer = new HashMap<>();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("uhc.whitelist")){
                int wlLimit = 1;
                int current = 0;
                if(p.hasPermission("uhc.whitelist.3")){
                    wlLimit = 3;
                }
                if(whitelistsPerPlayer.containsKey(p.getUniqueId())){
                    current = whitelistsPerPlayer.get(p.getUniqueId());
                }else{
                    whitelistsPerPlayer.put(p.getUniqueId(), 0);
                }


                if(args.length == 0){
                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ((wlLimit - current) == 0 ? ChatColor.RED : "") + "You have " + (wlLimit - current) + " whitelists remain.");

                }else if(args.length == 2){

                    if(args[0].equalsIgnoreCase("add")){
                        if(whitelistsPerPlayer.get(p.getUniqueId()) < wlLimit){
                            JoinListener.whitelist.add(args[1]);
                            whitelistsPerPlayer.put(p.getUniqueId(), current + 1);
                            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + args[1] + " has been added to whitelist.");
                        }else{
                            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou have already whitelisted " + current + " players.");
                        }

                    }
                    else if(args[0].equalsIgnoreCase("remove")){
                        if(current != 0){
                            String arg = args[1];
                            if(JoinListener.whitelist.contains(arg)){
                                OfflinePlayer pls = Bukkit.getOfflinePlayer(args[1]);
                                if(!pls.isOnline()){
                                    JoinListener.whitelist.remove(arg);
                                    whitelistsPerPlayer.put(p.getUniqueId(), current - 1);
                                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + args[1] + " has been removed from the whitelist.");
                                }else{
                                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't remove a player from the whitelist if he already joined the game.");
                                }
                            }else{
                                p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + ChatColor.RED + arg + " was not whitelisted.");
                            }
                        }else{
                            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou did not whitelist anybody.");
                        }

                    }else{
                        p.sendMessage("§cWrong synthax. Please try with /whitelist add <player>");
                    }

                }else{
                    p.sendMessage("§cWrong synthax. Please try with /whitelist add <player>");
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
