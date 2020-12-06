package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 01/05/2020.
 */
public class AlphaCommand extends UHCCommand{

    private Game game;

    public AlphaCommand(Game game){
        super("alpha", "/alpha");
        this.game = game;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("uhc.admin")){
                if(args.length == 0){
                    if(GameState.isState(GameState.LOBBY)){
                        if(game.isStats()){
                            game.setStats(false);
                            game.setGenerated(true);
                            p.sendMessage(OniziacUHC.PREFIX + "The game is now in alpha mode.");
                        }else{
                            game.setStats(true);
                            game.setGenerated(false);
                            p.sendMessage(OniziacUHC.PREFIX + "The game is not in alpha mode anymore.");
                        }
                    }else{
                        p.sendMessage(OniziacUHC.PREFIX + "§cThe game has already begin.");
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
