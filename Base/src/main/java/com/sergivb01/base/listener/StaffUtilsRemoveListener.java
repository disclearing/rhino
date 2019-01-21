package com.sergivb01.base.listener;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.user.BaseUser;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class StaffUtilsRemoveListener implements Listener{

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId());
		if(!player.hasPermission("base.staffdetection")){
			if(baseUser.isStaffUtil()) baseUser.setStaffUtil(false); // Remove ModMode
			if(baseUser.isVanished()) baseUser.setVanished(false); // Remove Vanish
			if(player.getGameMode().equals(GameMode.CREATIVE)){ // Remove Gamemode && Inventory contents
				player.setGameMode(GameMode.SURVIVAL);
				player.getInventory().clear();
				player.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
			}
		}
	}
}
