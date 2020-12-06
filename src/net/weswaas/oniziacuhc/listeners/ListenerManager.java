package net.weswaas.oniziacuhc.listeners;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.gui.GUIManager;
import net.weswaas.oniziacuhc.listeners.events.FreezeListener;
import net.weswaas.oniziacuhc.listeners.events.GameStartListener;
import net.weswaas.oniziacuhc.managers.*;
import net.weswaas.oniziacuhc.managers.borders.BorderListener;
import net.weswaas.oniziacuhc.managers.borders.BorderPlacer;
import net.weswaas.oniziacuhc.managers.borders.BorderTeleporter;
import net.weswaas.oniziacuhc.managers.borders.BorderTimer;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import net.weswaas.oniziacuhc.scenario.scenarios.Timebomb;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.stats.SQLManager;
import net.weswaas.oniziacuhc.utils.PlayerUtils;
import net.weswaas.oniziacuhc.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {
	
	private net.weswaas.oniziacuhc.OniziacUHC pl;
	
	public ListenerManager(net.weswaas.oniziacuhc.OniziacUHC pl) {
		
		this.pl = pl;
	}
	
	public void registerListeners(PlayerUtils pu, Settings settings, ScenarioManager manager, Game game, BorderTimer bt, WinManager wm, SpectatorManager spec, RespawnManager rm, ScatterManager sm, ScoreboardManager board, WorldUtils wu, MiningNotifManager mnm, GUIManager gui, BorderPlacer bp, Timer timer, Timebomb timebomb, CombatLoggerManager clm, DeathListener dl, SQLManager sql, PlayerDataManager data, ArenaManager arena, BorderTeleporter tp, NickManager nick){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new RespawnListener(spec, arena), pl);
		pm.registerEvents(clm, pl);
		pm.registerEvents(arena, pl);
		pm.registerEvents(new JoinListener(pu, game, spec, rm, board, wm, settings, clm, sql, data, nick), pl);
		pm.registerEvents(new FreezeListener(), pl);
		pm.registerEvents(new EssentialsListeners(settings, game, data, arena, tp, manager, gui, nick, spec), pl);
		pm.registerEvents(new BorderListener(game), pl);
		pm.registerEvents(dl, pl);
		pm.registerEvents(spec, pl);
		pm.registerEvents(new GameStartListener(sm, game, spec, bp, timer, arena, bt), pl);
		pm.registerEvents(mnm, pl);
		pm.registerEvents(new BanManager(dl, timebomb), pl);
		pm.registerEvents(new NickManager(), pl);
		pm.registerEvents(new ChunkListener(game), pl);
	}

}
