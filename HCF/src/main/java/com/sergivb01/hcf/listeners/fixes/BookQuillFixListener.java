package com.sergivb01.hcf.listeners.fixes;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class BookQuillFixListener implements Listener{

	public BookQuillFixListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void craftBookEvent(PrepareItemCraftEvent e){
		Material itemType = e.getRecipe().getResult().getType();
		if(itemType == Material.BOOK_AND_QUILL){
			e.getInventory().setResult(new ItemStack(Material.AIR));
			for(HumanEntity he : e.getViewers()){
				if(he instanceof Player){
					((Player) he).sendMessage(ChatColor.RED + "This item is disabled.");
				}
			}
		}
	}
}