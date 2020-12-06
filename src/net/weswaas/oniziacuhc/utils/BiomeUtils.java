package net.weswaas.oniziacuhc.utils;

import net.minecraft.server.v1_7_R4.BiomeBase;

import java.lang.reflect.Field;
import java.util.Arrays;

public class BiomeUtils {
	
	 private static Class<?> biomeBase;
	  private static Field biomeField;
	  private static Object[] biomesCopy;

	  static
	  {
	    try
	    {
	      biomeBase = BiomeBase.class;
	      biomeField = biomeBase.getDeclaredField("biomes");
	      biomeField.setAccessible(true);
	      biomesCopy = Arrays.copyOf(getBiomes(), 256);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  public static void changeBiome(int id, int toId) {
	    Object[] biomes = getBiomes();
	    biomes[id] = biomesCopy[toId];
	  }

	  public static Object[] getBiomes() {
	    try {
	      return (Object[])biomeField.get(null);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return new Object[119];
	  }

}
