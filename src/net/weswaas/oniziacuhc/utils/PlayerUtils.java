package net.weswaas.oniziacuhc.utils;

import com.weswaas.api.utils.ItemBuilder;
import com.weswaas.api.utils.ValueComparator;
import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.listeners.DeathListener;
import net.weswaas.oniziacuhc.managers.ArenaManager;
import net.weswaas.oniziacuhc.managers.SpectatorManager;
import net.weswaas.oniziacuhc.scenario.Scenario;
import net.weswaas.oniziacuhc.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.TreeMap;

public class PlayerUtils {

    private Game game;
    private ScenarioManager manager;
    private SpectatorManager spec;
    private ArenaManager arena;

    public PlayerUtils(Game game, ScenarioManager manager, SpectatorManager spec, ArenaManager arena) {

        this.game = game;
        this.manager = manager;
        this.spec = spec;
        this.arena = arena;
    }

    @SuppressWarnings("deprecation")
    public void lobbyJoin(Player p){

        p.teleport(new Location(Bukkit.getWorld("lobby"), 0.5, 101, 0.5).setDirection(new Vector(-5, 0, 0)));
        sendJoinMessage(p);
        p.getInventory().clear();
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setFlying(false);
        p.setLevel(0);
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        if(game.getHostName() != p.getDisplayName()){
            OniziacUHC.getInstance().inGamePlayers.add(p);
        }
        for(PotionEffect pe : p.getActivePotionEffects()){
            p.removePotionEffect(pe.getType());
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 99999999, 2));
        p.setGameMode(GameMode.SURVIVAL);
        if(game.isHost(p.getDisplayName())){
            spec.add(p);
            p.setGameMode(GameMode.CREATIVE);
            p.sendMessage(OniziacUHC.PREFIX + "Welcome back, host.");
        }
        if(game.isHost(p.getDisplayName())){p.setFlying(true);}

        if(Bukkit.getOnlinePlayers().size() == 1 || game.isHost(p.getDisplayName())){
            if(p.hasPermission("uhc.host")){
                p.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).name("§3UHC Settings").build());
            }
        }


    }

    public static Map.Entry<String, Integer> getKTEntry(){

        ValueComparator bvc = new ValueComparator(DeathListener.pKills);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
        sorted_map.putAll(DeathListener.pKills);

        return sorted_map.pollFirstEntry();

    }

    public void sendJoinMessage(Player p){

        String scenarios = "§bThis is a vanilla match.";

        StringBuilder list = new StringBuilder("");
        int i = 1;

        if(manager.getEnabledScenarios().size() > 0){
            for(Scenario scen : manager.getEnabledScenarios()){
                if(list.length() > 0){
                    if(i == manager.getEnabledScenarios().size()){
                        list.append(" and ");
                    }else{
                        list.append(", ");
                    }
                }

                list.append(scen.getName());
                i++;
                scenarios = list.toString().trim();
            }
        }

        p.sendMessage("§a§m-------------------------------------");
        p.sendMessage("§8» §aWelcome to §bOniziacUHC v" + OniziacUHC.getInstance().getDescription().getVersion());
        p.sendMessage("§a");
        p.sendMessage("§8» §aHost: §b" + (game.getHost() == null ? "Host is not set." : game.getHost().getName()));
        p.sendMessage("§8» §aTeam Size: §b" + (game.getTeamSize() > 1 ? "TO" + game.getTeamSize() : "FFA"));
        p.sendMessage("§b");
        p.sendMessage("§8» §aScenarios (" +manager.getEnabledScenarios().size() + "): §b" + scenarios);

        p.sendMessage("§a§m-------------------------------------");
        if(arena.isEnabled()){
            p.sendMessage(OniziacUHC.PREFIX + "§3Arena is now live! §bJoin with /arena !");
        }

    }

}