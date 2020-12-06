package net.weswaas.oniziacuhc.stats;

import org.bukkit.entity.Player;

import java.sql.*;


public class SQLManager {
	
	private String url_base, host, name, username, password, table;
	public static Connection connection;
	private PlayerDataManager data;
	
	public SQLManager(String url_base, String host, String name, String username, String password, String table, PlayerDataManager data) {
		this.url_base = url_base;
		this.host = host;
		this.name = name;
		this.username = username;
		this.password = password;
		this.table = table;
		this.data = data;
	}
	
	public void connection(){
		if(!isConnected()){
			try{
				connection = DriverManager.getConnection(url_base + host + "/" + name, username, password);
                PreparedStatement sts = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (uuid VARCHAR(255), name VARCHAR(255), kills INTEGER NOT NULL DEFAULT 0, deaths INTEGER NOT NULL DEFAULT 0, kdr VARCHAR(255), wins INTEGER NOT NULL DEFAULT 0, diamonds INTEGER NOT NULL DEFAULT 0, golds INTEGER NOT NULL DEFAULT 0, irons INTEGER NOT NULL DEFAULT 0, cows INTEGER NOT NULL DEFAULT 0, chickens INTEGER NOT NULL DEFAULT 0, pigs INTEGER NOT NULL DEFAULT 0, games INTEGER NOT NULL DEFAULT 0, nethers INTEGER NOT NULL DEFAULT 0, gapples INTEGER NOT NULL DEFAULT 0, gheads INTEGER NOT NULL, horsestamed INTEGER NOT NULL DEFAULT 0)");
                sts.executeUpdate();
                sts.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public void deconnection(){
		if(isConnected()){
			try{
				connection.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected(){
		try{
			if((connection == null) || connection.isClosed() || (!connection.isValid(5))){
				return false;
			}else{
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	public Connection getConnection() {
		if (!isConnected()) {
			connection();
		}

		return this.connection;
	}

	public boolean hasAccount(String uuid){
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT kills FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				sts.close();
				return false;
			}else{
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public void createAccount(Player p){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("SELECT kills FROM " + table + " WHERE UUID = ?");
			sts.setString(1, p.getUniqueId().toString());
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				sts.close();
				PreparedStatement sts2 = getConnection().prepareStatement("INSERT INTO " + table + " (uuid, name, kills, deaths, kdr, wins, diamonds, golds, irons, cows, chickens, pigs, games, nethers, gapples, gheads, horsestamed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				sts2.setString(1, p.getUniqueId().toString());
				sts2.setString(2, p.getName());
				sts2.setInt(3, 0);
				sts2.setInt(4, 0);
				sts2.setString(5, "0.00");
				sts2.setInt(6, 0);
				sts2.setInt(7, 0);
				sts2.setInt(8, 0);
				sts2.setInt(9, 0);
				sts2.setInt(10, 0);
				sts2.setInt(11, 0);
				sts2.setInt(12, 0);
				sts2.setInt(13, 0);
				sts2.setInt(14, 0);
				sts2.setInt(15, 0);
				sts2.setInt(16, 0);
				sts2.setInt(17, 0);
				sts2.executeUpdate();
				sts2.close();
			}
			
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void createPlayerData(String uuid, String name){
		PlayerData pData = new PlayerData(name, uuid, getKills(uuid), getDeaths(uuid), getKDR(uuid), getWins(uuid), getDiamondsMined(uuid), getGoldsMined(uuid), getIronsMined(uuid), getCowsKilled(uuid), getChickensKilled(uuid), getPigsKilled(uuid), getGamesPlayed(uuid), getNethersEntered(uuid), getGapplesEaten(uuid), getGHeadsEaten(uuid), getHorsesTamed(uuid));
		data.datas.add(pData);
		data.pData.put(uuid, pData);
	}
	
	public void addKills(String uuid, int kills){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET kills=kills+" + kills + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addDeath(String uuid){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET deaths=deaths+1 WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addWin(String uuid){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET wins=wins+1 WHERE uuid = ?");
			sts.setString(1, uuid.toString());
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addDiamonds(String uuid, int diamonds){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET diamonds=diamonds" + diamonds + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addGolds(String uuid, int golds){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET golds=golds" + golds + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addIrons(String uuid, int irons){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET irons=irons" + irons + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addCows(String uuid, int cows){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET cows=cows" + cows + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addChickens(String uuid, int chickens){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET chickens=chickens" + chickens + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addPigs(String uuid, int pigs){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET pigs=pigs" + pigs + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addGamesPlayed(String uuid){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET games=games+1 WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addNethers(String uuid, int nethers){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET nethers=nethers" + nethers + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addGapples(String uuid, int gapples){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET gapples=gapples" + gapples + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addGHeads(String uuid, int gheads){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET gheads=gheads" + gheads + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addHorses(String uuid, int horses){
		try{
			PreparedStatement sts = getConnection().prepareStatement("UPDATE " + table + " SET horses=horses" + horses + " WHERE uuid = ?");
			sts.setString(1, uuid);
			sts.executeUpdate();
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public int getKills(String uuid){
		int kills = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT kills FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return kills;
			}
			kills = rs.getInt("kills");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return kills;
	}
	
	public int getDeaths(String uuid){
		int deaths = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT deaths FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return deaths;
			}
			deaths = rs.getInt("deaths");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return deaths;
	}
	
	public String getKDR(String uuid){
		String kdr = "0.00";
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT kdr FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return kdr;
			}
			kdr = rs.getString("kdr");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return kdr;
	}
	
	public int getWins(String uuid){
		int wins = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT wins FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return wins;
			}
			wins = rs.getInt("wins");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return wins;
	}
	
	public int getDiamondsMined(String uuid){
		int diamonds = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT diamonds FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return diamonds;
			}
			diamonds = rs.getInt("diamonds");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return diamonds;
	}
	
	public int getGoldsMined(String uuid){
		int golds = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT golds FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return golds;
			}
			golds = rs.getInt("golds");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return golds;
	}
	
	public int getIronsMined(String uuid){
		int irons = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT irons FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return irons;
			}
			irons = rs.getInt("irons");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return irons;
	}
	
	public int getCowsKilled(String uuid){
		int cows = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT cows FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return cows;
			}
			cows = rs.getInt("cows");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return cows;
	}
	
	public int getChickensKilled(String uuid){
		int chickens = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT chickens FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return chickens;
			}
			chickens = rs.getInt("chickens");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return chickens;
	}
	
	public int getPigsKilled(String uuid){
		int pigs = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT pigs FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return pigs;
			}
			pigs = rs.getInt("pigs");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return pigs;
	}
	
	public int getGamesPlayed(String uuid){
		int games = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT games FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return games;
			}
			games = rs.getInt("games");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return games;
	}
	
	public int getNethersEntered(String uuid){
		int nethers = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT nethers FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return nethers;
			}
			nethers = rs.getInt("nethers");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return nethers;
	}
	
	public int getGapplesEaten(String uuid){
		int gapples = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT gapples FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return gapples;
			}
			gapples = rs.getInt("gapples");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return gapples;
	}
	
	public int getGHeadsEaten(String uuid){
		int gheads = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT gheads FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return gheads;
			}
			gheads = rs.getInt("gheads");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return gheads;
	}
	
	public int getHorsesTamed(String uuid){
		int horsestamed = 0;
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT horsestamed FROM " + table + " WHERE uuid = ?");
			sts.setString(1, uuid);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return horsestamed;
			}
			horsestamed = rs.getInt("horsestamed");
			sts.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return horsestamed;
	}

}
