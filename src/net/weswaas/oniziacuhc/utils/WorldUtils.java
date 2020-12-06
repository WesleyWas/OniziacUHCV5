package net.weswaas.oniziacuhc.utils;

import net.weswaas.oniziacuhc.Game;
import net.weswaas.oniziacuhc.OniziacUHC;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("deprecation")
public class WorldUtils {
	
	private Game game;
    private World spawnWorld;
    private World gameWorld;
    private World arenaWorld;
	
	public WorldUtils(Game game) {
		
		this.game = game;
	}
	
	public Location spawnLoc = new Location(Bukkit.getWorld("lobby"), 0, 101, 0).setDirection(new Vector(-5, 0, 0));
	
	public ArrayList<Location> notGenerated = new ArrayList<Location>();
	
	public World getSpawnWorld(){
		return spawnWorld;
	}
	
	public World getGameWorld(){
		return gameWorld;
	}
	
	public void createWorld(){
		WorldCreator wc = new WorldCreator("lobby");
		wc.environment(Environment.NORMAL);
		wc.createWorld();
		
		WorldCreator wc2 = new WorldCreator("arena");
		wc2.environment(Environment.NORMAL);
		wc2.createWorld();
	}

	public void load(){
        spawnWorld = Bukkit.getWorld("lobby");
        gameWorld = Bukkit.getWorld("world");
        arenaWorld = Bukkit.getWorld("arena");

        gameWorld.setPVP(false);
        spawnWorld.setPVP(false);
        arenaWorld.setGameRuleValue("doNaturalRegen", "false");
        gameWorld.setGameRuleValue("doNaturalRegen", "false");
        arenaWorld.setPVP(true);
        spawnWorld.setGameRuleValue("doMobSpawning", "false");
        arenaWorld.setGameRuleValue("doMobSpawning", "false");
        arenaWorld.setDifficulty(Difficulty.NORMAL);
        gameWorld.setGameRuleValue("doDaylightCycle", "false");
        arenaWorld.setGameRuleValue("doDaylightCycle", "false");
        spawnWorld.setGameRuleValue("doDaylightCycle", "false");
	}
	
	public Location getSpawnLoc(){
		return spawnLoc;
	}
	
	public void generateChunks(final Player p, ArrayList<Location> locs){
		
		for(Location loc : locs){
			notGenerated.add(loc);
		}
		
		new BukkitRunnable() {
			public void run() {
				
				if(p.getLocation().getChunk().isLoaded()){
					Random r = new Random();
					Location randomLoc = notGenerated.get(r.nextInt(notGenerated.size()));
					p.teleport(randomLoc);
					notGenerated.remove(randomLoc);
					p.getLocation().getChunk().load();
				}
				if(notGenerated.size() <= 0){
					this.cancel();
					Bukkit.broadcastMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Chunk generation is now finished.");
					game.setGenerated(true);
					p.teleport(new Location(Bukkit.getWorld("lobby"), 0, 101, 0).setDirection(new Vector(-5, 0, 0)));
					new SoundsUtils(p).playSounds(Sound.ANVIL_USE);
				}
				
			}
		}.runTaskTimer(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 0, 40);
		
	}

	public static void verifyCenter(){

		World world = Bukkit.getWorld("world");

		if(WorldUtils.isCenterValid(world)){
			OniziacUHC.getInstance().getGame().setMapOK(true);
		}else{
			net.weswaas.oniziacuhc.OniziacUHC.getInstance().getLogger().info("NOT VALID SPAWN, RESTARTING");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
		}

	}

	public static boolean isCenterValid(World world){

		int waterCount = 0;
		int highestY = 50;
		int lowestY = 52;

		boolean isWaterCorrect = true;
		boolean isDeniveledCorrect = true;

		for(int i = -100; i < 100; i++){
			for(int j = -100; j < 100; j++){
				Block block = world.getHighestBlockAt(i, j).getLocation().add(0, -1, 0).getBlock();
				if(block.getType() == Material.STATIONARY_LAVA || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER || block.getType() == Material.LAVA){
					waterCount++;
				}
				int y = world.getHighestBlockYAt(i, j);
				if(y < lowestY){
					lowestY = y;
				}
				if(y > highestY){
					highestY = y;
				}
			}
		}

		//1000
		if(waterCount >= 1000){
			Bukkit.getLogger().info(ChatColor.RED + "Invalid center, too much water/lava (" + waterCount + ")");
			isWaterCorrect = false;
		}

		//35
		if((highestY - lowestY) > 35){
			Bukkit.getLogger().info(ChatColor.RED + "Invalid center, too much moutains (" + (highestY - lowestY) + " blocks difference)");
			isDeniveledCorrect = false;
		}

		return (isWaterCorrect && isDeniveledCorrect);

	}
	
	public void gheadRecipe(){

		ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("Golden Head");
		item.setItemMeta(itemMeta);
		@SuppressWarnings("unused")
		ItemStack crane = new ItemStack(Material.SKULL_ITEM, 1,(byte)3);
		
		ShapedRecipe recipe = new ShapedRecipe(item);
		recipe.shape("GGG","GHG","GGG");
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('H', Material.SKULL_ITEM,(byte)3);
		
		Bukkit.getServer().addRecipe(recipe);
	}

}
