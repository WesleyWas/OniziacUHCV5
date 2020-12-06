package net.weswaas.oniziacuhc;

/**
 * Created by Weswas on 27/04/2020.
 */

import net.weswaas.oniziacuhc.managers.ScatterManager;

        import net.weswaas.oniziacuhc.commands.CommandHandler;
        import net.weswaas.oniziacuhc.gui.GUIManager;
        import net.weswaas.oniziacuhc.listeners.DeathListener;
        import net.weswaas.oniziacuhc.listeners.ListenerManager;
        import net.weswaas.oniziacuhc.listeners.events.PVPEvent;
        import net.weswaas.oniziacuhc.managers.*;
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
        import org.bukkit.Location;
        import org.bukkit.entity.Player;
        import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class OniziacUHC extends JavaPlugin {

    public static OniziacUHC instance;

    private ScenarioManager manager;
    private WorldUtils wu;
    private PlayerUtils pu;
    private Game game;
    private Timer timer;
    private CommandHandler cmd;
    private ScatterManager sm;
    private Settings settings;
    private BorderPlacer bp;
    private BorderTeleporter bt;
    private GUIManager gm;
    private BorderTimer btimer;
    private WinManager wm;
    private SpectatorManager spec;
    private ListenerManager lm;
    private RespawnManager rm;
    private ScoreboardManager board;
    private MiningNotifManager mnm;
    private Timebomb timebomb;
    private DeathListener dl;
    private CombatLoggerManager clm;
    private PlayerDataManager data;
    private SQLManager sql;
    private NickManager nm;
    private ArenaManager arena;
    private PVPEvent pvp;

    public static String PREFIX = "§a§lOniziacUHC §8» §a";

    public ArrayList<Player> inGamePlayers = new ArrayList<Player>();

    public void onEnable() {
        instance = this;
        instances();
        GameState.setState(GameState.LOBBY);
        wu.createWorld();
        wu.gheadRecipe();
        sql.connection();
        Bukkit.broadcastMessage("");

        manager.registerScenarios(timer, timebomb, settings);
        gm.registerGUIS(settings, manager, game, timer, spec, mnm);

        lm = new ListenerManager(this);
        lm.registerListeners(pu, settings, manager, game, btimer, wm, spec, rm, sm, board, wu, mnm, gm, bp, timer, timebomb, clm, dl, sql, data, arena, bt, nm);
        fillMap();
        cmd.registerCommands(game, timer, sm, settings, wu, manager, spec, rm, mnm, gm, data, sql, nm, arena, pvp, nm);
        board.updater();
        spec.spawn = new Location(Bukkit.getWorld("world"), 0, Bukkit.getWorld("world").getHighestBlockYAt(new Location(Bukkit.getWorld("world"), 0, 100, 0)) + 1, 0);
        nm.registerNickNames();
        Bukkit.getWorld("arena").setGameRuleValue("naturalRegeneration", "false");
        wu.load();

        new BukkitRunnable(){
            @Override
            public void run() {
                getLogger().info("§cVERIFYING CENTER...");
                WorldUtils.verifyCenter();
            }
        }.runTaskLater(this, 60);

    }

    public void onDisable() {
        sql.deconnection();
    }

    private void instances(){

        game = new Game();
        settings = new Settings();
        manager = new ScenarioManager();
        wu = new WorldUtils(game);
        gm = new GUIManager();
        bp = new BorderPlacer(game);
        bt = new BorderTeleporter();
        btimer = new BorderTimer(bp, game, bt);
        pvp = new PVPEvent();
        timer = new Timer(game, btimer, pvp, bp);
        spec = new SpectatorManager(game, gm, timer, data);
        arena = new ArenaManager();
        pu = new PlayerUtils(game, manager, spec, arena);
        cmd = new CommandHandler();
        board = new ScoreboardManager(timer, game);
        nm = new NickManager();
        data = new PlayerDataManager(nm);
        sql = new SQLManager("jdbc:mysql://", "localhost", "oniziac", "weswas", "wqa159", "uhc", data);
        data.setSQLManager(sql);
        sm = new ScatterManager(wu, game, timer, board, data, spec);
        wm = new WinManager(game, data, timer);
        mnm = new MiningNotifManager();
        rm = new RespawnManager(spec);
        timebomb = new Timebomb(this);
        dl = new DeathListener(manager, wm, rm, data, arena, game, nm);
        clm = new CombatLoggerManager(dl, rm, wm, manager, timebomb, spec, data, game);
    }

    public ArrayList<Player> getPlayers(){
        return inGamePlayers;
    }

    public static OniziacUHC getInstance(){
        return instance;
    }

    private void fillMap(){

        DeathListener.pKills.put("None", 0);
        DeathListener.pKills.put("None ", 0);
        DeathListener.pKills.put("None  ", 0);
        DeathListener.pKills.put("None   ", 0);
        DeathListener.pKills.put("None    ", 0);
        DeathListener.pKills.put("None     ", 0);
        DeathListener.pKills.put("None      ", 0);
        DeathListener.pKills.put("None       ", 0);
        DeathListener.pKills.put("None        ", 0);
        DeathListener.pKills.put("None         ", 0);

    }

    public Player getPlayerByName(String name){
        for(Player pls : getPlayers()){
            if(pls.getName().equalsIgnoreCase(name)){
                return pls;
            }
        }
        return null;
    }

    public SpectatorManager getSpec(){
        return this.spec;
    }

    public boolean isInGame(Player p){
        return inGamePlayers.contains(p);
    }

    public ScenarioManager getScenarioManager(){
        return this.manager;
    }

    public WinManager getWinManager(){
        return this.wm;
    }

    public Game getGame(){
        return this.game;
    }

    public NickManager getNickManager(){
        return this.nm;
    }

    public ArenaManager getArenaManager(){
        return this.arena;
    }

}

