package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ToggleDonorOnly
		extends BaseCommand{
	BasePlugin plugin;

	public ToggleDonorOnly(BasePlugin plugin){
		super("toggledonoronly", "Turns the server into Donor only mode.");
		this.setUsage("/(command)");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		this.plugin.getServerHandler().setDonorOnly(!this.plugin.getServerHandler().isDonorOnly());
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Server is " + (!this.plugin.getServerHandler().isDonorOnly() ? new StringBuilder().append(ChatColor.RED).append("not").toString() : new StringBuilder().append(ChatColor.GREEN).append("now").toString()) + ChatColor.YELLOW + " in donor only mode.");
		return true;
	}
}

