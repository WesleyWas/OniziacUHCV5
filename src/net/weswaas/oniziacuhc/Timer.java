package net.weswaas.oniziacuhc;

import net.weswaas.oniziacuhc.listeners.events.PVPEvent;
import net.weswaas.oniziacuhc.listeners.events.StarterFoodEvent;
import net.weswaas.oniziacuhc.managers.borders.BorderPlacer;
import net.weswaas.oniziacuhc.managers.borders.BorderTimer;
import net.weswaas.oniziacuhc.utils.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {

    private net.weswaas.oniziacuhc.Game game;
    private BorderTimer btimer;
    private PVPEvent pvp;
    private BorderPlacer bp;

    public Timer(net.weswaas.oniziacuhc.Game game, BorderTimer btimer, PVPEvent pvp, BorderPlacer bp) {

        this.game = game;
        this.btimer = btimer;
        this.pvp = pvp;
        this.bp = bp;
    }

    private BukkitRunnable timerMinutesTask;
    private BukkitRunnable timerSecondsTask;

    private int damageS = 20;
    private int healM = 10;
    private int healS = (healM * 60);
    private int pvpM = 20;
    private int pvpS = (pvpM * 60);
    private int firstBorderM = 35;

    private int gameTimeM = 0;
    private int gameTimeS = 0;

    public void setDamage(int seconds){
        this.damageS = seconds;
    }

    public int getDamage(){
        return this.damageS;
    }

    public void setHeal(int minutes){
        this.healM = minutes;
        this.healS = (minutes * 60);
    }

    public int getHeal(){
        return this.healM;
    }

    public void setPvP(int minutes){
        this.pvpM = minutes;
        this.pvpS = (minutes * 60);
    }

    public int getPvP(){
        return this.pvpM;
    }

    public void setFirstBorder(int minutes){
        this.firstBorderM = minutes;
    }

    public int getFirstBorder(){
        return this.firstBorderM;
    }

    public int getGameTimeMinutes(){
        return this.gameTimeM;
    }

    public int getGameTimeSeconds(){
        return this.gameTimeS;
    }

    public void gameTimer(){

        timerMinutesTask = new BukkitRunnable() {
            @SuppressWarnings("deprecation")
            public void run() {

                gameTimeM++;
                healM--;
                pvpM--;
                firstBorderM--;

                if(gameTimeM == 3){
                    Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cMobs are now able to spawn.");
                    Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", "true");
                    game.setMuted(false);
                    Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§aThe chat is now enabled.");
                }

                if(healM == 5 || healM == 1 || healM == 0){
                    if(healM == 0){
                        Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§aThe final heal was given.");
                        Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§aPlease do not ask for another one.");
                        for(Player online : Bukkit.getOnlinePlayers()){
                            online.setHealth(20);
                            for(PotionEffect pe : online.getActivePotionEffects()){
                                online.removePotionEffect(pe.getType());
                            }
                        }
                    }else{
                        Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§aFinal heal will be given in " + healM + " minute" + (healM == 1 ? "" : "s") + ".");
                    }
                }

                if(pvpM == 15 || pvpM == 10 || pvpM == 5 || pvpM == 1 || pvpM == 0){
                    if(pvpM == 0){
                        Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPVP is now activated.");
                        Bukkit.getWorld("world").setPVP(true);
                        Bukkit.getPluginManager().callEvent(pvp);
                        for(Player pls : Bukkit.getOnlinePlayers()){
                            new SoundsUtils(pls).playSounds(Sound.ENDERDRAGON_GROWL);
                        }
                    }else{
                        Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPVP will be activated in " + pvpM + " minutes.");
                    }
                }

                if(firstBorderM == 5 || firstBorderM == 3 || firstBorderM == 1 || firstBorderM == 0){
                    if(firstBorderM == 0){
                        btimer.lauchTimer();
                    }else{
                        Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§6World borders gonna schrink to " + btimer.getBordersList().get(0) + "x" + btimer.getBordersList().get(0) + " in " + firstBorderM + " minute(s).");
                        if(firstBorderM == 1){
                            bp.place(btimer.getBordersList().get(0), Bukkit.getWorld("world"), 0, false);
                        }
                    }
                }
            }
        };
        timerMinutesTask.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 1200, 1200);

        timerSecondsTask = new BukkitRunnable() {
            public void run() {
                gameTimeS++;
                healS--;
                pvpS--;
                //firstBorderS--;

                if(gameTimeS == 5){
                    for(final Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
                        pls.getInventory().clear();
                        pls.getInventory().setHelmet(null);
                        pls.getInventory().setChestplate(null);
                        pls.getInventory().setLeggings(null);
                        pls.getInventory().setBoots(null);
                        pls.updateInventory();
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                pls.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 15));
                                pls.getInventory().addItem(new ItemStack(Material.BOOK, 1));
                            }
                        }.runTaskLater(OniziacUHC.getInstance(), 2);
                    }
                    Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§aStarter food has been given.");
                    Bukkit.getPluginManager().callEvent(new StarterFoodEvent());
                }

                if(gameTimeS == 20){
                    Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§aDamages are now activated.");
                    game.setDamages(true);
                }

                if(healS == 30 || healS == 15){
                    Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§aFinal heal will be given in " + healS + " seconds");
                }

                if(pvpS == 30 || pvpS == 15){
                    Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cPVP will be activated in " + pvpS + " seconds");
                }
            }
        };
        timerSecondsTask.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 0, 20);

    }

    public void stopTimer(){
        this.timerMinutesTask.cancel();
        this.timerSecondsTask.cancel();
    }

}
