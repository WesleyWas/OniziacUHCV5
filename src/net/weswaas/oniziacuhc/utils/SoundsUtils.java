package net.weswaas.oniziacuhc.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundsUtils {
	
	private Player player;
	
	public SoundsUtils(Player p){
		this.player = p;
	}
	
	public void playSounds(Sound s){
		this.player.playSound(this.player.getLocation(), s, 8.0F, 8.0F);
	}

}
