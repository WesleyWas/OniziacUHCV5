package net.weswaas.oniziacuhc.listeners.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FreezeEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private Player freezer;
	private Player frozen;
	
	public FreezeEvent(Player freezer, Player frozen) {
		
		this.freezer = freezer;
		this.frozen = frozen;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
		
	}
	
	public Player getFreezer(){
		return freezer;
	}
	
	public Player getFrozen(){
		return frozen;
	}

}
