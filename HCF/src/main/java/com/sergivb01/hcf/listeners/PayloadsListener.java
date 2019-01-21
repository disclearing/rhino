package com.sergivb01.hcf.listeners;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.payloads.Cache;
import com.sergivb01.hcf.payloads.types.Payload;
import com.sergivb01.hcf.payloads.types.ServerSwitchPayload;
import com.sergivb01.hcf.payloads.types.StaffChatPayload;
import com.sergivb01.hcf.utils.StringUtils;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.stream.Collectors;

public class PayloadsListener implements Listener{

	public PayloadsListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(player.hasPermission("hcf.utils.staff")){
			if(ConfigurationService.REDIS_ENABLED){
				Payload payload = new ServerSwitchPayload(player.getUniqueId(), player.getName(), "joined");
				payload.send();
				Cache.addPayload(payload);
				return;
			}
			Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("hcf.utils.staff")).collect(Collectors.toList())
					.forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&5[Staff] &d" + player.getName() + " &7has joined the server."
					)));
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(player.hasPermission("hcf.utils.staff")){
			if(ConfigurationService.REDIS_ENABLED){
				Payload payload = new ServerSwitchPayload(player.getUniqueId(), player.getName(), "left");
				payload.send();
				Cache.addPayload(payload);
				return;
			}

			Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("hcf.utils.staff")).collect(Collectors.toList())
					.forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&5[Staff] &d" + player.getName() + " &7has left the server."
					)));
		}
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onStaffChatChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();

		if(player.hasPermission("hcf.command.staffchat") && BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isInStaffChat() && !event.getMessage().startsWith("!")){
			if(ConfigurationService.REDIS_ENABLED){
				Payload payload = new StaffChatPayload(player.getUniqueId(), player.getName(), event.getMessage());
				payload.send();
				Cache.addPayload(payload);
				event.setCancelled(true);
				return;
			}

			Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("hcf.utils.staff")).collect(Collectors.toList())
					.forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigurationService.STAFFCHAT
							.replace("%PLAYER%", player.getName())
							.replace("%MESSAGE%", event.getMessage())
					)));
			event.setCancelled(true);
		}
	}

}
