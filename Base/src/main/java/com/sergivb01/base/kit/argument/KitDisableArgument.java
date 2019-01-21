package com.sergivb01.base.kit.argument;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.kit.Kit;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitDisableArgument
		extends CommandArgument{
	private final BasePlugin plugin;

	public KitDisableArgument(BasePlugin plugin){
		super("disable", "Disable or enable a kit");
		this.plugin = plugin;
		this.aliases = new String[]{"enable", "toggle"};
		this.permission = "command.kit.argument." + this.getName();
	}

	@Override
	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName() + " <kitName>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length < 2){
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		Kit kit = this.plugin.getKitManager().getKit(args[1]);
		if(kit == null){
			sender.sendMessage(ChatColor.RED + "No kit named " + args[1] + " found.");
			return true;
		}
		boolean newEnabled = !kit.isEnabled();
		kit.setEnabled(newEnabled);
		sender.sendMessage(ChatColor.AQUA + "Kit " + kit.getDisplayName() + " has been " + (newEnabled ? new StringBuilder().append(ChatColor.GREEN).append("enabled").toString() : new StringBuilder().append(ChatColor.RED).append("disabled").toString()) + ChatColor.AQUA + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		if(args.length != 2){
			return Collections.emptyList();
		}
		List<Kit> kits = this.plugin.getKitManager().getKits();
		ArrayList<String> results = new ArrayList<String>(kits.size());
		for(Kit kit : kits){
			results.add(kit.getName());
		}
		return results;
	}
}

