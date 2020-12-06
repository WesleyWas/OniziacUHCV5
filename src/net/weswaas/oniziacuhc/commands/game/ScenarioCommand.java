package net.weswaas.oniziacuhc.commands.game;

import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.commands.CommandException;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ScenarioCommand extends UHCCommand{
	
	private net.weswaas.oniziacuhc.OniziacUHC plugin;
	private ScenarioManager manager;

	public ScenarioCommand(net.weswaas.oniziacuhc.OniziacUHC plugin, ScenarioManager manager) {
		super("scenario", "/scenario <enable | disable> <scenario>");
		
		this.manager = manager;
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("enable")){
				if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING)){
					if(sender.hasPermission("uhc.scenario.manage")){
						if(args.length == 1){
							return false;
						}
						
						Scenario scen = manager.getScenario(args[1]);
						
						if(scen == null){
							try {
								throw new CommandException(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§c'" + args[1] + "' is not a scenario.");
							} catch (CommandException e) {
								e.printStackTrace();
							}
						}
						
						if(scen.isEnabled()){
							sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + " §c" + scen.getName() + "is already activated !");
							return true;
						}
						
						Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§l" + scen.getName() + "§r§3 has been activated !");
						scen.enable(plugin);
						return true;
					}
				}else{
					sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cCan't manage the scenarios once the game has started.");
				}
			}
			
			if(args[0].equalsIgnoreCase("disable")){
				if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.SCATTERING)){
					if(sender.hasPermission("uhc.scenario.manage")){
						if(args.length == 1){
							return false;
						}
						
						Scenario scen = manager.getScenario(args[1]);
						
						if(scen == null){
							try {
								throw new CommandException(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§c'" + args[1] + "' is not a scenario.");
							} catch (CommandException e) {
								e.printStackTrace();
							}
						}
						
						if(!scen.isEnabled()){
							sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§l§3'" + scen.getName() + "' §r§cis not activated");
							return true;
						}
						
						Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§a§l" + scen.getName() + " §r§3has been desactivated !");
						scen.disable();
						return true;
					}
				}else{
					sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cCan't manage the scenarios once the game has started.");
				}
			}
			
			if(args[0].equalsIgnoreCase("list")){
				StringBuilder list = new StringBuilder("");
				int i = 1;
				
				for(Scenario scen : manager.getScenarios()){
					if(list.length() > 0){
						if(i == manager.getScenarios().size()){
							list.append("§8 and §7");
						}else{
							list.append("§8, §7");
						}
					}
					
					list.append((scen.isEnabled() ? "§a" : "§c") + scen.getName());
					i++;
				}
				
				sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + " §2List of all scenarios: §8(§6" + manager.getScenarios().size() + "§8)");
				sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §7" + list.toString().trim());
				return true;
			}
			
			if(args[0].equalsIgnoreCase("info")){
				if(args.length == 1){
					return false;
				}
				
				Scenario scen = manager.getScenario(args[1]);
				
				if(scen == null){
					try {
						throw new CommandException(net.weswaas.oniziacuhc.OniziacUHC.PREFIX +"§c'" + args[1] + "' is not a scenario");
					} catch (CommandException e) {
						e.printStackTrace();
					}
				}
				
				sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Informations about §3" + scen.getName() + "§3: ");
				sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "!8» §f§o" + scen.getDescription());
				return true;
			}
		}
		
		int size = manager.getEnabledScenarios().size();
		
		sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Activated scenarios: §8(§6" + (size == 0 ? 0 : size) + "§8)");
		
		if(size == 0){
			sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThis UHC doesn't contains any specials scenarios.");
			return true;
		}
		
		for(Scenario scen : manager.getEnabledScenarios()){
			sender.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §7" + scen.getName() + " §8- §f§o" + scen.getDescription());
		}
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		
		List<String> toReturn = new ArrayList<String>();
		
		if(args.length == 1){
			toReturn.add("list");
			toReturn.add("info");
			
			if(sender.hasPermission("uhc.scenario.manage")){
				toReturn.add("enable");
				toReturn.add("disable");
			}
		}
		
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("info")){
				for(Scenario scen : manager.getScenarios()){
					toReturn.add(scen.getName());
				}
			}
			
			if(!sender.hasPermission("uhc.scenario.manage")){
				return null;
			}
			
			if(args[0].equalsIgnoreCase("enable")){
				for(Scenario scen : manager.getScenarios()){
					toReturn.add(scen.getName());
				}
			}else if(args[0].equalsIgnoreCase("disable")){
				for(Scenario scen : manager.getEnabledScenarios()){
					toReturn.add(scen.getName());
				}
			}
		}
		
		return toReturn;
	}

}
