package net.weswaas.oniziacuhc.stats;

public class StatsToStore {
	
	private String name = null;
	private String uuid = null;
	private int kills = 0;
	private int deaths = 0;
	private int wins = 0;
	private int diamonds = 0;
	private int golds = 0;
	private int irons = 0;
	private int cows = 0;
	private int pigs = 0;
	private int chickens = 0;
	private int games = 0;
	private int nethers = 0;
	private int gapples = 0;
	private int gheads = 0;
	private int horsestamed = 0;
	
	public StatsToStore(String name, String uuid) {
		
		this.name = name;
		this.uuid = uuid;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getUUID(){
		return this.uuid;
	}
	
	public void addKill(){
		this.kills = kills + 1;
	}
	
	public void addDeath(){
		this.deaths = deaths + 1;
	}
	
	public void addWin(){
		this.wins = wins + 1;
	}
	
	public void addDiamond(){
		this.diamonds = diamonds + 1;
	}
	
	public void addGold(){
		this.golds = golds + 1;
	}
	
	public void addIron(){
		this.irons = irons + 1;
	}
	
	public void addCow(){
		this.cows = cows + 1;
	}
	
	public void addChicken(){
		this.chickens = chickens + 1;
	}
	
	public void addPig(){
		this.pigs = pigs + 1;
	}
	
	public void addGame(){
		this.games = games + 1;
	}
	
	public void addNether(){
		this.nethers = nethers + 1;
	}
	
	public void addGapple(){
		this.gapples = gapples + 1;
	}
	
	public void addGHead(){
		this.gheads = gheads + 1;
	}
	
	public void addHorseTamed(){
		this.horsestamed = horsestamed + 1;
	}
	
	public int getKills(){
		return this.kills;
	}
	
	public int getDeaths(){
		return this.deaths;
	}
	
	public int getWins(){
		return this.wins;
	}
	
	public int getDiamondsMined(){
		return this.diamonds;
	}
	
	public int getGoldsMined(){
		return this.golds;
	}
	
	public int getIronsMined(){
		return this.irons;
	}
	
	public int getCowsKilled(){
		return this.cows;
	}
	
	public int getChickensKilled(){
		return this.chickens;
	}
	
	public int getPigsKilled(){
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
	
	public int getGHeadsEaten(){
		return this.gheads;
	}
	
	public int getHorsesTamed(){
		return this.horsestamed;
	}

}
