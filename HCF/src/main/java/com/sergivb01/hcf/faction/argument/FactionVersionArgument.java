package com.sergivb01.hcf.faction.argument;

import com.sergivb01.hcf.HCF;
import com.sergivb01.util.BukkitUtils;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FactionVersionArgument
		extends CommandArgument{
	private final HCF plugin;

	public FactionVersionArgument(HCF plugin){
		super("version", "View plugin information.");
		this.plugin = plugin;
	}

	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8" + BukkitUtils.STRAIGHT_LINE_DEFAULT));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThis server is running &6" + HCF.getPlugin().getDescription().getName() + "&e. Version: &6" + HCF.getPlugin().getDescription().getVersion()));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eVersion by: &6sergivb01"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oThis version can be remotely disabled."));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8" + BukkitUtils.STRAIGHT_LINE_DEFAULT));
		return true;
	}


}

