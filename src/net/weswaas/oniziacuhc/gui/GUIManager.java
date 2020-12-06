package net.weswaas.oniziacuhc.gui;

import com.google.common.collect.ImmutableList;
import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.gui.guis.*;
import net.weswaas.oniziacuhc.managers.MiningNotifManager;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
	
	private List<GUI> guis = new ArrayList<GUI>();
	
	public GUI getGUI(String name){
		for(GUI gui : guis){
			if(gui.getName().equalsIgnoreCase(name)){
				return gui;
			}
		}
		return null;
	}
	
	public List<GUI> getGUIS(){
		return ImmutableList.copyOf(guis);
	}
	
	public void registerGUIS(Settings settings, ScenarioManager manager, Game game, Timer timer, SpectatorManager sm, MiningNotifManager mnm){
		
		addGUI(new SettingsGUI(settings, this, game, timer, sm));
		addGUI(new MaxPlayersGUI(settings, this));
		addGUI(new ScenarioGUI(manager, this));
		addGUI(new BorderRadiusGUI(this, game));
		addGUI(new SpectatorSettingsGUI(sm, mnm));
		addGUI(new TeamSizeGUI(this, game));
		addGUI(new CheckStuffGUI());
		
	}
	
	public void addGUI(GUI gui){
		guis.add(gui);
		gui.create();
		
		if(gui instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener)gui, net.weswaas.oniziacuhc.OniziacUHC.getInstance());
		}
	}

}
