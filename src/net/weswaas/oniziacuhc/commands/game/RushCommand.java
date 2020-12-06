package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.utils.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 26/04/2020.
 */
public class RushCommand extends UHCCommand{

    private Timer timer;
    private Game game;

    public RushCommand(Timer timer, Game game) {
        super("rush", "/rush");

        this.timer = timer;
        this.game = game;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("uhc.admin")){
                if(GameState.isState(GameState.LOBBY)){
                    if(game.getHost().getName().equalsIgnoreCase(p.getName())){
                        toggle();
                    }else{
                        p.sendMessage(OniziacUHC.PREFIX + "§cYou have to be host to perform this command.");
                    }
                }else{
                    p.sendMessage(OniziacUHC.PREFIX + "§cYou can't toggle PvP before the start.");
                }
            }
        }

        return false;
    }

    private void toggle(){

        if(!game.isRush()){
            game.setRush(true);
            timer.setHeal(5);
            timer.setPvP(10);
            timer.setFirstBorder(25);
            Bukkit.broadcastMessage(OniziacUHC.PREFIX + "The game is now a RUSH Game.");
            Bukkit.broadcastMessage(OniziacUHC.PREFIX + "PVP will be enabled at 10 minutes, final heal will be given at 5 minutes, and the border will begin to shrink at 25 minutes. Good luck all !");
        }else{
            game.setRush(false);
            timer.setHeal(10);
            timer.setPvP(20);
            timer.setFirstBorder(35);
            Bukkit.broadcastMessage(OniziacUHC.PREFIX + "The game is now a NORMAL Game.");
            Bukkit.broadcastMessage(OniziacUHC.PREFIX + "PVP will be enabled at 20 minutes, final heal will be given at 10 minutes, and the border will begin to shrink at 35 minutes. Good luck all !");
        }

        for(Player pls : Bukkit.getOnlinePlayers()){
            new SoundsUtils(pls).playSounds(Sound.NOTE_PLING);
        }

    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

}
