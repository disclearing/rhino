package com.sergivb01.hcf.listeners;

import com.sergivb01.hcf.HCF;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class AutoSmeltOreListener implements Listener{

	public AutoSmeltOreListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event){
		ItemStack stack;
		Player player = event.getPlayer();
		if(player.getGameMode() != GameMode.CREATIVE && player.hasPermission("hcf.autosmeltore") && (stack = player.getItemInHand()) != null && stack.getType() != Material.AIR && !stack.containsEnchantment(Enchantment.SILK_TOUCH)){
			Material dropType;
			Block block = event.getBlock();
			switch(block.getType()){
				case IRON_ORE:{
					dropType = Material.IRON_INGOT;
					break;
				}
				case GOLD_ORE:{
					dropType = Material.GOLD_INGOT;
					break;
				}
				default:{
					return;
				}
			}
			Location location = block.getLocation();
			World world = location.getWorld();
			ItemStack drop = new ItemStack(dropType, 1);
			world.dropItemNaturally(location, drop);
			block.setType(Material.AIR);
			block.getState().update();
		}
	}

}

