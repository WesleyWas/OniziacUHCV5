package net.weswaas.oniziacuhc.scenario;

import com.google.common.base.Splitter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
	private boolean enabled = false;
	
	private String name;
	private String description;
	
	protected ItemStack material;
	protected int slot = 0;
	
	protected Scenario(String name, String description){
		this.name = name;
		this.description = description;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean toggle(){
		return isEnabled() ? disable() : enable(net.weswaas.oniziacuhc.OniziacUHC.getInstance());
	}
	
	public boolean enable(net.weswaas.oniziacuhc.OniziacUHC plugin){
		if(isEnabled()){
			return false;
		}
		
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, plugin);
		}
		
		enabled = true;
		onEnable();
		return true;
	}
	
	public boolean disable(){
		if(!isEnabled()){
			return false;
		}
		
		if(this instanceof Listener){
			HandlerList.unregisterAll((Listener) this);
		}
		
		enabled = false;
		onDisable();
		return true;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void onEnable(){
		
	}
	
	public void onDisable(){
		
	}
	
	public int getSlot(){
		return this.slot;
	}
	
	public ItemStack getMaterial(){
		return this.material;
	}
	
	public void setMaterial(ItemStack item){
		this.material = item;
	}
	
	public ItemStack getItem(){
		
		ItemMeta meta = material.getItemMeta();
		
		List<String> lore = new ArrayList<String>();
		
		Iterable<String> pieces = Splitter.fixedLength(30).split(getDescription());
		
		lore.add(" ");
		lore.add("§8» §7Currently: " + (isEnabled() ? "§aEnabled" : "§cDisabled"));
		lore.add(" ");
		
		for(String s : pieces){
			lore.add("§8» §7" + s);
		}
		
		lore.add(" ");
		
		meta.setDisplayName("§8» §3" + getName());
		meta.setLore(lore);
		material.setItemMeta(meta);
		
		return material;
		
	}

}
