package com.sergivb01.hcf.listeners;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.faction.type.Faction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CoreListener
		implements Listener{
	private final HCF plugin;

	public CoreListener(HCF plugin){
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
		if(reason == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT){ // allow slimes to always split
			return;
		}

		Location location = event.getLocation();
		Faction factionAt = plugin.getFactionManager().getFactionAt(location);
		if(factionAt.isSafezone() && reason == CreatureSpawnEvent.SpawnReason.SPAWNER){ // allow creatures to spawn in safe-zones by Spawner
			return;
		}
		if(factionAt.isSafezone() && reason == CreatureSpawnEvent.SpawnReason.NATURAL){
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onLightning(BlockIgniteEvent event){
		if(event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING){
			event.setCancelled(true);
		}
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		event.setJoinMessage(null);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerKickEvent event){
		event.setLeaveMessage(null);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event){
		event.setQuitMessage(null);
		Player player = event.getPlayer();
		this.plugin.getVisualiseHandler().clearVisualBlocks(player, null, null, false);
		this.plugin.getUserManager().getUser(player.getUniqueId()).setShowClaimMap(false);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		this.plugin.getVisualiseHandler().clearVisualBlocks(player, null, null, false);
		this.plugin.getUserManager().getUser(player.getUniqueId()).setShowClaimMap(false);
	}
}

