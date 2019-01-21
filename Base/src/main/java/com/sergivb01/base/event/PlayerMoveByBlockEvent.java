package com.sergivb01.base.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerMoveByBlockEvent
		extends PlayerEvent
		implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private Location from;
	private Location to;
	private boolean cancelled;

	public PlayerMoveByBlockEvent(Player player, Location to, Location from){
		super(player);
		this.from = from;
		this.to = to;
	}

	public static HandlerList getHandlerList(){
		return handlers;
	}

	public Location getFrom(){
		return this.from;
	}

	public void setFrom(Location from){
		this.from = from;
	}

	public Location getTo(){
		return this.to;
	}

	public void setTo(Location to){
		this.to = to;
	}

	public boolean isCancelled(){
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled){
		this.cancelled = cancelled;
	}

	public HandlerList getHandlers(){
		return handlers;
	}
}

