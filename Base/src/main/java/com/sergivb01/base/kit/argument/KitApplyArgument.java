package com.sergivb01.base.kit.argument;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.kit.Kit;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitApplyArgument
		extends CommandArgument{
	private final BasePlugin plugin;

	public KitApplyArgument(BasePlugin plugin){
		super("apply", "Applies a kit to player");
		this.plugin = plugin;
		this.permission = "command.kit.argument." + this.getName();
	}

	@Override
	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName() + " <kitName> <playerName>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length < 3){
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		Kit kit = this.plugin.getKitManager().getKit(args[1]);
		if(kit == null){
			sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
			return true;
		}
		Player target = Bukkit.getPlayer(args[2]);
		if(target == null || sender instanceof Player && !((Player) sender).canSee(target)){
			sender.sendMessage(ChatColor.RED + "Player '" + ChatColor.GRAY + args[2] + ChatColor.RED + "' not found.");
			return true;
		}
		if(kit.applyTo(target, true, true)){
			sender.sendMessage(ChatColor.GRAY + "Applied kit '" + kit.getDisplayName() + "' to '" + target.getName() + "'.");
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Failed to apply kit " + kit.getDisplayName() + " to " + target.getName() + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		if(args.length == 2){
			List<Kit> kits = this.plugin.getKitManager().getKits();
			ArrayList<String> results = new ArrayList<String>(kits.size());
			for(Kit kit : kits){
				results.add(kit.getName());
			}
			return results;
		}
		if(args.length == 3){
			return null;
		}
		return Collections.emptyList();
	}
}

