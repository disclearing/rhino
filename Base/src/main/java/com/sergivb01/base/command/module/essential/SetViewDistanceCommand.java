package com.sergivb01.base.command.module.essential;

import com.google.common.primitives.Ints;
import com.sergivb01.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class SetViewDistanceCommand extends BaseCommand{

	public SetViewDistanceCommand(){
		super("setviewdistance", "Starts global ban wave.");
		this.setAliases(new String[]{"renderdistance", "setrenderdistance", "svd", "srd"});
		this.setUsage("/(command) <vipsonly(true/false)> <distance>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length <= 1){
			sender.sendMessage(this.getUsage());
			return false;
		}

		boolean vipsonly = args[0].equalsIgnoreCase("true");
		Integer distance = Ints.tryParse(args[1]);

		if(distance == null){
			sender.sendMessage(RED + args[1] + " is not an integer u fatass!");
			return false;
		}

		if(distance <= 1){
			sender.sendMessage(RED + "View distance may not be less or equals to 1.");
			return false;
		}


		if(vipsonly){
			for(Player target : Bukkit.getOnlinePlayers()){
				if(target.hasPermission("utils.vipdistance")){
					target.spigot().setViewDistance(distance);
				}
			}
			sender.sendMessage(GREEN + "Set distance of VIPS (players with permission utils.vipdistance) to " + distance);
			return true;
		}

		for(Player target : Bukkit.getOnlinePlayers()){
			target.spigot().setViewDistance(distance);
		}


		return true;
	}
}