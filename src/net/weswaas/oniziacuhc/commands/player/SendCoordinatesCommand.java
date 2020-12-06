package net.weswaas.oniziacuhc.commands.player;

import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weswas on 21/04/2020.
 */
public class SendCoordinatesCommand extends UHCCommand {

    public SendCoordinatesCommand() {
        super("sc", "/sc");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {


        if (sender instanceof Player) {

            Player p = (Player) sender;
            if (Team.hasTeam(p)) {
                for (String s : Team.getTeam(p).getPlayerNames()) {
                    Player teamPlayer = Bukkit.getPlayer(s);
                    Location loc = p.getLocation();
                    teamPlayer.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§b[Team]" + p.getName() + ": " + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                }
                return true;
            }

        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

}
