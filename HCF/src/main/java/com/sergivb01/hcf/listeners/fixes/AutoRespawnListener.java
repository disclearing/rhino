package com.sergivb01.hcf.listeners.fixes;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class AutoRespawnListener implements Listener{
	private HCF plugin;

	public AutoRespawnListener(HCF plugin){
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

    /*@EventHandler //Laggy af
    public void onPlayerDie(PlayerDeathEvent event){
        if(ConfigurationService.KIT_MAP){
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                event.getEntity().getPlayer().spigot().respawn();

            }
            }, 0, 10);
        }
    }*/


}
