package net.weswaas.oniziacuhc.utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class BlockUtils {
	
	public static void degradeDurabiliy(final Player player) {
		final ItemStack item = player.getItemInHand();

        if (item.getType() == Material.AIR || item.getType() == Material.BOW || item.getType().getMaxDurability() == 0) {
            return;
        }
        
        short durability = item.getDurability();
        final Random rand = new Random();

        if (item.containsEnchantment(Enchantment.DURABILITY)) {
        	double chance = (100 / (item.getEnchantmentLevel(Enchantment.DURABILITY) + 1));
        	
        	if (rand.nextDouble() <= (chance / 100)) {
                durability++;
        	}
        } else {
            durability++;
        }

        if (durability >= item.getType().getMaxDurability()) {
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
            player.setItemInHand(new ItemStack(Material.AIR));
            return;
        }
        
        item.setDurability(durability);
        player.setItemInHand(item);
	}
	
	@SuppressWarnings("deprecation")
	public static void blockBreak(final Player player, final Block block) {
		for (Player online : block.getWorld().getPlayers()) {
        	if (player != null && online == player) {
        		continue;
        	}

        	online.playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
        }
	}
	
	public static void dropItem(final Location loc, final ItemStack toDrop) {
		new BukkitRunnable() {
        	public void run() {
        		// spawn item.
    			Item item = loc.getWorld().dropItem(loc, toDrop);
    			item.setVelocity(randomOffset());
        	}
        }.runTaskLater(net.weswaas.oniziacuhc.OniziacUHC.getInstance(), 2);
	}
	
	private static Vector randomOffset() {
		final Random rand = new Random();

		double offsetX = rand.nextDouble() / 20;
		double offsetZ = rand.nextDouble() / 20;

		offsetX = offsetX - (rand.nextDouble() / 20);
		offsetZ = offsetZ - (rand.nextDouble() / 20);
		
		return new Vector(offsetX, 0.2, offsetZ);
	}

}
