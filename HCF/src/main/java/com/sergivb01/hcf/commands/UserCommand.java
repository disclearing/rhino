package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserCommand implements CommandExecutor,
		TabCompleter{
	private final HCF plugin;

	public UserCommand(HCF plugin){
		this.plugin = plugin;
	}

	private static Inventory getMenu(final OfflinePlayer player){
		final Inventory inventory = Bukkit.createInventory(null, 9, player.getName() + (player.getName().endsWith("s") ? "'" : "'s") + " Punishments");

		ItemStack a = new ItemStack(160, 1, (short) 14);
		ItemMeta aMeta = a.getItemMeta();
		aMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lRevoke"));
		aMeta.setLore(Arrays.asList(ChatColor.GRAY + "", ChatColor.RED.toString() + ChatColor.ITALIC + "idk"));
		a.setItemMeta(aMeta);
		inventory.setItem(2, a);


		ItemStack b = new ItemStack(160, 1, (short) 14);
		ItemMeta bMeta = b.getItemMeta();
		bMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lOverview"));
		bMeta.setLore(Arrays.asList(ChatColor.GRAY + "", ChatColor.GRAY + "View an overview of this player"));
		b.setItemMeta(bMeta);
		inventory.setItem(4, b);


		ItemStack c = new ItemStack(160, 1, (short) 14);
		ItemMeta cMeta = c.getItemMeta();
		cMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lPermissions"));
		cMeta.setLore(Arrays.asList(ChatColor.GRAY + "", ChatColor.GRAY + "View a list of permissions"));
		c.setItemMeta(cMeta);
		inventory.setItem(6, c);


		return inventory;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		//if(target.hasPlayedBefore()){
		((Player) sender).openInventory(getMenu(target));
		//} else {
		//	sender.sendMessage(ChatColor.RED + "This player has not played before.");
		//	}

		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return Collections.emptyList();
	}
}