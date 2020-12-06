package net.weswaas.oniziacuhc.listeners;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.Settings;
import net.weswaas.oniziacuhc.managers.*;
import net.weswaas.oniziacuhc.managers.combatlogger.CombatVillager;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.stats.SQLManager;
import net.weswaas.oniziacuhc.team.Team;
import net.weswaas.oniziacuhc.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class JoinListener implements Listener {

    private PlayerUtils pu;
    private Game game;
    private SpectatorManager spec;
    private RespawnManager rm;
    private ScoreboardManager board;
    private WinManager wm;
    private Settings settings;
    private CombatLoggerManager clm;
    private SQLManager sql;
    private PlayerDataManager data;
    private NickManager nick;

    public static ArrayList<String> inRelog = new ArrayList<String>();
    public static ArrayList<String> whitelist = new ArrayList<String>();

    private BukkitRunnable task;

    public JoinListener(PlayerUtils pu, Game game, SpectatorManager spec, RespawnManager rm, ScoreboardManager board, WinManager wm, Settings settings, CombatLoggerManager clm, SQLManager sql, PlayerDataManager data, NickManager nick) {

        this.pu = pu;
        this.game = game;
        this.spec = spec;
        this.rm = rm;
        this.board = board;
        this.wm = wm;
        this.settings = settings;
        this.clm = clm;
        this.sql = sql;
        this.data = data;
        this.nick = nick;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        e.setJoinMessage(null);

        final Player p = e.getPlayer();
        vanish();

        if(!data.isCreated(p.getUniqueId().toString())){
            sql.createPlayerData(p.getUniqueId().toString(), p.getName());
        }

        sql.connection();
        sql.createAccount(p);

        if(!data.hasStatsToStoreAccount(p)){
            data.createStatsToStoreAccount(p);
        }

        if(!DeathListener.pKills.containsKey(p.getPlayerListName())){
            DeathListener.pKills.put(p.getPlayerListName(), 0);
        }

        if(GameState.isState(GameState.LOBBY)){
            pu.lobbyJoin(p);

        }else{
            if(!GameState.isState(GameState.SCATTERING)){
                sidebars(p);
                removeAllEffects(p);
            }

            if(inRelog.contains(p.getName()) && GameState.isState(GameState.GAME)){
                CombatVillager cv = CombatVillager.getCombatVillager(p.getName());
                cv.relog();
            }
            else if(spec.getPreparedPlayers().contains(p.getName())){
                spec.add(p);
            }
            else if(rm.readyToBeRespawned.contains(p.getName())){
                rm.respawn(p);
                rm.readyToBeRespawned.remove(p.getName());
                heal(p);
                if(!OniziacUHC.getInstance().getPlayers().contains(p)){
                    OniziacUHC.getInstance().inGamePlayers.add(p);
                }
            }
            else if(nick.isNickedPerName(p.getName()) && GameState.isState(GameState.GAME)){
                final String game = nick.getNickedName(p.getName());
                if(inRelog.contains(game)){
                    CombatVillager cv = CombatVillager.getCombatVillager(game);
                    cv.relog();

                    for(Player pls : Bukkit.getServer().getOnlinePlayers()){
                        if(pls.hasPermission("uhc.staff") && !OniziacUHC.getInstance().getPlayers().contains(pls)){
                            pls.sendMessage("§3[Staff] " + p.getName() + " has relogged as " + game);
                        }
                    }

                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            nick.changeName(game, p);
                            p.setDisplayName(game);
                            p.setCustomName(game);
                            p.setPlayerListName(game);
                            for(Player pls : Bukkit.getServer().getOnlinePlayers()){
                                if(OniziacUHC.getInstance().getPlayerByName(game) != null){
                                    pls.showPlayer(p);
                                    if(pls.hasPermission("uhc.staff")){
                                        pls.sendMessage("§3[Staff] " + p.getName() + "(" + game + ") is now visible for the other players");
                                    }
                                }
                            }
                        }
                    }.runTaskLater(OniziacUHC.getInstance(), 20);

                }
            }
            else{
                spec.add(p);
            }
        }
    }

    private void removeAllEffects(Player p){
        for(PotionEffect pe : p.getActivePotionEffects()){
            p.removePotionEffect(pe.getType());
        }
    }

    private void sidebars(Player p){
        if(board.boards.containsKey(p.getName())){
            p.setScoreboard((Scoreboard) board.boards.get(p.getName()));
        }else{
            board.createSidebar(p);
        }
    }

    private void heal(final Player p){
        new BukkitRunnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                p.setHealth(20);
            }
        }.runTaskLater(OniziacUHC.getInstance(), 50);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

        e.setQuitMessage(null);

        final Player p = e.getPlayer();

        if(spec.isSpectator(p.getName())){
            spec.getSpecs().remove(p.getName());
        }

        if(!GameState.isState(GameState.LOBBY) && !GameState.isState(GameState.SCATTERING) && !GameState.isState(GameState.FINISH)){

            if(OniziacUHC.getInstance().inGamePlayers.contains(p)){
                if(!spec.isSpectator(p.getName())){
                    inRelog.add(p.getName());
                    OniziacUHC.getInstance().getPlayers().remove(p);
                    new CombatVillager(p);
                }
            }
        }else{

            if(game.getTeamSize() > 1){
                if(Team.hasTeam(p)){
                    if(Team.getTeam(p).isLeader(p)){
                        for(String s : Team.getTeam(p).getPlayerNames()){
                            Player pls = Bukkit.getPlayer(s);
                            if(pls != null && pls.isOnline()){
                                pls.sendMessage(OniziacUHC.PREFIX + "§c" + p.getPlayerListName() + " removed the team.");
                            }
                        }
                        Team.getTeam(p).removeTeam();
                    }else{
                        for(String s : Team.getTeam(p).getPlayerNames()){
                            Player pls = Bukkit.getPlayer(s);
                            if(pls != null && pls.isOnline()){
                                pls.sendMessage(OniziacUHC.PREFIX + "§c" + p.getPlayerListName() + " left the team.");
                            }
                        }
                        Team.getTeam(p).removePlayer(p);
                    }
                }
            }

            if(spec.isSpectator(p.getName()) && game.getHostName() != p.getDisplayName()){
                spec.remove(p);
            }

            OniziacUHC.getInstance().getPlayers().remove(p);

        }

        wm.winCheck();

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        Player p = e.getPlayer();

        if(Bukkit.getOnlinePlayers().size() >= settings.getMaxPlayers() - 3){
            if(!p.hasPermission("uhc.reserved.slot") && GameState.isState(GameState.LOBBY)){
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Sorry, but the UHC is full.");
            }
        }

        if(GameState.isState(GameState.LOBBY)){
            if(!game.isOpen()){
                if(!p.hasPermission("uhc.prejoin") && !whitelist.contains(p.getName())){
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "The UHC is currently not open. Check our Twitter @Oniziac to know the opening time.");
                }
            }
        }else{

            if(!rm.readyToBeRespawned.contains(p.getName())){
                if(!spec.getPreparedPlayers().contains(p.getName())){
                    if(!inRelog.contains(p.getName())){
                        if(!CombatLoggerManager.pVillager.containsKey(p.getName())){
                            if(!GameState.isState(GameState.SCATTERING)){
                                spec.add(p);
                            }else{
                                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Please wait for the scattering state to be done to be able to join the game and to latescatter.");
                            }
                        }
                    }
                }
            }
        }

    }

    @SuppressWarnings("deprecation")
    private void vanish(){
        for(Player pls : Bukkit.getOnlinePlayers()){
            for(String specs : spec.getSpecs()){
                Player spectator = Bukkit.getPlayer(specs);
                if(spectator != null && spectator.isOnline()){
                    pls.hidePlayer(spectator);
                }
            }
        }
    }

}
