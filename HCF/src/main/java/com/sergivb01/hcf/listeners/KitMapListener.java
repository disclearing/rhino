package com.sergivb01.hcf.listeners;

import com.sergivb01.base.kit.event.KitApplyEvent;
import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.timer.event.TimerStartEvent;
import com.sergivb01.hcf.timer.type.PvpProtectionTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class KitMapListener implements Listener{
	final HCF plugin;

	public KitMapListener(HCF plugin){
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		event.setCancelled(true);
	}

	@EventHandler
	public void onTimer(TimerStartEvent e){
		if(e.getTimer() instanceof PvpProtectionTimer){
			this.plugin.getTimerManager().pvpProtectionTimer.clearCooldown(e.getUserUUID().get());
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(this.plugin.getTimerManager().pvpProtectionTimer.getRemaining(e.getPlayer()) >= 0){
			this.plugin.getTimerManager().pvpProtectionTimer.clearCooldown(e.getPlayer());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onKitApplyMonitor(KitApplyEvent event){
		Player player = event.getPlayer();
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
	}

}

