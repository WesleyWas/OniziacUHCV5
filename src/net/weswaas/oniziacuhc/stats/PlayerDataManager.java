package net.weswaas.oniziacuhc.stats;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.managers.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

	private NickManager nick;
	private SQLManager sql;
	
	public PlayerDataManager(NickManager nick) {
		this.nick = nick;
	}
	
	public ArrayList<net.weswaas.oniziacuhc.stats.PlayerData> datas = new ArrayList<>();
	public ArrayList<net.weswaas.oniziacuhc.stats.StatsToStore> sts = new ArrayList<>();
	public HashMap<String, net.weswaas.oniziacuhc.stats.PlayerData> pData = new HashMap<>();

	public void setSQLManager(SQLManager sql){
		this.sql = sql;
	}
	
	public net.weswaas.oniziacuhc.stats.PlayerData getPlayerData(String uuid){
		for(net.weswaas.oniziacuhc.stats.PlayerData data : datas){
			if(data.getUUID().equalsIgnoreCase(uuid)){
				return data;
			}
		}
		return null;
	}
	
	public net.weswaas.oniziacuhc.stats.StatsToStore getStatsToStoreByUUID(String uuid){
		for(net.weswaas.oniziacuhc.stats.StatsToStore sts1 : sts){
			if(sts1.getUUID().equalsIgnoreCase(uuid)){
				return sts1;
			}
		}
		return new net.weswaas.oniziacuhc.stats.StatsToStore(Bukkit.getOfflinePlayer(uuid).getName(), uuid);
	}
	
	public void createStatsToStoreAccount(Player p){
		net.weswaas.oniziacuhc.stats.StatsToStore stats = new net.weswaas.oniziacuhc.stats.StatsToStore(p.getName(), p.getUniqueId().toString());
		sts.add(stats);
	}
	
	public boolean hasStatsToStoreAccount(Player p){
		if(sts.contains(getStatsToStoreByUUID(p.getUniqueId().toString()))){
			return true;
		}
		return false;
	}
	
	public void sendStatsToBase(){
		for(net.weswaas.oniziacuhc.stats.StatsToStore sts1 : sts){
			String uuid = sts1.getUUID();
			net.weswaas.oniziacuhc.stats.PlayerData data = getPlayerData(uuid);
			if(data != null){
				sendStatsToBase(uuid, getPlayerData(uuid));
			}
		}
	}
	
	public boolean isCreated(String uuid){
		return pData.containsKey(uuid);
	}
	
	@SuppressWarnings("deprecation")
	public Inventory getStatsInventory(String uuid, net.weswaas.oniziacuhc.stats.PlayerData data){

		OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
		String name = op.getName();
		
		Inventory inv = Bukkit.createInventory(null, 36, "§aStats for " + name);

		net.weswaas.oniziacuhc.stats.StatsToStore sts = getStatsToStoreByUUID(uuid);

		inv.setItem(0, new ItemBuilder(Material.BEACON).name("§aWins §8» §b" + (sts.getWins() + (data.getWins()))).build());
		inv.setItem(9, new ItemBuilder(Material.DIAMOND_SWORD).name("§aKills §8» §b" + (sts.getKills() + data.getKills())).build());
		inv.setItem(10, new ItemBuilder(Material.SKULL_ITEM).data((byte)2).name("§aDeaths §8» §b" + (sts.getDeaths() + data.getDeaths())).build());
		inv.setItem(11, new ItemBuilder(Material.NETHER_STAR).name("§aKDR §8» §b" + data.getKDR()).build());
		inv.setItem(15, new ItemBuilder(Material.IRON_ORE).name("§aIrons mined §8» §b" + (sts.getIronsMined() + data.getIrons())).build());
		inv.setItem(16, new ItemBuilder(Material.GOLD_ORE).name("§aGolds mined §8» §b" + (sts.getGoldsMined() + data.getGolds())).build());
		inv.setItem(17, new ItemBuilder(Material.DIAMOND_ORE).name("§aDiamonds mined §8» §b" + (sts.getDiamondsMined() + data.getDiamonds())).build());
		inv.setItem(18, new ItemBuilder(Material.getMaterial(383)).data((byte)92).name("§aCows killed §8» §b" + (sts.getCowsKilled() + data.getCows())).build());
		inv.setItem(19, new ItemBuilder(Material.getMaterial(383)).data((byte)93).name("§aChickens killed §8» §b" + (sts.getChickensKilled() + data.getChickens())).build());
		inv.setItem(20, new ItemBuilder(Material.getMaterial(383)).data((byte)90).name("§aPigs killed §8» §b" + (sts.getPigsKilled() + data.getPigs())).build());
		inv.setItem(25, new ItemBuilder(Material.PAPER).name("§aGames played §8» §b" + (sts.getGamesPlayed() + data.getGamesPlayed())).build());
		inv.setItem(26, new ItemBuilder(Material.NETHERRACK).name("§aNethers entered §8» §b" + (sts.getNethersEntered() + data.getNethersEntered())).build());
		inv.setItem(27, new ItemBuilder(Material.GOLDEN_APPLE).name("§aGolden apples eaten §8» §b" + (sts.getGapplesEaten() + data.getGapplesEaten())).build());
		inv.setItem(28, new ItemBuilder(Material.GOLDEN_APPLE).name("§aGolden heads eaten §8» §b" + (sts.getGHeadsEaten() + data.getGoldenHeadsEaten())).build());
		inv.setItem(35, new ItemBuilder(Material.SADDLE).name("§aHorses tamed §8» §b" + (sts.getHorsesTamed() + data.getHorsesTamed())).build());

		
		return inv;
	}
	
	public void sendStatsToBase(String uuid, net.weswaas.oniziacuhc.stats.PlayerData data){
		try{
			
			int deaths = 1;
			if(data.getDeaths() > 0){
				deaths = data.getDeaths();
			}
			double decimals = (data.getKills() / deaths);
			DecimalFormat df = new DecimalFormat("#.##");
			String kdr = df.format(decimals);
			
			net.weswaas.oniziacuhc.stats.StatsToStore stats = this.getStatsToStoreByUUID(uuid);
			PreparedStatement sts = sql.getConnection().prepareStatement("UPDATE " + "uhc" + " SET kills=kills+?, deaths=deaths+?, wins=wins+?, diamonds=diamonds+?, golds=golds+?, irons=irons+?, cows=cows+?, chickens=chickens+?, "
					+ "pigs=pigs+?, games=games+?, nethers=nethers+?, gapples=gapples+?, gheads=gheads+?, horsestamed=horsestamed+?, kdr=? WHERE uuid=?");
			
			sts.setInt(1, stats.getKills());
			sts.setInt(2, stats.getDeaths());
			sts.setInt(3, stats.getWins());
			sts.setInt(4, stats.getDiamondsMined());
			sts.setInt(5, stats.getGoldsMined());
			sts.setInt(6, stats.getIronsMined());
			sts.setInt(7, stats.getCowsKilled());
			sts.setInt(8, stats.getChickensKilled());
			sts.setInt(9, stats.getPigsKilled());
			sts.setInt(10, stats.getGamesPlayed());
			sts.setInt(11, stats.getNethersEntered());
			sts.setInt(12, stats.getGapplesEaten());
			sts.setInt(13, stats.getGHeadsEaten());
			sts.setInt(14, stats.getHorsesTamed());
			sts.setString(15, kdr);
			sts.setString(16, uuid);
			
			sts.executeUpdate();
			sts.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

}
