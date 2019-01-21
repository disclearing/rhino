package com.sergivb01.util.command;

import com.google.common.collect.ImmutableList;
import com.sergivb01.util.BukkitUtils;
import com.sergivb01.util.chat.ClickAction;
import com.sergivb01.util.chat.Text;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ArgumentExecutor
		implements CommandExecutor,
		TabCompleter{
	protected final List<CommandArgument> arguments = new ArrayList<CommandArgument>();
	protected final String label;

	public ArgumentExecutor(String label){
		this.label = label;
	}

	public boolean containsArgument(CommandArgument argument){
		return this.arguments.contains(argument);
	}

	public void addArgument(CommandArgument argument){
		this.arguments.add(argument);
	}

	public void removeArgument(CommandArgument argument){
		this.arguments.remove(argument);
	}

	public CommandArgument getArgument(String id){
		for(CommandArgument argument : this.arguments){
			String name = argument.getName();
			if(!name.equalsIgnoreCase(id) && !Arrays.asList(argument.getAliases()).contains(id.toLowerCase())) continue;
			return argument;
		}
		return null;
	}

	public String getLabel(){
		return this.label;
	}

	public List<CommandArgument> getArguments(){
		return ImmutableList.copyOf(this.arguments);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		String permission2;
		if(args.length < 1){
			sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + WordUtils.capitalizeFully(label) + ChatColor.AQUA + ChatColor.BOLD.toString() + " Help" + ChatColor.GRAY + " (Page 1 out of 1)");
			sender.sendMessage(" ");
			for(CommandArgument argument : this.arguments){
				String permission = argument.getPermission();
				if(permission != null && !sender.hasPermission(permission)) continue;
				new Text(ChatColor.BLUE + argument.getUsage(label) + ChatColor.GRAY + " - " + ChatColor.WHITE + argument.getDescription()).setClick(ClickAction.SUGGEST_COMMAND, "/" + argument.getUsage(label)).setColor(ChatColor.GRAY).send(sender);
			}
			sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			return true;
		}
		CommandArgument argument2 = this.getArgument(args[0]);
		String string = permission2 = argument2 == null ? null : argument2.getPermission();
		if(argument2 == null || permission2 != null && !sender.hasPermission(permission2)){
			sender.sendMessage(ChatColor.RED + WordUtils.capitalizeFully(this.label) + " sub-command " + args[0] + " not found.");
			return true;
		}
		argument2.onCommand(sender, command, label, args);
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		List results = new ArrayList<String>();
		if(args.length < 2){
			for(CommandArgument argument : this.arguments){
				String permission = argument.getPermission();
				if(permission != null && !sender.hasPermission(permission)) continue;
				results.add(argument.getName());
			}
		}else{
			CommandArgument argument2 = this.getArgument(args[0]);
			if(argument2 == null){
				return results;
			}
			String permission2 = argument2.getPermission();
			if((permission2 == null || sender.hasPermission(permission2)) && (results = argument2.onTabComplete(sender, command, label, args)) == null){
				return null;
			}
		}
		return BukkitUtils.getCompletions(args, results);
	}
}

