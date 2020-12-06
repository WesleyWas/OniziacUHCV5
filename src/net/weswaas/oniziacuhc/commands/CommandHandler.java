package net.weswaas.oniziacuhc.commands;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.commands.game.*;
import net.weswaas.oniziacuhc.commands.player.*;
import net.weswaas.oniziacuhc.gui.GUIManager;
import net.weswaas.oniziacuhc.listeners.events.PVPEvent;
import net.weswaas.oniziacuhc.managers.*;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.stats.SQLManager;
import net.weswaas.oniziacuhc.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements TabCompleter, CommandExecutor{
	
	private List<UHCCommand> cmds = new ArrayList<UHCCommand>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		UHCCommand command = getCommand(cmd.getName());
		
		if(command == null){
			return true;
		}
		
		try{
			if(!command.execute(sender, args)){
			}
		}catch (Exception ex){
			sender.sendMessage("§cError: " + ex.getMessage());
		}
		
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		UHCCommand command = getCommand(cmd.getName());
		
		if(command == null){
			return null;
		}
		
		try{
			
			List<String> list = cmd.tabComplete(sender, args);
			
			if(list == null){
				return null;
			}
			
			if(list.isEmpty()){
				return list;
			}
			
			List<String> toReturn = new ArrayList<String>();
			
			if(args[args.length - 1].isEmpty()){
				for(String type : list){
					toReturn.add(type);
				}
			}else{
				for(String type : list){
					if(type.toLowerCase().startsWith(args[args.length - 1].toLowerCase())){
						toReturn.add(type);
					}
				}
			}
			
		}catch (Exception ex){
			sender.sendMessage("§cError: " + ex.getMessage());
		}
		
		return null;
	}
	
    protected UHCCommand getCommand(String name) {
        for (UHCCommand cmd : cmds) {
            if (cmd.getName().equalsIgnoreCase(name)) {
                return cmd;
            }
        }
        
        return null;
    }
    
    public void registerCommands(Game game, Timer timer, ScatterManager sm, Settings settings, WorldUtils wu, ScenarioManager manager, SpectatorManager spec, RespawnManager rm, MiningNotifManager mnm, GUIManager gui, PlayerDataManager data, SQLManager sql, NickManager nm, ArenaManager arena, PVPEvent pvp, NickManager nick){
    	
    	cmds.add(new StartCommand(game));
    	cmds.add(new HostCommand(game, spec));
    	cmds.add(new OpenCommand(game));
    	cmds.add(new CloseCommand(game));
    	cmds.add(new FreezeCommand());
    	cmds.add(new UnfreezeCommand());
    	cmds.add(new HealthCommand());
    	cmds.add(new ChunksCommand(game, sm, settings, wu));
    	cmds.add(new SettingsCommand(settings, game, gui));
    	cmds.add(new TeamCommand(game));
    	cmds.add(new ScenarioCommand(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), manager));
    	cmds.add(new PvPCommand(pvp));
    	cmds.add(new VisitCommand());
    	cmds.add(new SpecCommand(spec, rm));
    	cmds.add(new RespawnCommand(rm));
    	cmds.add(new TPCommand());
    	cmds.add(new GamemodeCommand());
    	cmds.add(new GiveCommand());
    	cmds.add(new MiningNotifCommand(mnm));
    	cmds.add(new HelpopCommand());
    	cmds.add(new KillCountCommand());
    	cmds.add(new KillTopCommand());
    	cmds.add(new CheckStuffCommand());
    	cmds.add(new StaffCommand());
    	cmds.add(new ConfigCommand(game, settings, manager, timer));
    	cmds.add(new PlayerListCommand(nm));
    	cmds.add(new StatsCommand(data, sql, nick));
    	cmds.add(new NickCommand(nm));
    	cmds.add(new FlyCommand());
    	cmds.add(new HealCommand());
    	cmds.add(new ArenaCommand(arena));
    	cmds.add(new SummonCommand());
    	cmds.add(new SpeedCommand());
		cmds.add(new ChatCommand(game));
		cmds.add(new SendCoordinatesCommand());
		cmds.add(new TeamChatCommand());
		cmds.add(new BackbackCommand(manager));
		cmds.add(new InvseeCommand(data, nick));
		cmds.add(new RemoveFromListCommand());
		cmds.add(new WhitelistCommand());
		cmds.add(new LatescatterCommand(timer, sm));
		cmds.add(new RushCommand(timer, game));
		cmds.add(new StatsResetCommand(sql, data));
        cmds.add(new AlphaCommand(game));
    	
    	for(UHCCommand cmd : cmds){
    		PluginCommand pCmd = net.weswaas.oniziacuhc.OniziacUHC.getInstance().getCommand(cmd.getName());
    		
    		if(pCmd == null){
    			Bukkit.broadcastMessage(cmd.getName());
    			continue;
    		}
    		
    		pCmd.setExecutor(this);
    		pCmd.setTabCompleter(this);
    	}
    	
    }

}
