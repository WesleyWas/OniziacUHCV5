package net.weswaas.oniziacuhc.stats;

public class PlayerData {

	private String name;
	private String uuid;
	private int kills;
	private int deaths;
	private String kdr;
	private int wins;
	private int diamonds;
	private int golds;
	private int irons;
	private int cows;
	private int pigs;
	private int chickens;
	private int games;
	private int nethers;
	private int gapples;
	private int gheads;
	private int horsestamed;
	
	public PlayerData(String name, String uuid, int kills, int deaths, String kdr, int wins, int diamonds, int golds, int irons, int cows, int chickens, 
			int pigs, int games, int nethers, int gapples, int gheads, int horsestamed) {
		
		this.name = name;
		this.uuid = uuid;
		this.kills = kills;
		this.deaths = deaths;
		this.kdr = kdr;
		this.wins = wins;
		this.diamonds = diamonds;
		this.golds = golds;
		this.irons = irons;
		this.cows = cows;
		this.chickens = chickens;
		this.pigs = pigs;
		this.games = games;
		this.nethers = nethers;
		this.gapples = gapples;
		this.gheads = gheads;
		this.horsestamed = horsestamed;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getUUID(){
		return this.uuid;
	}
	
	public int getKills(){
		return this.kills;
	}
	
	public int getDeaths(){
		return this.deaths;
	}
	
	public String getKDR(){
		return this.kdr;
	}
	
	public int getWins(){
		return this.wins;
	}
	
	public int getDiamonds(){
		return this.diamonds;
	}
	
	public int getGolds(){
		return this.golds;
	}
	
	public int getIrons(){
		return this.irons;
	}
	
	public int getCows(){
		return this.cows;
	}
	
	public int getChickens(){
		return this.chickens;
	}
	
	public int getPigs(){
		return this.pigs;
	}
	
	public int getGamesPlayed(){
		return this.games;
	}
	
	public int getNethersEntered(){
		return this.nethers;
	}
	
	public int getGapplesEaten(){
		return this.gapples;
	}
	
	public int getGoldenHeadsEaten(){
		return this.gheads;
	}
	
	public int getHorsesTamed(){
		return this.horsestamed;
	}

}
