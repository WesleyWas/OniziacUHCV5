package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.OniziacUHC;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * Created by Weswas on 29/04/2020.
 */
public class NoClean extends Scenario implements Listener{

    public static ArrayList<String> immunised = new ArrayList<>();

    public NoClean(){
        super("NoClean", "After you kill someone, you get 15 seconds of invincibility.");
        slot = 34;
        setMaterial(new ItemBuilder(Material.PISTON_STICKY_BASE).name("§aNoClean").build());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if(e.getEntity().getKiller() instanceof Player){
            final Player killer = e.getEntity().getKiller();
            if(OniziacUHC.getInstance().getArenaManager().isInArena(killer)){
                return;
            }
            if(immunised.contains(killer.getName())){
                immunised.remove(killer.getName());
            }
            immunised.add(killer.getName());
            killer.sendMessage(OniziacUHC.PREFIX + "[NoClean] You have now 15 seconds of immunity.");

            new BukkitRunnable(){
                @Override
                public void run() {
                    if(immunised.contains(killer.getName())){
                        immunised.remove(killer.getName());
                        killer.sendMessage(OniziacUHC.PREFIX + "[NoClean] §cYou are not immunised anymore.");
                    }
                }
            }.runTaskLater(OniziacUHC.getInstance(), 300);

        }
    }

    @EventHandler
    public void onEntity(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            if(OniziacUHC.getInstance().getArenaManager().isInArena(p)){
                return;
            }
            if(!immunised.contains(p.getName())){
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            if(e.getEntity() instanceof Player){
                Player damager = (Player) e.getDamager();
                if(OniziacUHC.getInstance().getArenaManager().isInArena(damager)){
                    return;
                }
                if(immunised.contains(damager.getName())){
                    immunised.remove(damager.getName());
                    damager.sendMessage(OniziacUHC.PREFIX + "[NoClean] §cYou are not immunised anymore.");
                }
            }
        }
    }

}
