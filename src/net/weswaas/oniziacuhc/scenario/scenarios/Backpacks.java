package net.weswaas.oniziacuhc.scenario.scenarios;

import com.weswaas.api.utils.ItemBuilder;
import net.weswaas.oniziacuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.Listener;

/**
 * Created by Weswas on 21/04/2020.
 */
public class Backpacks extends Scenario implements Listener {

    public Backpacks(){
        super("Backpacks", "Share an extra inventory with your team mate(s).");

        slot = 33;
        setMaterial(new ItemBuilder(Material.IRON_PICKAXE).name("§aBackpacks").build());
    }



}

