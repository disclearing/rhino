package com.sergivb01.util.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.Permissible;

import java.util.Iterator;
import java.util.Set;

public class StaffMessage{


	public static void broadcastFilter(CommandSender source, String message){
		broadcastFilter(source, message, true);
	}

	public static void broadcastFilter(CommandSender source, String message, boolean sendToSource){
		String result = source.getName() + " " + message;
		if(source instanceof BlockCommandSender){
			BlockCommandSender blockCommandSender = (BlockCommandSender) source;
			if(blockCommandSender.getBlock().getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")){
				Bukkit.getConsoleSender().sendMessage(result);
				return;
			}
		}else if(source instanceof CommandMinecart){
			CommandMinecart commandMinecart = (CommandMinecart) source;
			if(commandMinecart.getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")){
				Bukkit.getConsoleSender().sendMessage(result);
				return;
			}
		}

		Set<Permissible> users = Bukkit.getPluginManager().getPermissionSubscriptions("bukkit.broadcast.staff");
		String colored = ChatColor.GOLD + "" + ChatColor.ITALIC + "[Filter] " + ChatColor.GRAY + "" + ChatColor.ITALIC + result;
		if(sendToSource && !(source instanceof ConsoleCommandSender)){
			source.sendMessage(message);
		}
		Iterator var1 = users.iterator();

		while(var1.hasNext()){
			Permissible user = (Permissible) var1.next();
			if(user instanceof CommandSender){
				CommandSender target = (CommandSender) user;
				if(target instanceof ConsoleCommandSender){
					target.sendMessage(result);
				}else if(target != source){
					target.sendMessage(colored);
				}
			}
		}

	}

}
