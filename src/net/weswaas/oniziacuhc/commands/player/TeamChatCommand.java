package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 21/04/2020.
 */
public class TeamChatCommand extends UHCCommand {

    public TeamChatCommand() {
        super("tc", "/tc <message>");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {


        if (sender instanceof Player) {

            Player p = (Player) sender;

            if(args.length != 0){
                if (Team.hasTeam(p)) {
                    if(GameState.isState(GameState.GAME)){
                            if(Team.hasTeam(p)){
                                if(args.length > 0){

                                    StringBuilder list = new StringBuilder("");

                                    for(String s : args){
                                        list.append(s + " ");
                                    }

                                    for(String s : Team.getTeam(p).getPlayerNames()){
                                        Player pls = Bukkit.getPlayer(s);
                                        pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§b[TeamChat]" + p.getName() + "§8 »§f " + list.toString().trim());
                                    }

                                }else{
                                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /team chat <sentence>");
                                }
                            }else {
                                p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou need a team to talk with them.");
                            }
                    }
                } else {
                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou don't have a team.");
                }
            }else{
                p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Try with /tc <message>");
            }
        }
        return false;

    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

}

