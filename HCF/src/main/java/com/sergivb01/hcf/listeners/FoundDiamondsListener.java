package com.sergivb01.hcf.listeners;

import com.sergivb01.hcf.HCF;
import com.sergivb01.util.chat.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class FoundDiamondsListener implements Listener{
	public static String DIAMOND_ORE_ALERTS = "FD_ALERTS";

	public FoundDiamondsListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event){
		Block block = event.getBlock();
		if(block.getType().equals(Material.DIAMOND_ORE)){
			block.setMetadata("FoundDiamond", new FixedMetadataValue(HCF.getPlugin(), Boolean.TRUE));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(player.getGameMode() == GameMode.CREATIVE){
			return;
		}
		if(player.getItemInHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)){
			return;
		}
		Block block = event.getBlock();
		if((block.getType().equals(Material.DIAMOND_ORE)) && (!block.hasMetadata("FoundDiamond"))){
			int i = 0;
			for(int j = -5; j < 5; j++){
				for(int k = -5; k < 5; k++){
					for(int m = -5; m < 5; m++){
						Block localBlock2 = block.getRelative(j, k, m);
						if((localBlock2.getType().equals(Material.DIAMOND_ORE))
								&& (!localBlock2.hasMetadata("FoundOre"))){
							i++;
							localBlock2.setMetadata("FoundDiamond",
									new FixedMetadataValue(HCF.getPlugin(), Boolean.TRUE));
						}
					}
				}
			}
			if(i == 1){
				Text message = new Text(ChatColor.translateAlternateColorCodes('&', "&f[FD]&b " + player.getName() + " has found&b " + i + " diamonds."));
				for(Player other : Bukkit.getOnlinePlayers()){
					if(!other.hasMetadata("FD_ALERTS")){
						message.send(other);
					}
				}
			}else{
				Text message1 = new Text(ChatColor.translateAlternateColorCodes('&', "&f[FD]&b " + player.getName() + " has found&b " + i + " diamonds."));
				for(Player other : Bukkit.getOnlinePlayers()){
					if(!other.hasMetadata("FD_ALERTS")){
						message1.send(other);
					}
				}
			}
		}
	}
}

