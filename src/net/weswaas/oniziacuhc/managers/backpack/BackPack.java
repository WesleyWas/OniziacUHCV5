package net.weswaas.oniziacuhc.managers.backpack;

import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by Weswas on 21/04/2020.
 */
public class BackPack {

    private Inventory inv;
    private Team team;

    public BackPack(Team team){

        this.team = team;
        inv = Bukkit.createInventory(null, 27, "Backbacks");
    }

    public void open(Player p){

        p.openInventory(inv);

    }

    public Team getTeam(){
        return this.team;
    }

}
