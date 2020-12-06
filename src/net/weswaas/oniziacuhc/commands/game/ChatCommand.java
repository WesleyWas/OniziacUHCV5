package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 25/06/2019.
 */
public class ChatCommand extends UHCCommand{

    private Game game;

    public ChatCommand(Game game){
        super("chat", "/chat mute");
        this.game = game;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args){

        if(sender instanceof Player){
            Player p  = (Player) sender;
                if(p.hasPermission("uhc.host")) {
                    if(args.length == 0){
                        p.sendMessage(OniziacUHC.PREFIX + "The chat is currently " + (game.isMuted() ? "muted" : "not muted"));
                    }
                    else if(args.length == 1){
                        if(GameState.isState(GameState.SCATTERING)){
                            p.sendMessage(OniziacUHC.PREFIX + "§cYou can't change the chat status while scattering.");
                        }else{
                            if(args[0].equalsIgnoreCase("mute")){
                                if(!game.isMuted()){
                                    Bukkit.broadcastMessage(OniziacUHC.PREFIX + "§cThe chat has been muted.");
                                    game.setMuted(true);
                                }else{
                                    p.sendMessage(OniziacUHC.PREFIX + "§cThe chat is already muted.");
                                }
                            }
                            else if(args[0].equalsIgnoreCase("demute")){
                                if(game.isMuted()){
                                    Bukkit.broadcastMessage(OniziacUHC.PREFIX + "§aThe chat is now enabled.");
                                    game.setMuted(false);
                                }else{
                                    p.sendMessage(OniziacUHC.PREFIX + "§cThe chat is already demuted.");
                                }
                            }
                        }
                    }else{
                        p.sendMessage(OniziacUHC.PREFIX + "§cInvalid synthax. Please try /chat [<mute>/<demute>]");
                    }
                }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        // TODO Auto-generated method stub
        return null;
    }

}
