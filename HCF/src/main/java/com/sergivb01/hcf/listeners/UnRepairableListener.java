package com.sergivb01.hcf.listeners;

import com.sergivb01.hcf.HCF;
import me.sergivb01.event.PrepareAnvilRepairEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class UnRepairableListener implements Listener{

	public UnRepairableListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onRepair(PrepareAnvilRepairEvent e){
		for(ItemStack itemStack : e.getInventory().getContents()){
			if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()){
				continue;
			}

			for(String lore : itemStack.getItemMeta().getLore()){
				String fixedLore = ChatColor.stripColor(lore.toLowerCase());
				if(!fixedLore.contains("no repair") && !fixedLore.contains("unrepairable") && !fixedLore.contains("norepair") && !fixedLore.contains("nofix") && !fixedLore.contains("no fix"))
					continue;
				e.setCancelled(true);
				e.setResult(new ItemStack(Material.AIR));
				e.getRepairer().closeInventory();
				((Player) e.getRepairer()).sendMessage(ChatColor.RED + "This item cannot be repaired.");
			}
		}
	}
}

