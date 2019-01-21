package com.sergivb01.hcf.listeners;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HitDetectionListener implements Listener{

	public HitDetectionListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void onEnable(){

		for(Player player : Bukkit.getOnlinePlayers()){
			player.setMaximumNoDamageTicks(19);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		event.getPlayer().setMaximumNoDamageTicks(19);
	}

}
