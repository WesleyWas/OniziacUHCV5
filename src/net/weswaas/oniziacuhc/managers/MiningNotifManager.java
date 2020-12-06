package net.weswaas.oniziacuhc.managers;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MiningNotifManager implements Listener{
	
	public ArrayList<Player> notified = new ArrayList<Player>();
	private ArrayList<Player> cooldowns = new ArrayList<Player>();
	
	@EventHandler
	public void on(BlockBreakEvent e){
		final Player p = e.getPlayer();
		Block b = e.getBlock();
		
		if(!net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers.contains(p)){
			return;
		}
		
		if(b.getType() != Material.DIAMOND_ORE){
			return;
		}
		
		if(cooldowns.contains(p)){
			return;
		}
		
		
		for(Player pls : notified){
			pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + p.getName() + " is mining a §bdiamond §a veine.");
            sendRawMessage(pls, p);
		}
		
		cooldowns.add(p);
		new BukkitRunnable() {
			public void run() {
				cooldowns.remove(p);
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 200);
	}
	
	public void switchNotifs(Player p){
		if(notified.contains(p)){
			notified.remove(p);
		}else{
			notified.add(p);
		}
	}

	private void sendRawMessage(Player p, Player target){
		PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §3Click §bHERE §3to teleport to §b" + target.getName() + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tp " + target.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click here to teleport\",\"color\":\"aqua\"}]}}}"));
        PacketPlayOutChat packet2 = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§8» §3Click §bHERE §3to see §b" + target.getName() + "'s §bInventory" + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/is " + target.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click here to see his inventory\",\"color\":\"aqua\"}]}}}"));
        connection.sendPacket(packet);
        connection.sendPacket(packet2);
	}

}
