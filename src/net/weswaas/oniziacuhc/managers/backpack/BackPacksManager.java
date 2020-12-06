package net.weswaas.oniziacuhc.managers.backpack;

import net.weswaas.oniziacuhc.team.Team;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

/**
 * Created by Weswas on 21/04/2020.
 */
public class BackPacksManager {

    public static HashMap<Team, Inventory> teamInv = new HashMap<>();

    public BackPacksManager(){



    }

    public BackPack getBackPack(Team team){
        for(Team t : teamInv.keySet()){
            if(t == team){
                return t.getBackPack();
            }
        }
        return null;
    }

}
