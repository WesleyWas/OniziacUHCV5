package net.weswaas.oniziacuhc.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class NickManager implements Listener{
	
	public ArrayList<String> nickedUUID = new ArrayList<String>();
	public HashMap<String, String> uuidNick = new HashMap<String, String>();
    public HashMap<String, String> nameNick = new HashMap<String, String>();
	public HashMap<String, String> nickName = new HashMap<String, String>();
	public ArrayList<String> names = new ArrayList<String>();
	
	public void registerNickNames(){
		
		names.add("Fever");
		names.add("Netspin");
		names.add("EndMan");
		names.add("Seems");
		names.add("Capitaldood");
		names.add("Kev99IS");
		names.add("newMarley55");
		names.add("Unsurged");
		names.add("HyperFractal");
		names.add("DailyPrimed");
		names.add("Geeksday1998");
		names.add("WizCambrodge");
		names.add("NoTimeToLate");
		names.add("NeverToEarly");
		names.add("Saawseyy");
		names.add("Zombie66");
		names.add("ProPlayerNoScope");
		names.add("Squeechaay");
		names.add("NoticedForLife");
		names.add("ItsBraasted");
		
	}

	public String getNickedName(String player){

		String game = getNickName().get(player);
		if(game == player){
			game = getNameNick().get(player);
		}

		return game;
	}
	
	public void nick(Player p){
		
		Random r = new Random();
		int i = r.nextInt(names.size());
		String name = names.get(i);
		
		if(name == null){
			name = names.get(0);
		}
		
		if(!isNicked(p)){
            nameNick.put(name, p.getName());
			nickName.put(p.getName(), name);
            nickedUUID.add(p.getUniqueId().toString());
            uuidNick.put(p.getUniqueId().toString(), name);
            names.remove(name);
			p.setDisplayName(name);
			p.setCustomName(name);
			p.setPlayerListName(name);
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You're now nicked as " + name);
			changeName(name, p);
		}else{
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou're already nicked. To remove, and change your nick, make /nick remove");
		}
		
	}
	
	public void unNick(Player p){
		
		if(isNicked(p)){

            String masked = uuidNick.get(p.getUniqueId().toString());
            String official = nameNick.get(masked);

            p.setDisplayName(official);
            p.setPlayerListName(official);
            p.setCustomName(official);
			names.add(p.getPlayerListName());
			uuidNick.remove(p.getUniqueId().toString());
			uuidNick.remove(p.getUniqueId().toString());
			uuidNick.remove(p.getUniqueId().toString());
			nickedUUID.remove(p.getUniqueId().toString());
            nameNick.remove(masked);
			nickName.remove(official);
            changeName(official, p);
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Players can now see you as " + official);
		}else{
			p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cYou're not nicked !");
		}
		
	}

	public String getOfficialName(String nick){
		return this.nameNick.get(nick);
	}
	
	public boolean isNicked(Player p){

		if(this.nameNick.containsKey(p.getName()) || this.nameNick.containsValue(p.getName())){
			return true;
		}else{
			return false;
		}

		//if(nickedUUID.contains(p.getUniqueId().toString())){
			//return true;
		//}else{
			//return false;
		//}
	}

	public boolean isNickedPerName(String s){
		if(this.nameNick.containsKey(s) || this.nameNick.containsValue(s)){
			return true;
		}else{
			return false;
		}
	}

	public HashMap<String, String> getNameNick(){
		return this.nameNick;
	}

	public HashMap<String, String> getNickName(){
		return this.nickName;
	}

	public static void changeName(String name, Player player) {
		try {
			Method getHandle = player.getClass().getMethod("getHandle");
			Object entityPlayer = getHandle.invoke(player);
            /*
             * These methods are no longer needed, as we can just access the
             * profile using handle.getProfile. Also, because we can just use
             * the method, which will not change, we don't have to do any
             * field-name look-ups.
             */
			boolean gameProfileExists = false;
			// Some 1.7 versions had the GameProfile class in a different package
			try {
				Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
				gameProfileExists = true;
			} catch (ClassNotFoundException ignored) {

			}
			try {
				Class.forName("com.mojang.authlib.GameProfile");
				gameProfileExists = true;
			} catch (ClassNotFoundException ignored) {

			}
			if (!gameProfileExists) {
                /*
                 * Only 1.6 and lower servers will run this code.
                 *
                 * In these versions, the name wasn't stored in a GameProfile object,
                 * but as a String in the (final) name field of the EntityHuman class.
                 * Final (non-static) fields can actually be modified by using
                 * {@link java.lang.reflect.Field#setAccessible(boolean)}
                 */
				Field nameField = entityPlayer.getClass().getSuperclass().getDeclaredField("name");
				nameField.setAccessible(true);
				nameField.set(entityPlayer, name);
			} else {
				// Only 1.7+ servers will run this code
				Object profile = entityPlayer.getClass().getMethod("getProfile").invoke(entityPlayer);
				Field ff = profile.getClass().getDeclaredField("name");
				ff.setAccessible(true);
				ff.set(profile, name);
			}
			// In older versions, Bukkit.getOnlinePlayers() returned an Array instead of a Collection.
			if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class) {
				Collection<? extends Player> players = (Collection<? extends Player>) Bukkit.class.getMethod("getOnlinePlayers").invoke(null);
				for (Player p : players) {
					p.hidePlayer(player);
					p.showPlayer(player);
				}
			} else {
				Player[] players = ((Player[]) Bukkit.class.getMethod("getOnlinePlayers").invoke(null));
				for (Player p : players) {
					p.hidePlayer(player);
					p.showPlayer(player);
				}
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
            /*
             * Merged all the exceptions. Less lines
             */
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getNickedPlayers(){
		return nickedUUID;
	}

}
