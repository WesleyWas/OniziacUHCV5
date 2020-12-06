package net.weswaas.oniziacuhc.gui.guis;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Weswas on 01/05/2020.
 */
public class CheckStuffGUI extends GUI implements Listener{

    public CheckStuffGUI(){
        super("CheckStuff", "A GUI to check the diamond parts of players");
    }

    private Inventory inv;

    @Override
    public void create() {

        inv = Bukkit.createInventory(null, 18, "§4CheckStuffGUI");

    }

    @Override
    public void open(final Player p) {
        fill(p);

        p.openInventory(inv);
    }

    @Override
    public void fill(Player p) {

        ItemStack one = new ItemBuilder(Material.DIAMOND_CHESTPLATE).amount(1).name("§4Players with 1 diamond part").build();
        ItemStack two = new ItemBuilder(Material.DIAMOND_CHESTPLATE).amount(2).name("§4Players with 2 diamond parts").build();
        ItemStack three = new ItemBuilder(Material.DIAMOND_CHESTPLATE).amount(3).name("§4Players with 3 diamond parts").build();
        ItemStack four = new ItemBuilder(Material.DIAMOND_CHESTPLATE).amount(4).name("§4Players with 4 diamond parts").build();

        inv.setItem(3, one);
        inv.setItem(4, two);
        inv.setItem(5, three);
        inv.setItem(13, four);

    }

    @EventHandler
    public void on(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        Inventory inv = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();

        if(item == null){
            return;
        }

        if(!this.inv.getTitle().equals(inv.getTitle())){
            return;
        }

        e.setCancelled(true);

        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        if(item.getItemMeta().getDisplayName().contains("1")){
            p.performCommand("checkstuff 1");
        }else if(item.getItemMeta().getDisplayName().contains("2")){
            p.performCommand("checkstuff 2");
        }else if(item.getItemMeta().getDisplayName().contains("3")){
            p.performCommand("checkstuff 3");
        }else if(item.getItemMeta().getDisplayName().contains("4")){
            p.performCommand("checkstuff 4");
        }

        p.closeInventory();
    }

}
