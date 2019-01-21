package com.sergivb01.hcf.listeners.fixes;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EndermanFixListener implements Listener{

	public EndermanFixListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onEnderDamage(EntityDamageEvent e){
		if((e.getEntity() instanceof Player) && (e.getCause().equals(EntityType.ENDERMAN))){
			e.setCancelled(true);
		}
	}
}