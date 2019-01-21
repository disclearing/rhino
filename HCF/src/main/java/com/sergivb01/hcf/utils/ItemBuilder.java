package com.sergivb01.hcf.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder{
	private Material type;
	private String name;
	private String[] lore;
	private int amount;
	private short data;
	private boolean unbreakable;

	public ItemBuilder(final Material type, final String name, final String... lore){
		this.type = type;
		this.name = name;
		this.lore = lore;
		this.amount = 1;
		this.data = 0;
	}

	public ItemBuilder(final Material type, final String name, final List<String> list, final String... lore){
		this.type = type;
		this.name = name;
		this.lore = lore;
		this.amount = 1;
		this.data = 0;
	}

	public ItemBuilder(final Material type, final String name, final boolean unbreakable, final String... lore){
		this.type = type;
		this.name = name;
		this.lore = lore;
		this.amount = 1;
		this.unbreakable = unbreakable;
		this.data = 0;
	}

	public ItemBuilder(final Material type, final String name, final int amount, final String... lore){
		this.type = type;
		this.name = name;
		this.lore = lore;
		this.amount = amount;
		this.data = 0;
	}

	public ItemBuilder(final Material type, final String name, final int amount, final byte data, final String... lore){
		this.type = type;
		this.name = name;
		this.lore = lore;
		this.amount = amount;
		this.data = data;
	}

	public ItemBuilder(final Material type, final String name, final int amount, final byte data, final List<String> lore){
		this.type = type;
		this.name = name;
		this.lore = lore.toArray(new String[lore.size()]);
		this.amount = amount;
		this.data = data;
	}

	public ItemBuilder(final ItemStack display, final String... lore){
		this.type = display.getType();
		this.name = display.getItemMeta().getDisplayName();
		this.lore = lore;
		this.amount = display.getAmount();
		this.data = display.getDurability();
	}

	public ItemStack getItem(){
		final List<String> lore = new ArrayList<String>();
		for(int i = 0; i < this.lore.length; ++i){
			lore.add(ChatColor.translateAlternateColorCodes('&', this.lore[i]));
		}
		final ItemStack item = new ItemStack(this.type, this.amount, this.data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(lore);
		meta.spigot().setUnbreakable(this.unbreakable);
		item.setItemMeta(meta);
		return item;
	}


}
