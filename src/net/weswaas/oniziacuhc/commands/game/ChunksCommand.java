package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.ScatterManager;
import net.weswaas.oniziacuhc.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weswas on 12/11/2016.
 */
public class ChunksCommand extends UHCCommand{

    private Game game;
    private ScatterManager sm;
    private Settings settings;
    private WorldUtils wu;

    public ChunksCommand(Game game, ScatterManager sm, Settings settings, WorldUtils wu) {
        super("chunks", "/chunks generate");

        this.game = game;
        this.sm = sm;
        this.settings = settings;
        this.wu = wu;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("uhc.chunks.generate")){

                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("generate")){
                        if(!game.isGenerated()){

                            ArrayList<Location> locs = sm.findLocations(game.getRadius(), settings.getMaxPlayers());
                            game.setLocs(locs);
                            wu.generateChunks(p, locs);
                            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Chunks generation has been started.");
                            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Please just let yourself be teleported and be AFK.");
                            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "It should take something like " + (settings.getMaxPlayers() * 2) + " seconds.");

                        }else{
                            p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cChunks are already generated.");
                        }
                    }else{
                        p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /chunks generate.");
                    }
                }else{
                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cInvalid synthax. Please try with /chunks generate.");
                }

            }
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> toReturn = new ArrayList<String>();

        if(args.length == 1){
            toReturn.add("generate");
        }

        return toReturn;
    }

}
