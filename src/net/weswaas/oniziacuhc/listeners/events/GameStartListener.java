package net.weswaas.oniziacuhc.listeners.events;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.managers.ArenaManager;
import net.weswaas.oniziacuhc.managers.ScatterManager;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import net.weswaas.oniziacuhc.managers.borders.BorderPlacer;
import net.weswaas.oniziacuhc.managers.borders.BorderTimer;
import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartListener implements Listener{
	
	private ScatterManager sm;
	private Game game;
	private SpectatorManager spec;
	private BorderPlacer bp;
	private Timer timer;
	private ArenaManager arena;
	private BorderTimer btimer;
	
	private Location spawn = new Location(Bukkit.getWorld("world"), 0, Bukkit.getWorld("world").getHighestBlockYAt(new Location(Bukkit.getWorld("world"), 0, 100, 0)) + 5, 0);
	
	public GameStartListener(ScatterManager sm, Game game, SpectatorManager spec, BorderPlacer bp, Timer timer, ArenaManager arena, BorderTimer btimer) {
		
		this.sm = sm;
		this.game = game;
		this.spec = spec;
		this.bp = bp;
		this.timer = timer;
		this.arena = arena;
		this.btimer = btimer;
	}
	
	@EventHandler
	public void on(GameStartEvent e){
		
		game.setStarted(true);
		game.setOpen(false);
		game.setMuted(true);
		arena.removeAllFromArena();
		Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cThe chat is now muted.");
		GameState.setState(GameState.SCATTERING);
		Bukkit.getWorld("world").setTime(6000);
		Bukkit.getWorld("world").setGameRuleValue("naturalRegeneration", "false");
		Bukkit.getWorld("world_nether").setGameRuleValue("naturalRegeneration", "false");
        Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorld("world").setTime(0);
		Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Scattering has started... (" + net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.size() + " players to scatter)");
		Bukkit.broadcastMessage(OniziacUHC.PREFIX + "§cPlease DO NOT relog during scattering.");
		createTeams();
		btimer.fillList();
		bp.place(game.getRadius(), Bukkit.getWorld("world"), 2, true);
		for(String specs : spec.getSpecs()){
			Player spec = Bukkit.getPlayer(specs);
			if(spec != null){
				Bukkit.getPlayer(specs).teleport(spawn);
			}
		}
		sendRules();
		new BukkitRunnable(){
			@Override
			public void run() {
				sm.scatter(net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers, game.getLocs());
			}
		}.runTaskLater(OniziacUHC.getInstance(), 20);
	}
	
	private void createTeams(){
		
		for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
			if(!Team.hasTeam(pls)){
				new Team(pls);
			}
		}
		
	}
	
	private void sendRules(){
		
		Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§a========== §aUHC Rules ==========");
		Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fA Final heal will be given at " + timer.getHeal() + " minutes.");
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fThe PVP will be activated at " + timer.getPvP() + " minutes.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 80);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fStripmining is allowed.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 160);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fBlastmining and pokeholing are not allowed. They're equal to a 3 days ban.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 240);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fApple rates are 2%.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 320);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fIf you die by a hacker, you can request a respawn only if he is dead / banned.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.instance, 400);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fSkybasing is forbidden is 100x100. It can result in a 1 week ban.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 480);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fThe uses of any cheat (x-ray, hacked client) is able to be banned permanently.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 560);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fShears does NOT work for apples.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 640);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fStealing is allowed, like stalking, but not excessively. It can result in a 3 days ban.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 720);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fiPvP during the no-PvP period is not allowed and is bannable.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 800);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fIf you relog during the final heal, you will not receive it.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 880);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fYou can relog, but do not take more time then 5 minutes.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 960);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fThe border only TP you, don't kill.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 1040);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fIf you have some questions, ask it via /helpop and do not spam it.");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 1120);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§fHave fun, and don't forget to thanks " + game.getHost().getName() + " for the host !");
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 1200);
	}

}
