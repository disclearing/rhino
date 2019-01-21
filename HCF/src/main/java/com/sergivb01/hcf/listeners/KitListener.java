package com.sergivb01.hcf.listeners;

import com.sergivb01.base.kit.event.KitApplyEvent;
import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.faction.type.Faction;
import com.sergivb01.hcf.faction.type.PlayerFaction;
import com.sergivb01.hcf.timer.event.TimerStartEvent;
import com.sergivb01.hcf.timer.type.PvpProtectionTimer;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class KitListener implements Listener{
	private final HCF plugin;

	public KitListener(HCF plugin){
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onTimer(TimerStartEvent e){
		if(ConfigurationService.KIT_MAP && e.getTimer() instanceof PvpProtectionTimer){
			this.plugin.getTimerManager().pvpProtectionTimer.clearCooldown(e.getUserUUID().get());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onKitApply(KitApplyEvent event){
		PlayerFaction playerFaction;
		Player player = event.getPlayer();
		Location location = player.getLocation();
		Faction factionAt = this.plugin.getFactionManager().getFactionAt(location);
		if(!(factionAt.isSafezone() || (playerFaction = this.plugin.getFactionManager().getPlayerFaction(player)) != null && playerFaction.equals(factionAt))){
			player.sendMessage(ChatColor.RED + "Kits can only be applied in safe-zones or your own claims.");
			event.setCancelled(true);
		}
	}
}

