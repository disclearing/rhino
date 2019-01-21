package com.sergivb01.hcf.commands;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.hcf.HCF;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public class HelpCommand implements CommandExecutor, TabCompleter{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		for(String messages : HCF.getInstance().getConfig().getStringList("help")){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages).replace("%OVERWORLD%", BasePlugin.getPlugin().getServerHandler().getWorldBorder() + "")
					.replace("%NETHER%", BasePlugin.getPlugin().getServerHandler().getNetherBorder() + "")
					.replace("%END%", BasePlugin.getPlugin().getServerHandler().getEndBorder() + ""));
		}

		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return Collections.emptyList();
	}
}

