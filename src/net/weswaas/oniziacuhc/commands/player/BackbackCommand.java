package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 21/04/2020.
 */
public class BackbackCommand extends UHCCommand{

    private ScenarioManager scen;

    public BackbackCommand(ScenarioManager scen){
        super("bp", "/bp");

        this.scen = scen;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            if(scen.getScenario("Backpacks").isEnabled()){
                if(GameState.isState(GameState.GAME)){
                    if(Team.hasTeam(p)){
                        Team team = Team.getTeam(p);
                        team.getBackPack().open(p);
                    }else{
                        p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou don't have a team.");
                    }
                }else{
                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cBackpack is only available during the match.");
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
