package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.ScatterManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Weswas on 26/04/2020.
 */
public class LatescatterCommand extends UHCCommand{

    public static ArrayList<UUID> hasPlayed = new ArrayList<>();

    private Timer timer;
    private ScatterManager scatter;

    public LatescatterCommand(Timer timer, ScatterManager scatter) {
        super("latescatter", "/latescatter");
        this.timer = timer;
        this.scatter = scatter;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(!hasPlayed.contains(p.getUniqueId())){
                if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().contains(p)){
                    if(GameState.isState(GameState.GAME)){
                        if(timer.getGameTimeMinutes() < 16){
                            if(!Bukkit.getWorld("world").getPVP()){
                                int gameTime = timer.getGameTimeMinutes();

                                if(gameTime > 1 && !p.hasPermission("uhc.latescatter.5") && !p.hasPermission("uhc.latescatter.10") && !p.hasPermission("uhc.admin")){
                                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cSorry, but you are not allowed to latescatter after 2 minutes.");
                                    return true;
                                }
                                else if(gameTime > 4 && !p.hasPermission("uhc.latescatter.10") && !p.hasPermission("uhc.admin")){
                                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cSorry, but you are not allowed to latescatter after 5 minutes.");
                                    return true;
                                }
                                else if(gameTime > 9 && !p.hasPermission("uhc.admin")){
                                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cSorry, but you are not allowed to latescatter after 10 minutes.");
                                    return true;
                                }

                                scatter.lateScatter(p);
                            }else{
                                p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't latescatter after PVP enabling.");
                            }
                        }else{
                            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou can't latescatter after 15 mins!");
                        }
                    }else{
                        p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThe game has not started yet !");
                    }
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
