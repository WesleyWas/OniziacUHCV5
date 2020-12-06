package net.weswaas.oniziacuhc.managers.combatlogger;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.listeners.DeathListener;
import net.weswaas.oniziacuhc.listeners.JoinListener;
import net.weswaas.oniziacuhc.managers.NickManager;
import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Weswas on 23/04/2020.
 */
public class CombatVillager {

    public static HashMap<String, CombatVillager> playerVillager = new HashMap<>();
    public static ArrayList<Villager> villagers = new ArrayList<Villager>();
    public static HashMap<String, Villager> pVillager = new HashMap<String, Villager>();
    public static HashMap<String, net.minecraft.server.v1_7_R4.Entity> pEntity = new HashMap<>();
    public static HashMap<Villager, String> vPlayer = new HashMap<Villager, String>();
    public static HashMap<String, PlayerInventory> invs = new HashMap<String, PlayerInventory>();
    public static HashMap<String, Location> pLoc = new HashMap<String, Location>();

    private Player player;
    private String name;
    private Villager villager;
    private net.minecraft.server.v1_7_R4.Entity nmsEntity;

    private BukkitTask task;
    private BukkitTask loop;

    public CombatVillager(Player p){

        this.name = p.getName();
        this.player = p;
        spawnVillager();

    }

    public static CombatVillager getCombatVillager(String playerName){

        for(String s : playerVillager.keySet()){
            if(s.equalsIgnoreCase(playerName)){
                return playerVillager.get(s);
            }
        }
        return null;
    }

    public void spawnVillager(){

        if(!GameState.isState(GameState.GAME)){
            return;
        }

        if(!JoinListener.inRelog.contains(name)){
            return;
        }

        Entity en = Bukkit.getWorld("world").spawnEntity(player.getLocation(), EntityType.VILLAGER);
        villager = (Villager) en;

        EntityEquipment ee = villager.getEquipment();
        ee.setHelmet(player.getInventory().getHelmet());
        ee.setChestplate(player.getInventory().getChestplate());
        ee.setLeggings(player.getInventory().getLeggings());
        ee.setBoots(player.getInventory().getBoots());
        ee.setItemInHand(player.getItemInHand());

        villager.setCustomName(name);
        villager.setCustomNameVisible(true);
        villager.setAdult();
        villager.setProfession(Villager.Profession.BUTCHER);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 18000, 127));
        villager.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 18000, 127));
        villager.setRemoveWhenFarAway(false);

        nmsEntity = ((CraftEntity) en).getHandle();
        NBTTagCompound compound = new NBTTagCompound();
        nmsEntity.c(compound);
        compound.setByte("NoAI", (byte)1);
        nmsEntity.f(compound);

        pEntity.put(name, nmsEntity);
        villagers.add(villager);
        pVillager.put(player.getName(), villager);
        vPlayer.put(villager, player.getName());
        invs.put(player.getName(), player.getInventory());
        pLoc.put(player.getName(), player.getLocation());
        playerVillager.put(name, this);

        for(Player pls : Bukkit.getServer().getOnlinePlayers()){
            if(pls.hasPermission("uhc.staff")){
                pls.sendMessage("§3[Staff] " + ChatColor.YELLOW + name + " disconnected. He has 5 minutes to relog");
            }
        }

        startTask(player);
        loop();

    }

    public void loop(){
        loop = new BukkitRunnable(){
            @Override
            public void run() {
                villager.getLocation().getChunk().load();
            }
        }.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 0, 80);
    }

    public void startTask(final  Player p){

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                tooLate(p);
            }
        }.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 5650);

    }

    public void tooLate(Player p){

        JoinListener.inRelog.remove(name);
        Location loc = pLoc.get(name).add(0, 1, 0);

        Bukkit.broadcastMessage(ChatColor.RED + name + ChatColor.YELLOW + " did not relog in time.");

        new BukkitRunnable(){
            @Override
            public void run() {
                villager.damage(99999);
                villager.playEffect(EntityEffect.DEATH);
                nmsEntity.damageEntity(null, 99999);
                villager.remove();
            }
        }.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 10);

        for(ItemStack item : invs.get(name).getContents()){
            if(item != null && item.getType() != Material.AIR){
                loc.getWorld().dropItemNaturally(loc, item);
            }
        }

        for(ItemStack item : invs.get(name).getArmorContents()){
            if(item != null && item.getType() != Material.AIR){
                loc.getWorld().dropItemNaturally(loc, item);
            }
        }

        DeathListener.setFence(loc.add(0, -1, 0), name);

        pEntity.remove(name);
        villagers.remove(villager);
        pVillager.remove(name);
        vPlayer.remove(villager);
        invs.remove(name);
        pLoc.remove(name);

        if(net.weswaas.oniziacuhc.OniziacUHC.getInstance().getGame().getTeamSize() > 1){
            Team team = Team.getTeam(p);
            team.removePlayer(p);
        }

        net.weswaas.oniziacuhc.OniziacUHC.getInstance().getWinManager().winCheck();

    }

    public void relog(){

        NickManager nick = net.weswaas.oniziacuhc.OniziacUHC.getInstance().getNickManager();
        player = Bukkit.getPlayer(name);

        if(nick.isNickedPerName(name)){
            String trueName = nick.getNameNick().get(name);
            if(trueName == name){
                trueName = nick.getNameNick().get(name);
            }
            player = Bukkit.getPlayer(trueName);
        }else{
            player = Bukkit.getPlayer(name);
        }

        net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers().add(player);

        JoinListener.inRelog.remove(name);
        player.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You relogged in time.");

        new BukkitRunnable(){
            @Override
            public void run() {
                villager.damage(99999);
                villager.playEffect(EntityEffect.DEATH);
                nmsEntity.damageEntity(null, 99999);
                villager.remove();
            }
        }.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 10);

        pEntity.remove(name);
        villagers.remove(villager);
        pVillager.remove(name);
        vPlayer.remove(villager);
        invs.remove(name);
        pLoc.remove(name);

        this.task.cancel();
        this.loop.cancel();

    }

    public String getName(){
        return this.name;
    }

}
