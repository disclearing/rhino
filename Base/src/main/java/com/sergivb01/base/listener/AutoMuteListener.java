package com.sergivb01.base.listener;

import com.sergivb01.base.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class AutoMuteListener implements Listener{

	private final BasePlugin plugin;
	private int cooldownToAdvert = 3;
	private int cooldownToRemoveAdvert = 600;
	private HashMap<String, Integer> messageCount = new HashMap<>();
	private HashMap<String, Integer> totalAdverts = new HashMap<>();

	public AutoMuteListener(BasePlugin plugin){
		this.plugin = plugin;
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	private void autoMute(AsyncPlayerChatEvent event){
		long remainingChatSlowed = this.plugin.getServerHandler().getRemainingChatSlowedMillis();
		long remainingChatDisabled = this.plugin.getServerHandler().getRemainingChatDisabledMillis();
		if(!(remainingChatDisabled > 0) || !(remainingChatSlowed > 0)){
			Player player = event.getPlayer();
			String playerString = player.getName();

			if(player.hasPermission("automute.bypass")){
				return;
			}

			if(!messageCount.containsKey(playerString)){
				toAdvert(playerString);
			}
			addMessage(playerString);

			if(messageCount.get(playerString) == 3){
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Please slow down!");
			}

			if(messageCount.get(playerString) == 4){
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You have recieved a warning for spamming.");
				addAdvert(playerString);
			}

		}
	}

	private void addMessage(final String playername){
		int messageInt;

		if(messageCount.containsKey(playername)){
			messageInt = messageCount.get(playername);
			messageCount.remove(playername);
		}else{
			messageInt = 0;
		}
		messageInt++;
		messageCount.put(playername, messageInt);
	}

	private void addAdvert(final String playername){
		int advertsInt;
		if(totalAdverts.containsKey(playername)){
			advertsInt = totalAdverts.get(playername);
			totalAdverts.remove(playername);
		}else{
			advertsInt = 0;
		}
		advertsInt++;
		totalAdverts.put(playername, advertsInt);
		if(totalAdverts.get(playername) == 2){
			// Final warning, removes all previous warnings
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute " + playername + " 5m [AutoMute] Spam -s");
			totalAdverts.remove(playername);
		}
	}

	private void toAdvert(final String playername){
		Bukkit.getScheduler().scheduleSyncDelayedTask(BasePlugin.getPlugin(), () -> messageCount.remove(playername), cooldownToAdvert * 20);
	}

	private void toRemoveAdverts(final String playername){
		Bukkit.getScheduler().scheduleSyncDelayedTask(BasePlugin.getPlugin(), () -> totalAdverts.remove(playername), cooldownToRemoveAdvert * 20);
	}
}
