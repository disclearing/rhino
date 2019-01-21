package com.sergivb01.base.kit.argument;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.kit.Kit;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class KitGuiArgument
		extends CommandArgument{
	private final BasePlugin plugin;

	public KitGuiArgument(BasePlugin plugin){
		super("gui", "Opens the kit gui");
		this.plugin = plugin;
		this.aliases = new String[]{"menu"};
		this.permission = "command.kit.argument." + this.getName();
	}

	@Override
	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only players may open kit GUI's.");
			return true;
		}
		List<Kit> kits = this.plugin.getKitManager().getKits();
		if(kits.isEmpty()){
			sender.sendMessage(ChatColor.RED + "No kits have been defined.");
			return true;
		}
		Player player = (Player) sender;
		player.openInventory(this.plugin.getKitManager().getGui(player));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return Collections.emptyList();
	}
}

