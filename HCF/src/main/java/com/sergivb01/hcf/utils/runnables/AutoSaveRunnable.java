package com.sergivb01.hcf.utils.runnables;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class AutoSaveRunnable implements Runnable{

	@Override
	public void run(){
		Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Starting backup of data");
		Bukkit.getWorlds().forEach(world -> {
			world.setThundering(false);
			world.setStorm(false);
		});

		HCF.getInstance().saveData();
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lAutoSave &eTask was completed."));
	}


}
