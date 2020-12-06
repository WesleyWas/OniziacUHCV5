package net.weswaas.oniziacuhc.managers.borders;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.utils.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class BorderTimer {
	
	private ArrayList<Integer> borders = new ArrayList<Integer>();
	private int counter = 300;
	
	private BorderPlacer bp;
	private Game game;
	private BorderTeleporter bt;
	
	public BorderTimer(BorderPlacer bp, Game game, BorderTeleporter bt) {
		
		this.bp = bp;
		this.game = game;
		this.bt = bt;
	}
	
	public void lauchTimer(){
		
		new BukkitRunnable() {
			public void run() {
				
				if(!borders.isEmpty()){
					execute();
				}else{
					this.cancel();
				}
				
			}
		}.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 0, 6000);
		
		new BukkitRunnable() {
			public void run() {
				counter--;
				
				if(borders.isEmpty()){
					this.cancel();
				}
				
				if(counter == 180 || counter == 120){
					if(counter == 180){
						Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§6The world border gonna shrink to " + borders.get(0) + "x" + borders.get(0) + " in 3 minutes.");
					}else{
						Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§6The world border gonna shrink to " + borders.get(0) + "x" + borders.get(0) + " in 2 minutes.");
					}
				}else if(counter == 60 || counter == 30 || counter == 15 || counter == 10 || counter == 5){
					Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§6The world border gonna shrink to " + borders.get(0) + "x" + borders.get(0) + " in " + counter + " seconds.");
					if(counter == 60 && borders.get(0) != game.getRadius()){
                        bp.place(borders.get(0), Bukkit.getWorld("world"), 0, false);
					}

                    if(counter == 60 || counter == 30 || counter == 15){
                        if(borders.get(0) < 101){
                            for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().getPlayers()){
                                if(pls.getLocation().getBlockY() < 50){
                                    new SoundsUtils(pls).playSounds(Sound.NOTE_PLING);
                                    pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cAs it will be the deathmatch in " + counter + " seconds, you have to GO UP or the border will TP you to the surface.");
                                }
                            }
                        }
                    }
				}
			}
		}.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 0, 20);
	}
	
	public void fillList(){
		
		borders.add(3000);
		borders.add(2500);
		borders.add(2000);
		borders.add(1500);
		borders.add(1000);
		borders.add(500);
		borders.add(100);
		borders.add(50);
		borders.add(25);
		borders.add(10);
		borders.add(5);
		
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for(int i : borders){
			if(i >= game.getRadius()){
				toRemove.add(i);
			}
		}

		borders.removeAll(toRemove);
	}
	
	@SuppressWarnings("deprecation")
	public void execute(){
		bp.place(borders.get(0), Bukkit.getWorld("world"), 2, true);
		for(Player pls : Bukkit.getOnlinePlayers()){
			bt.teleport(borders.get(0), Bukkit.getWorld("world"), pls);
		}
		Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§6Border has shrunk to " + borders.get(0) + "x" + borders.get(0) + ".");
		if(borders.get(0) == 500){
			bt.netherTeleport();
		}
		borders.remove(0);
		counter = 300;
	}

	public ArrayList<Integer> getBordersList(){
		return this.borders;
	}

}
