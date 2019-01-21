package com.sergivb01.hcf.listeners.fixes;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Random;

public class HungerFixListener implements Listener{

	public HungerFixListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void FixIt(FoodLevelChangeEvent e){
		if((e.getFoodLevel() < ((Player) e.getEntity()).getFoodLevel()) &&
				(new Random().nextInt(100) > 4)){
			e.setCancelled(true);
		}
	}
}