package net.weswaas.oniziacuhc.listeners;

import net.weswaas.oniziacuhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * Created by Weswas on 21/04/2020.
 */
public class ChunkListener implements Listener{


    private Game game;

    public ChunkListener(Game game){
        this.game = game;
    }

    private static int random = 1 + (int)(Math.random() * 2);

    @EventHandler
    public void SpawnBiomeChanger(ChunkLoadEvent event)
    {
        if (event.isNewChunk() && (event.getWorld() == Bukkit.getServer().getWorlds().get(0))){
            int X = event.getChunk().getX();
            int Z = event.getChunk().getZ();
            if (X >= -6 && X <= 6 && Z >= -6 && Z <= 6){
                for (int x=0; x<16; x++){
                    for (int z=0; z<16; z++){
                        final Block block = event.getChunk().getBlock(x, event.getChunk().getWorld().getHighestBlockYAt(x,z) , z);
                        switch (random) {
                            case 1:
                                block.setBiome(Biome.PLAINS);
                                break;
                            case 2:
                                block.setBiome(Biome.DESERT);
                                break;
                        }
                    }
                }
            }
        }
    }

}
