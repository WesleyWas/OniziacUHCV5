package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigCommand extends UHCCommand{
	
	private Game game;
	private Settings settings;
	private ScenarioManager manager;
	private Timer timer;

	public ConfigCommand(Game game, Settings settings, ScenarioManager manager, Timer timer) {
		super("config", "/config");
		
		this.game = game;
		this.settings = settings;
		this.manager = manager;
		this.timer = timer;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			int radius = game.getRadius();
			
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "========= UHC Config =========");
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§0");
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Horse healing: " + (settings.getHorseHealing() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Max players: " + (settings.getMaxPlayers()));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Map radius: " + radius);
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Nether: " + (settings.getNether() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "iPvP: §cdisabled");
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Stalking: Allowed, but §cnot excessively");
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Stats: " + (settings.getStats() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Absorbtion: " + (settings.getAbsorbtion() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Horses: " + (settings.getHorses() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Shears: §cdoesn't work for apples");
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Strength potions: " + (settings.getStrength() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Invisibility potions: " + (settings.getInvisibility() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Golden heads: " + (settings.getGoldenHeads() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "GodApples: " + (settings.getGodApples() ? "enabled" : "§cdisabled"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Crossteaming: " + (game.getTeamSize() > 1 ? "enabled" : "§cdisabled, it's FFA"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Apple rates: 2%");
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Team size: " + game.getTeamSize());
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Heal: " + (timer.getHeal() <= 0 ? "§calready given !" : "in " + timer.getHeal() + " minutes"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "PVP: " + (timer.getPvP() <= 0 ? "§calready enabled !" : "in " + timer.getPvP() + " minutes"));
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "First border shrink: " + (timer.getFirstBorder() <= 0 ? "§calready shrinked !" : "in " + timer.getFirstBorder() + " minutes"));
			p.sendMessage("§0");
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "==============================");
		}
		
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
