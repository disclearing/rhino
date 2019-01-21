package com.sergivb01.base.command.module.warp;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.warp.Warp;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class WarpSetArgument
		extends CommandArgument{
	private final BasePlugin plugin;

	public WarpSetArgument(BasePlugin plugin){
		super("set", "Sets a new server warps");
		this.plugin = plugin;
		this.aliases = new String[]{"create", "make"};
		this.permission = "command.warp.argument." + this.getName();
	}

	@Override
	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		if(args.length < 2){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + this.getName() + " <warpName>");
			return true;
		}
		if(this.plugin.getWarpManager().getWarp(args[1]) != null){
			sender.sendMessage(ChatColor.RED + "There is already a warp named " + args[1] + '.');
			return true;
		}
		Player player = (Player) sender;
		Location location = player.getLocation();
		Warp warp = new Warp(args[1], location);
		this.plugin.getWarpManager().createWarp(warp);
		sender.sendMessage(ChatColor.GRAY + "Created a global warp named " + ChatColor.BLUE + warp.getName() + ChatColor.GRAY + " with permission " + ChatColor.BLUE + warp.getPermission() + ChatColor.GRAY + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return Collections.emptyList();
	}
}

