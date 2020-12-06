package net.weswaas.oniziacuhc.managers;

import com.google.common.collect.ImmutableSet;
import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.Timer;
import net.weswaas.oniziacuhc.commands.game.LatescatterCommand;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.team.Team;
import net.weswaas.oniziacuhc.utils.SoundsUtils;
import net.weswaas.oniziacuhc.utils.WorldUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ScatterManager {
	
	private WorldUtils wu;
	private Game game;
	private Timer timer;
	private ScoreboardManager board;
	private PlayerDataManager data;
    private SpectatorManager spec;
	
	private BukkitRunnable task;
	
	public ScatterManager(WorldUtils wu, Game game, Timer timer, ScoreboardManager board, PlayerDataManager data, SpectatorManager spec) {
		
		this.wu = wu;
		this.game = game;
		this.timer = timer;
		this.board = board;
		this.data = data;
        this.spec = spec;
	}
	
	private static final Set<Material> INVAILD_SPAWN_BLOCKS = ImmutableSet.of(
			Material.STATIONARY_WATER,
			Material.WATER, 
			Material.STATIONARY_LAVA,
			Material.LAVA, 
			Material.CACTUS,
			Material.LEAVES,
			Material.LEAVES_2,
			Material.STONE,
            Material.LOG,
            Material.LOG_2,
			Material.SNOW
	);
	
	public static final Set<PotionEffect> EFFECTS = ImmutableSet.of(
			new PotionEffect(PotionEffectType.JUMP, 999999, 128), 
			new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 6),
			new PotionEffect(PotionEffectType.SLOW, 999999, 6)
	);

	private void clearInv(Player randomPlayer){
		randomPlayer.getInventory().clear();
		randomPlayer.getInventory().setHelmet(null);
		randomPlayer.getInventory().setChestplate(null);
		randomPlayer.getInventory().setLeggings(null);
		randomPlayer.getInventory().setBoots(null);
		randomPlayer.updateInventory();
	}
	
	public void scatter(final ArrayList<Player> toScatter, final ArrayList<Location> locs){
		
		GameState.setState(GameState.SCATTERING);
		
		int toscattersize = toScatter.size();
		final int quart = toscattersize / 4;
		final int half = toscattersize / 2;
		final int threequart = (toscattersize / 4) * 3;
		
		final ArrayList<Player> toScatterReal = new ArrayList<Player>();
		final ArrayList<Location> locsReal = new ArrayList<Location>();
		
		for(Player toscatter : toScatter){
			toScatterReal.add(toscatter);
		}
		
		for(Location loc : locs){
			locsReal.add(loc);
		}
		
		task = new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				
				if(toScatterReal.size() <= 0){
					
					this.cancel();
					
					Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Scattering is now done.");
					GameState.setState(GameState.GAME);
					sidebar();
					for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
						pls.setFoodLevel(20);
						pls.setHealth(20);
						pls.setGameMode(GameMode.SURVIVAL);
						pls.setFlying(false);
						new SoundsUtils(pls).playSounds(Sound.NOTE_BASS);
						data.getStatsToStoreByUUID(pls.getUniqueId().toString()).addGame();
					}
					timer.gameTimer();
					potionEffects();
					lateHealing();
					
				}else{
					
					Random r = new Random();
					Location randomLoc = locsReal.get(r.nextInt(locsReal.size()));
					Player randomPlayer = toScatterReal.get(r.nextInt(toScatterReal.size()));

					clearInv(randomPlayer);
					
					if(game.getTeamSize() >= 2){
						if(Team.hasTeam(randomPlayer)){
							if(Team.getTeam(randomPlayer).isLeader(randomPlayer)){
								
								randomLoc.getChunk().load();
								randomPlayer.teleport(randomLoc);
								toScatterReal.remove(randomPlayer);
								locsReal.remove(randomLoc);
								
								for(String s : Team.getTeam(randomPlayer).getPlayerNames()){
									Player mates = Bukkit.getPlayer(s);
									mates.teleport(randomLoc);
									toScatterReal.remove(mates);
									for(PotionEffect pe : EFFECTS){
										if(mates.hasPotionEffect(pe.getType())){
											mates.removePotionEffect(pe.getType());
										}
										
										mates.addPotionEffect(pe);

										clearInv(mates);
									}
								}
								
								
							}else{
								toScatterReal.remove(randomPlayer);
								locsReal.remove(randomLoc);
							}
						}else{
							randomLoc.getChunk().load();
							randomPlayer.teleport(randomLoc);
							locsReal.remove(randomLoc);
							toScatterReal.remove(randomPlayer);
							for(PotionEffect pe : EFFECTS){
								if(randomPlayer.hasPotionEffect(pe.getType())){
									randomPlayer.removePotionEffect(pe.getType());
								}
								
								randomPlayer.addPotionEffect(pe);
							}
						}
					}else{
						randomLoc.getChunk().load();
						randomPlayer.teleport(randomLoc);
						toScatterReal.remove(randomPlayer);
						locsReal.remove(randomLoc);
					}
					
					for(PotionEffect pe : EFFECTS){
						if(randomPlayer.hasPotionEffect(pe.getType())){
							randomPlayer.removePotionEffect(pe.getType());
						}
						
						randomPlayer.addPotionEffect(pe);
					}

					LatescatterCommand.hasPlayed.add(randomPlayer.getUniqueId());
					
					if(toScatterReal.size() == quart){
						sendMsg(75);
					}else if(toScatterReal.size() == half){
						sendMsg(50);
					}else if(toScatterReal.size() == threequart){
						sendMsg(25);
					}
					
				}
			}
		};
		task.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 0, 40);
		
	}
	
	@SuppressWarnings("deprecation")
	private void lateHealing(){
		new BukkitRunnable() {
			public void run() {
				for(Player pls : net.weswaas.oniziacuhc.OniziacUHC.getInstance().inGamePlayers){
					pls.setHealth(20);
				}
			}
		}.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 200);
	}
	
	public void sendMsg(int percents){
		Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Scattering is now finished at " + percents + "%.");
	}
	
	@SuppressWarnings("deprecation")
	private void potionEffects(){
		for(Player pls : Bukkit.getOnlinePlayers()){
			for(PotionEffect pe : pls.getActivePotionEffects()){
				pls.removePotionEffect(pe.getType());
			}
		}
	}

	private void singleSidebar(Player p){
        if(board.boards.containsKey(p.getName())){
            p.setScoreboard((Scoreboard) board.boards.get(p.getName()));
        }else{
            board.createSidebar(p);
        }
	}
	
	@SuppressWarnings("deprecation")
	private void sidebar(){
		for(Player pls : Bukkit.getOnlinePlayers()){
			board.createSidebar(pls);
		}
	}
	
	public ArrayList<Location> findLocations(int radius, int count){
		
		ArrayList<Location> foundedLocs = new ArrayList<Location>();
		
		int boucle = 0;
		
		while(boucle < count){
			
			Random rX = new Random();
			Random rZ = new Random();

            boolean negX = (Math.random() < 0.5);
            boolean negZ = (Math.random() < 0.5);
            int multiX = 1;
            int multiZ = 1;
            if(negX){
                multiX = -1;
            }
            if(negZ){
                multiZ = -1;
            }
			
			int x = multiX * rX.nextInt(radius - 5);
			int z = multiZ * rZ.nextInt(radius - 5);
			
			Location loc = new Location(wu.getGameWorld(), x + 0.5, 0, z + 0.5);
			
			if(isValid(loc.clone()) && isNotToNear(loc, foundedLocs)){
                loc.getChunk().load();
				loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 2);
				foundedLocs.add(loc);
				boucle++;
			}
			
		}
		
		return foundedLocs;
		
	}

	public boolean isNotToNear(Location loc, ArrayList<Location> alreadyFound){

        if(alreadyFound.size() == 0){
            return true;
        }

		boolean isOK = false;
		for(Location found : alreadyFound){
			double distance = Math.sqrt(((found.getBlockX() - loc.getBlockX()) * (found.getBlockX() - loc.getBlockX())) + ((found.getBlockY() - loc.getBlockY()) * found.getBlockY() - loc.getBlockY()));
			if(distance > 300){
				isOK = true;
				Bukkit.getServer().getLogger().info("Location in (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ") is away from location in (" + found.getBlockX() + ", " + found.getBlockY() + ", " + found.getBlockZ() + ") from " + (int)distance + " blocks");
			}
		}

		return isOK;
	}
	
	public boolean isValid(Location loc){
		boolean valid = true;
		
		loc.setY(loc.getWorld().getHighestBlockYAt(loc));
		
		Material type = loc.getBlock().getType();
		Material type2 = loc.add(0, -1, 0).getBlock().getType();
		Material type3 = loc.add(0, -1, 0).getBlock().getType();
		
		if(loc.getBlockY() < 60){
			valid = false;
		}
		
		for(Material mat : INVAILD_SPAWN_BLOCKS){
			if(type == mat || type2 == mat || type3 == mat){
				valid = false;
			}
		}
		
		return valid;
	}

	public void lateScatter(final Player p){

        p.getInventory().clear();
        spec.remove(p);
        new Team(p);
        Location loc = findLocations(game.getRadius(), 1).get(0);
        loc.getChunk().load();
        p.teleport(loc);
        p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "You have been latescattered.");

        for(PotionEffect pe : p.getActivePotionEffects()){
            p.removePotionEffect(pe.getType());
        }

        for(Player pls : Bukkit.getOnlinePlayers()){
            if(pls.hasPermission("uhc.admin")){
                pls.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§3[Staff] §7" + p.getName() + " has been scattered at location x:" + loc.getBlockX() + ", y: " + loc.getBlockY() + ", z: " + loc.getBlockZ());
            }
        }

        new BukkitRunnable(){
            public void run() {
                p.setHealth(20.0);
                p.setFoodLevel(20);
                singleSidebar(p);
                p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
                p.getInventory().addItem(new ItemStack(Material.BOOK, 1));
            }
        }.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 20);

    }

}
