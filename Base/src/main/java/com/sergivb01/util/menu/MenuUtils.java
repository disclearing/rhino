package com.sergivb01.util.menu;

import com.sergivb01.util.menu.mask.Mask;
import com.sergivb01.util.menu.mask.Mask2D;
import com.sergivb01.util.menu.slot.Slot;
import com.sergivb01.util.menu.type.ChestMenu;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class MenuUtils{

	public static Menu createMenu(String title, int rows){
		return ChestMenu.builder(rows)
				.title(title)
				.build();
	}

	public static void addClickHandler(Slot slot){
		slot.setClickHandler((player, info) -> {
			player.sendMessage("You clicked the slot at index " + info.getClickedSlot().getIndex());
			// Additional functionality goes here
		});
	}

	public static void addMaterialBorder(Inventory inventory, Menu menu, Material material, int amount){
		ItemStack glass = new ItemStack(material, amount);
		Mask mask = Mask2D.builder(menu).apply("111111111") // First row
				.nextRow().apply("100000001") // Second row
				.nextRow().apply("100000001") // Third row
				.nextRow().apply("111111111").build(); // Fourth row
		for(int slot : mask){
			menu.getSlot(slot).setItem(glass);
		}
	}

	public static void addBorder(Inventory inventory, Menu menu, Material material){
		ItemStack glass = new ItemStack(material);
		for(int i = 0; i < 9; i++){ // Setting first row
			menu.getSlot(i).setItem(glass);
		}
		menu.getSlot(17).setItem(glass);
		menu.getSlot(18).setItem(glass);
		for(int i = 26; i < 36; i++){
			menu.getSlot(i).setItem(glass);
		}
	}


}
