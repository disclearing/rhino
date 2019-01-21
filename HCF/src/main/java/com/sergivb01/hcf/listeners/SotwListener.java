package com.sergivb01.hcf.listeners;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SotwListener implements Listener{
	private final HCF plugin;

	public SotwListener(HCF plugin){
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityDamage(final EntityDamageEvent event){
		if(event.getEntity() instanceof Player && event.getCause() != EntityDamageEvent.DamageCause.SUICIDE && this.plugin.getSotwTimer().getSotwRunnable() != null && !plugin.getSotwTimer().getSotwRunnable().isSotwEnabled((Player) event.getEntity())){
			event.setCancelled(true);
		}
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player && plugin.getSotwTimer().getSotwRunnable() != null && !plugin.getSotwTimer().getSotwRunnable().isSotwEnabled((Player) event.getDamager())) {
			event.setCancelled(true);
		}
	}
}