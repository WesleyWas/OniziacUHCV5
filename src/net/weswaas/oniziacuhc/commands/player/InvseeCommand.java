package net.weswaas.oniziacuhc.commands.player;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.commands.UHCCommand;
import net.weswaas.oniziacuhc.managers.NickManager;
import net.weswaas.oniziacuhc.stats.PlayerDataManager;
import net.weswaas.oniziacuhc.stats.StatsToStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Weswas on 22/04/2020.
 */
public class InvseeCommand extends UHCCommand{

    private PlayerDataManager data;
    private NickManager nick;

    public InvseeCommand(PlayerDataManager data, NickManager nick) {
        super("invsee", "/invsee <player>");

        this.data = data;
        this.nick = nick;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            if(p.hasPermission("uhc.invsee") && !net.weswaas.oniziacuhc.OniziacUHC.getInstance().isInGame(p)){

                if(args.length == 1){

                    Player target = Bukkit.getPlayer(args[0]);
                    if(target != null && target.isOnline()){

                        String name = target.getName();

                        if(target.getName().length() > 14){
                            name = target.getName().substring(0, 14);
                        }

                        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Inventory §8» §7" + name);
                        StatsToStore sts = data.getStatsToStoreByUUID(target.getUniqueId().toString());

                        inv.setItem(4, new ItemBuilder(Material.NETHER_STAR).name(("§7Mining statistics for " + ChatColor.AQUA + target.getName() + " §7during §bTHIS §7game")).lore(" ")
                                .lore("§8» §7Diamonds: §b" + sts.getDiamondsMined())
                                .lore("§8» §7Golds: §b" + sts.getGoldsMined())
                                .lore("§8» §7Irons: §b" + sts.getIronsMined()).build());

                        for(int i = 0; i < 36; i++){
                            ItemStack item = target.getInventory().getItem((i));
                            if(item != null){
                                inv.setItem((i+9), item);
                            }
                        }

                        ItemStack boots = target.getInventory().getBoots() == null ? null : target.getInventory().getBoots();
                        ItemStack leg = target.getInventory().getLeggings() == null ? null : target.getInventory().getLeggings();
                        ItemStack chest = target.getInventory().getChestplate() == null ? null : target.getInventory().getChestplate();
                        ItemStack helmet = target.getInventory().getHelmet() == null ? null : target.getInventory().getHelmet();

                        inv.setItem(50, boots);
                        inv.setItem(51, leg);
                        inv.setItem(52, chest);
                        inv.setItem(53, helmet);

                        p.openInventory(inv);

                    }else{
                        p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "Player can't be found.");
                    }

                }else{
                    p.sendMessage(net.weswaas.oniziacuhc.OniziacUHC.PREFIX + "§cSynthax error. Please try with /invsee <player>");
                }

            }
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        // TODO Auto-generated method stub
        return null;
    }

}
