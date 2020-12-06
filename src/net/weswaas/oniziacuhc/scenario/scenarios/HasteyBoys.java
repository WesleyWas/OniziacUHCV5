package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.GameState;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Weswas on 21/04/2020.
 */
public class HasteyBoys extends Scenario implements Listener{

    public HasteyBoys(){
        super("HasteyBoys", "Mining faster by getting Efficiency 3 and Unbreaking 3 on your tools.");
        slot = 32;
        setMaterial(new ItemBuilder(Material.DIAMOND_PICKAXE).name("Hastey Boys").build());
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){

        if(!isEnabled()){
            return;
        }

        ItemStack item = e.getCurrentItem();

        if(item == null){
            return;
        }

        Material id = item.getType();

        if(!GameState.isState(GameState.GAME)){
            return;
        }

        if(e.getSlotType() != InventoryType.SlotType.RESULT){
            return;
        }

        if(id == Material.WOOD_PICKAXE || id == Material.WOOD_AXE || id == Material.WOOD_SPADE || id == Material.STONE_PICKAXE || id == Material.STONE_AXE || id == Material.STONE_SPADE
                || id == Material.IRON_AXE || id == Material.IRON_PICKAXE || id == Material.IRON_SPADE || id == Material.DIAMOND_AXE || id == Material.DIAMOND_PICKAXE || id == Material.DIAMOND_SPADE){

            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
            meta.addEnchant(Enchantment.DURABILITY, 3, true);

            item.setItemMeta(meta);

        }

    }

}
