package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 12/11/2016.
 */
public class CloseCommand extends UHCCommand {

    private Game game;

    public CloseCommand(Game game) {
        super("close", "/close");

        this.game = game;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            if(p.hasPermission("uhc.setclose")){

                if(game.isOpen()){

                    game.setOpen(false);
                    Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "The UHC is now closed.");

                }else{
                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThe game is already close.");
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
