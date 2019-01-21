package com.sergivb01.hcf.listeners.fixes;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherFixListener implements Listener{

	public WeatherFixListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e){
		if(e.getWorld().getEnvironment() == World.Environment.NORMAL){
			e.getWorld().setWeatherDuration(0);
		}
	}
}

